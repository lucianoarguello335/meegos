package utn.tdm.meegos.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class EventsSQLiteHelper extends SQLiteOpenHelper {

    public static String DB_NAME = "meegosdb";
    public static int CURRENT_DB_VERSION = 1;
    public static long CURRENT_ID;

    private static SQLiteDatabase db;

    public EventsSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT MAX(id) FROM Eventos", null);
        if (c.moveToFirst()) {
            CURRENT_ID = c.getInt(0);
        } else {
            CURRENT_ID = 0;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreate = "Create TABLE Eventos(id INTEGER, tipo_evento INTEGER, fecha INTEGER, " +
                "contacto_id INTEGER, contacto_lookup TEXT, contacto_nombre TEXT, contacto_numero TEXT, " +
                "origen INTEGER, sms_web TEXT)";
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop TABLE if exists Eventos");
        onCreate(db);
    }

    /**
     * Para registrar un evento.
     * @param tipo_evento
     * @param fecha
     * @param contacto_id
     * @param contacto_nombre
     * @param contacto_numero
     * @param origen
     * @param sms_web
     */
    public void insertEvento(int tipo_evento, long fecha,
                             long contacto_id, String contacto_lookup, String contacto_nombre,
                             String contacto_numero, int origen, String sms_web) {
        CURRENT_ID++;

        ContentValues nuevoEvento = new ContentValues();
        nuevoEvento.put("id", CURRENT_ID);
        nuevoEvento.put("tipo_evento", tipo_evento);
        nuevoEvento.put("fecha", fecha);
        nuevoEvento.put("contacto_id", contacto_id);
        nuevoEvento.put("contacto_lookup", contacto_lookup);
        nuevoEvento.put("contacto_nombre", contacto_nombre);
        nuevoEvento.put("contacto_numero", contacto_numero);
        nuevoEvento.put("origen", origen);
        nuevoEvento.put("sms_web", sms_web);

        SQLiteDatabase db = getWritableDatabase();
        db.insert("Eventos", null, nuevoEvento);

        db.close();
    }

    public int deleteEvento(long id) {
        db = getWritableDatabase();
        int outcome = db.delete(
                "Eventos",
                "id=?",
                new String[]{String.valueOf(id)}
                );
        db.close();
        return outcome;
    }

    /**
     *
     * @param contacto_id int > 0
     * @param tipos_evento string format "1,2,3"
     * @param origenes string format "1,2"
     */
    public Cursor getEventos(long contacto_id, String tipos_evento, String origenes) {

        StringBuilder selection = new StringBuilder();
        ArrayList<String> argumentsList = new ArrayList();
        if (contacto_id > 0) {
            selection.append("contacto_id=?");
            argumentsList.add(String.valueOf(contacto_id));
        }
        if (tipos_evento != null && !tipos_evento.isEmpty()){
            selection.append(" AND tipo_evento IN (?)");
            argumentsList.add(tipos_evento);
        }
        if (origenes != null && !origenes.isEmpty()){
            selection.append(" AND origenes IN (?)");
            argumentsList.add(origenes);
        }
        String[] arguments = new String[argumentsList.size()];
        for (int i = 0; i < argumentsList.size(); i++) {
            arguments[i] = argumentsList.get(i);
        }

        db = getWritableDatabase();
        Cursor c = db.query(
                "Eventos",
                null,
                selection.toString(),
                arguments,
                null,
                null,
                null
        );
        return c;
    }
}
