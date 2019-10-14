package utn.tdm.meegos.manager;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.Telephony;

import java.util.ArrayList;
import java.util.Comparator;

import utn.tdm.meegos.database.MeegosSQLHelper;
import utn.tdm.meegos.domain.Contacto;
import utn.tdm.meegos.domain.Evento;
import utn.tdm.meegos.preferences.MeegosPreferences;

public class EventoManager {

    private final Context context;
    MeegosSQLHelper meegosSQLHelper;
    ContactManager contactManager;

    public EventoManager(Context context) {
        this.context = context;
        this.meegosSQLHelper = new MeegosSQLHelper(context);
        this.contactManager = new ContactManager(context);
    }

    public ArrayList<Evento> findAllOutgoingCall(String contactoNombre) {
        ArrayList<Evento> eventos = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();

        @SuppressLint("MissingPermission")
        Cursor cursor = cr.query(
                CallLog.Calls.CONTENT_URI,
                null,
                CallLog.Calls.TYPE + " = " + CallLog.Calls.OUTGOING_TYPE
                        + " AND " + CallLog.Calls.CACHED_NAME + " LIKE '" + contactoNombre + "'",
                null,
                null
        );

        while (cursor.moveToNext()) {
            String phone_numbre = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            Contacto contacto = contactManager.findContactByPhoneNumbre(phone_numbre);
            if (contacto != null) {
                eventos.add(
                        new Evento(
                                cursor.getLong(cursor.getColumnIndex(CallLog.Calls._ID)),
                                Evento.LLAMADA,
                                cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)),
                                contacto.getId(),
                                contacto.getLookupKey(),
                                contacto.getNombre(),
                                phone_numbre,
                                Evento.SALIENTE,
                                ""
                        )
                );
            }
        }

        cursor.close();
        return eventos;
    }

    public ArrayList<Evento> findAllSentSMS(Long contactoId) {
        ArrayList<Evento> eventos = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(
                Telephony.Sms.Sent.CONTENT_URI,
                null,
                Telephony.Sms.Sent.TYPE + " = " + Telephony.Sms.Sent.MESSAGE_TYPE_SENT,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String phone_number = cursor.getString(cursor.getColumnIndex(Telephony.Sms.Sent.ADDRESS));
            Contacto contacto = contactManager.findContactByPhoneNumbre(phone_number);
            if (contacto != null && contacto.getId() == contactoId) {
                eventos.add(
                        new Evento(
                                cursor.getLong(cursor.getColumnIndex(Telephony.Sms.Sent._ID)),
                                Evento.SMS,
                                cursor.getLong(cursor.getColumnIndex(Telephony.Sms.Sent.DATE)),
                                contacto.getId(),
                                contacto.getLookupKey(),
                                contacto.getNombre(),
                                phone_number,
                                Evento.SALIENTE,
                                cursor.getString(cursor.getColumnIndex(Telephony.Sms.Sent.BODY))
                        )
                );
            }
        }
        cursor.close();
        return eventos;
    }

    public ArrayList<Evento> findEventosByContact(long contactId, String contactLookup, String contactNombre) {
        String selection = "";
        if (MeegosPreferences.isHistoryCallFiltered(context)) {
            selection = "tipo_evento = " + Evento.LLAMADA;
            if (MeegosPreferences.isHistorySMSFiltered(context)) {
                selection += " OR tipo_evento = " + Evento.SMS;
            }
        } else if (MeegosPreferences.isHistorySMSFiltered(context)) {
            selection = "tipo_evento = " + Evento.SMS;
        }

        ArrayList<Evento> eventos = new ArrayList<>();
        Cursor cursor = meegosSQLHelper.getEventos(contactId, selection);
        while (cursor.moveToNext()) {
            eventos.add(
                    new Evento(
                            cursor.getLong(0),
                            cursor.getInt(1),
                            cursor.getLong(2),
                            cursor.getLong(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getInt(7),
                            cursor.getString(8)
                    )
            );
        }
        cursor.close();

//        Agregamos las llamadas realizadas de ese usuario
        if (MeegosPreferences.isHistoryCallFiltered(context))
            eventos.addAll(findAllOutgoingCall(contactNombre));

//        Agregamos los mensajes enviados por ese usuario
        if (MeegosPreferences.isHistorySMSFiltered(context))
            eventos.addAll(findAllSentSMS(contactId));

        // Ordenamos el resultado
        eventos.sort(new Comparator<Evento>() {
            @Override
            public int compare(Evento e1, Evento e2) {
                if (MeegosPreferences.getHistoryOrder(context).equals("fecha ASC")) {
                    return Long.compare(e2.getFecha(), e1.getFecha());
                } else {
                    return Long.compare(e1.getFecha(), e2.getFecha());
                }
            }
        });

        return eventos;
    }

    public int deleteEvento(Evento evento) {
        int result = -1;
        if (evento.getOrigen() == Evento.ENTRANTE) {
            result = deleteEventoEntrante(evento);
        } else if (evento.getOrigen() == Evento.SALIENTE) {
            if (evento.getTipo() == Evento.LLAMADA) {
                result = deleteOutgoingCall(evento);
            } else if (evento.getTipo() == Evento.SMS) {
                result = deleteSentSMS(evento);
            } else if (evento.getTipo() == Evento.CHAT) {
                result = deleteEventoEntrante(evento);
            }
        }
        return result;
    }

    private int deleteEventoEntrante(Evento evento) {
        int result = meegosSQLHelper.deleteEvento(evento.getId());
        return result;
    }

    private int deleteOutgoingCall(Evento evento) {
        ContentResolver cr = context.getContentResolver();

        @SuppressLint("MissingPermission")
        int result = cr.delete(
                CallLog.Calls.CONTENT_URI,
                CallLog.Calls._ID + " = ?",
                new String[]{String.valueOf(evento.getId())}
        );
        return result;
    }

    /**
     * Only the default SMS app (selected by the user in system settings) is able to write
     * to the SMS Provider (the tables defined within the Telephony class).
     *
     * Other apps that are not selected as the default SMS app can only read the SMS Provider,
     * but may also be notified when a new SMS arrives by listening for the
     * Telephony.Sms.Intents.SMS_RECEIVED_ACTION broadcast, which is a non-abortable broadcast
     * that may be delivered to multiple apps. This broadcast is intended for apps that—while
     * not selected as the default SMS app—need to read special incoming messages
     * such as to perform phone number verification.
     *
     * https://developer.android.com/reference/android/provider/Telephony.html
     *
     * @param evento
     * @return
     */
    private int deleteSentSMS(Evento evento) {
        ContentResolver cr = context.getContentResolver();

        int result = cr.delete(
                Telephony.Sms.Sent.CONTENT_URI,
                Telephony.Sms.Sent._ID + " = ?",
                new String[]{String.valueOf(evento.getId())}
        );
        return result;
    }
}
