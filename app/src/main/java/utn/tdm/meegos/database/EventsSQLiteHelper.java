package utn.tdm.meegos.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventsSQLiteHelper extends SQLiteOpenHelper {

    public static String DB_NAME = "meegosdb";
    public static int CURRENT_DB_VERSION = 1;
    public static int CURRENT_ID;

    public EventsSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT MAX(id) FROM Eventos", null);
        if (c.moveToFirst()) {
            CURRENT_ID = c.getInt(0);
        } else {
            CURRENT_ID = 0;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreate = "Create TABLE Eventos(id INTEGER, tipo_evento INTEGER, fecha INTEGER, contacto_id INTEGER, contacto_nombre TEXT, origen INTEGER, sms_web TEXT)";
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
     * @param origen
     * @param sms_web
     */
    public void insertEvento(int tipo_evento, long fecha, long contacto_id, String contacto_nombre, int origen, String sms_web) {
        CURRENT_ID++;

        ContentValues nuevoEvento = new ContentValues();
        nuevoEvento.put("id", CURRENT_ID);
        nuevoEvento.put("tipo_evento", tipo_evento);
        nuevoEvento.put("fecha", fecha);
        nuevoEvento.put("contacto_id", contacto_id);
        nuevoEvento.put("contacto_nombre", contacto_nombre);
        nuevoEvento.put("origen", origen);
        nuevoEvento.put("sms_web", sms_web);

        SQLiteDatabase db = getWritableDatabase();
        db.insert("Eventos", null, nuevoEvento);

        db.close();
    }

    public boolean deleteEvento(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int outcome;
        outcome = db.delete("Eventos", "id=?", new String[]{String.valueOf(id)});
        db.close();
        if (outcome == 1) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param contacto_id int > 0
     * @param tipo_eventos string format "1,2,3,4"
     * @param origen string format "1,2"
     */
    public Cursor getEventos(int contacto_id, String tipo_eventos, String origen) {

        String[] arguments = {String.valueOf(contacto_id), tipo_eventos, origen};

        StringBuilder selection = new StringBuilder();
        selection.append("contacto_id=?");
        if (tipo_eventos != null){
            selection.append(" AND tipo_evento IN (?)");
        }
        if (tipo_eventos != null){
            selection.append(" AND origen IN (?)");
        }

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query("Eventos", null, selection.toString(), arguments, null, null, null);
        db.close();

        return c;
    }
}
