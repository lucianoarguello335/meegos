package utn.tdm.meegos.manager;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.Telephony;

import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Comparator;

import utn.tdm.meegos.R;
import utn.tdm.meegos.database.EventsSQLiteHelper;
import utn.tdm.meegos.domain.Contacto;
import utn.tdm.meegos.domain.Evento;
import utn.tdm.meegos.preferences.MeegosPreferences;

public class EventoManager {

    private final Context context;
    EventsSQLiteHelper eventsSQLiteHelper;
    ContactManager contactManager;
    MeegosPreferences meegosPreferences;

    public EventoManager(Context context) {
        this.context = context;
        this.eventsSQLiteHelper = new EventsSQLiteHelper(context, EventsSQLiteHelper.DB_NAME, null, EventsSQLiteHelper.CURRENT_DB_VERSION);
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
                Telephony.Sms.CONTENT_URI,
                null,
                Telephony.Sms.TYPE + " = " + Telephony.Sms.MESSAGE_TYPE_SENT,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String phone_numbre = cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS));
            Contacto contacto = contactManager.findContactByPhoneNumbre(phone_numbre);
            if (contacto != null && contacto.getId() == contactoId) {
                eventos.add(
                    new Evento(
                        cursor.getLong(cursor.getColumnIndex(Telephony.Sms._ID)),
                        Evento.SMS,
                        cursor.getLong(cursor.getColumnIndex(Telephony.Sms.DATE)),
                        contacto.getId(),
                        contacto.getLookupKey(),
                        contacto.getNombre(),
                        phone_numbre,
                        Evento.SALIENTE,
                        cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY))
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
        Cursor cursor = eventsSQLiteHelper.getEventos(contactId, selection);
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
        int result = eventsSQLiteHelper.deleteEvento(evento.getId());
        return result;
    }

    private int deleteOutgoingCall(Evento evento) {
        ContentResolver cr = context.getContentResolver();

        @SuppressLint("MissingPermission")
        int result = cr.delete(
                CallLog.Calls.CONTENT_URI,
                CallLog.Calls._ID + " = " + evento.getId(),
                null
        );
        return result;
    }

    private int deleteSentSMS(Evento evento) {
        ContentResolver cr = context.getContentResolver();

        @SuppressLint("MissingPermission")
        int result = cr.delete(
                Telephony.Sms.CONTENT_URI,
                Telephony.Sms._ID + " = " + evento.getId(),
                null
        );
        return result;
    }
}
