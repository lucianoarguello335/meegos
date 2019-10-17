package utn.tdm.meegos.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import utn.tdm.meegos.domain.Transaccion;

public class MeegosSQLHelper extends SQLiteOpenHelper {

    public static String DB_NAME = "meegosdb";
    public static int CURRENT_DB_VERSION = 1;

    public MeegosSQLHelper(Context context) {
        super(context, DB_NAME, null, CURRENT_DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlEventosCreate = "Create TABLE Eventos(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tipo_evento INTEGER, fecha INTEGER, contacto_id INTEGER, contacto_lookup TEXT, " +
                "contacto_nombre TEXT, contacto_numero TEXT, origen INTEGER, sms_web TEXT);";
        db.execSQL(sqlEventosCreate);

        String sqlTransactionsCreate = "Create TABLE Transacciones(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "request_id TEXT, request_name TEXT, response_type TEXT, error_code TEXT, timestamp TEXT);";
        db.execSQL(sqlTransactionsCreate);

        String sqlAliasCreate = "Create TABLE Alias(contacto_lookup TEXT, contacto_alias TEXT);";
        db.execSQL(sqlAliasCreate);

        String sqlChatCreate = "Create TABLE Chats(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "timestamp TEXT, fromAlias TEXT, toAlias TEXT, origen INTEGER, message TEXT);";
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

    /**
     *
     * @param contacto_id int > 0
     * @param selectionArguments string con la clausula where completa
     */
    public Cursor getEventos(long contacto_id, String selectionArguments) {
        String selection = "contacto_id=?";
        String[] arguments;

        if(selectionArguments != null && !selectionArguments.isEmpty()) {
            selection += " AND " + selectionArguments;
        }
        arguments = new String[]{
                String.valueOf(contacto_id)
        };

        SQLiteDatabase db = getWritableDatabase();
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

    public int deleteEvento(long id) {
        SQLiteDatabase db = getWritableDatabase();
        int outcome = db.delete(
                "Eventos",
                "_id=?",
                new String[]{String.valueOf(id)}
                );
        db.close();
        return outcome;
    }

    public void insertAlias(String contactLookupKey, String contactoAlias) {
        ContentValues nuevoAlias = new ContentValues();
        nuevoAlias.put("contacto_lookup", contactLookupKey);
        nuevoAlias.put("contacto_alias", contactoAlias);

        SQLiteDatabase db = getWritableDatabase();
        db.insert("Alias", null, nuevoAlias);
        db.close();
    }

    public Cursor getAliasByContactLookupKey(String contactLookupKey) {
        SQLiteDatabase db = getWritableDatabase();
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

    public void insertTransaccion(String request_id, String request_name, String response_type, String error_code, String timestamp) {
        ContentValues nuevaTransaccion = new ContentValues();
        nuevaTransaccion.put("request_id", request_id);
        nuevaTransaccion.put("request_name", request_name);
        nuevaTransaccion.put("response_type", response_type);
        nuevaTransaccion.put("error_code", error_code);
        nuevaTransaccion.put("timestamp", timestamp);

        SQLiteDatabase db = getWritableDatabase();
        db.insert("Transacciones", null, nuevaTransaccion);

        db.close();
    }

    public Cursor findAllTransacciones() {
        SQLiteDatabase db = getWritableDatabase();
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

    public long insertChat(String timestamp, String from, String to, int origen, String message) {
        ContentValues nuevaTransaccion = new ContentValues();
        nuevaTransaccion.put("timestamp", timestamp);
        nuevaTransaccion.put("fromAlias", from);
        nuevaTransaccion.put("toAlias", to);
        nuevaTransaccion.put("origen", origen);
        nuevaTransaccion.put("message", message);

        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert("Chats", null, nuevaTransaccion);

        db.close();
        return result;
    }

    public Cursor findChats(String selection, String[] arguments, String orderBy) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(
                "Chats",
                null,
                selection,
                arguments,
                null,
                null,
                orderBy
        );
        return c;
    }

    public int deleteChat(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int outcome = db.delete(
                "Chats",
                "_id=?",
                new String[]{String.valueOf(id)}
        );
        db.close();
        return outcome;
    }
}
