package utn.tdm.meegos.manager;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Comparator;

import utn.tdm.meegos.database.MeegosSQLHelper;
import utn.tdm.meegos.domain.Chat;
import utn.tdm.meegos.domain.Transaccion;
import utn.tdm.meegos.preferences.MeegosPreferences;

public class TransaccionManager {

    private Context context;
    MeegosSQLHelper meegosSQLHelper;

    public TransaccionManager(Context context) {
        this.context = context;
        this.meegosSQLHelper = new MeegosSQLHelper(context);
    }

    public void saveTransaccion(Transaccion transacccion) {
        meegosSQLHelper.insertTransaccion(
                transacccion.getRequestId(),
                transacccion.getRequestName(),
                transacccion.getResponseType(),
                transacccion.getErrorCode(),
                transacccion.getTimestamp()
        );
    }

    public ArrayList<Transaccion> findAllTransacciones() {
        ArrayList<Transaccion> transacciones = new ArrayList<>();
        String selection = "";
        //TODO: Usar PREFERNCES para la busqueda
//        if (MeegosPreferences.isch HistoryCallFiltered(context)) {
//            selection = "tipo_evento = " + Evento.LLAMADA;
//            if (MeegosPreferences.isHistorySMSFiltered(context)) {
//                selection += " OR tipo_evento = " + Evento.SMS;
//            }
//        } else if (MeegosPreferences.isHistorySMSFiltered(context)) {
//            selection = "tipo_evento = " + Evento.SMS;
//        }
        Cursor cursor = meegosSQLHelper.findAllTransacciones();
        while (cursor.moveToNext()) {
            transacciones.add(
                new Transaccion(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
                )
            );
        }
        cursor.close();

        // Ordenamos el resultado
        //TODO: Setear el ordenamiento
        transacciones.sort(new Comparator<Transaccion>() {
            @Override
            public int compare(Transaccion t1, Transaccion t2) {
                    return t2.getTimestamp().compareTo(t1.getTimestamp());
//                if (MeegosPreferences.getHistoryOrder(context).equals("fecha ASC")) {
//                    return t2.getTimestamp().compareTo(t1.getTimestamp());
//                } else {
//                    return t1.getTimestamp().compareTo(t2.getTimestamp());
//                }
            }
        });

        return transacciones;
    }

    public Cursor findAllTransaccionesCursor() {
        Cursor cursor = meegosSQLHelper.findAllTransacciones();
        return cursor;
    }
}
