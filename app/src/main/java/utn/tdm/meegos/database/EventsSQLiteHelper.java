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

    private static SQLiteDatabase db;

    public EventsSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlEventosCreate = "Create TABLE Eventos(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tipo_evento INTEGER, fecha INTEGER, contacto_id INTEGER, contacto_lookup TEXT, " +
                "contacto_nombre TEXT, contacto_numero TEXT, origen INTEGER, sms_web TEXT);";
        db.execSQL(sqlEventosCreate);

        String sqlTransactionsCreate = "Create TABLE Transacciones(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "request_name TEXT, response_type TEXT, fecha INTEGER);";
        db.execSQL(sqlTransactionsCreate);

        String sqlAliasCreate = "Create TABLE Alias(contacto_lookup TEXT, contacto_alias TEXT);";
        db.execSQL(sqlAliasCreate);

        String sqlChatCreate = "Create TABLE Chats(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "timestamp TEXT, toAlias TEXT, fromAlias TEXT, message TEXT);";
        db.execSQL(sqlChatCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop TABLE if exists Eventos");
        db.execSQL("Drop TABLE if exists Transacciones");
        db.execSQL("Drop TABLE if exists Alias");
        db.execSQL("Drop TABLE if exists Chats");
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

        ContentValues nuevoEvento = new ContentValues();

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
                "_id=?",
                new String[]{String.valueOf(id)}
                );
        db.close();
        return outcome;
    }

    /**
     *
     * @param contacto_id int > 0
     * @param selectionArguments string con la clausula where completa
     */
    public Cursor getEventos(long contacto_id, String selectionArguments) {
        String selection = "contacto_id=?";
        String[] arguments;

        if(selectionArguments != null || !selectionArguments.isEmpty()) {
            selection += " AND " + selectionArguments;
        }
        arguments = new String[]{
            String.valueOf(contacto_id)
        };

        db = getWritableDatabase();
        Cursor c = db.query(
                "Eventos",
                null,
                selection,
                arguments,
                null,
                null,
                null
        );
        return c;
    }

    public void insertTransaccion(String id, String requestName, String responseType, long date) {
        ContentValues nuevaTransaccion = new ContentValues();
//        nuevaTransaccion.put("_id", id);
        nuevaTransaccion.put("request_name", requestName);
        nuevaTransaccion.put("response_type", responseType);
        nuevaTransaccion.put("fecha", date);

        SQLiteDatabase db = getWritableDatabase();
        db.insert("Transacciones", null, nuevaTransaccion);

        db.close();
    }

    public Cursor getAllTransacciones() {
        db = getWritableDatabase();
        Cursor c = db.query(
                "Transacciones",
                null,
                null,
                null,
                null,
                null,
                null
        );
        return c;
    }

    public Cursor getAliasByContactLookupKey(String contactLookupKey) {
        db = getWritableDatabase();
        Cursor c = db.query(
                "Alias",
                null,
                "contacto_lookup = ?",
                new String[]{contactLookupKey},
                null,
                null,
                null
        );
        return c;
    }

    public long insertChat(long timestamp, String from, String to, String message) {
        ContentValues nuevaTransaccion = new ContentValues();
        nuevaTransaccion.put("timestamp", timestamp);
        nuevaTransaccion.put("to", to);
        nuevaTransaccion.put("from", from);
        nuevaTransaccion.put("message", message);

        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert("Chats", null, nuevaTransaccion);

        db.close();
        return result;
    }

    public Cursor findChatsByAlias(String selectionArguments){
        db = getWritableDatabase();
        Cursor c = db.query(
                "Chats",
                null,
                selectionArguments,
                null,
                null,
                null,
                null
        );
        return c;
    }
}
