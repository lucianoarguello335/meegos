package utn.tdm.meegos.manager;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Comparator;

import utn.tdm.meegos.database.EventsSQLiteHelper;
import utn.tdm.meegos.domain.Chat;
import utn.tdm.meegos.preferences.MeegosPreferences;

public class ChatManager {

    private Context context;
    EventsSQLiteHelper eventsSQLiteHelper;

    public ChatManager(Context context) {
        this.context = context;
        this.eventsSQLiteHelper = new EventsSQLiteHelper(context, EventsSQLiteHelper.DB_NAME, null, EventsSQLiteHelper.CURRENT_DB_VERSION);
    }

    public void saveChat(long timestamp, String from, String to, String message) {
        eventsSQLiteHelper.insertChat(timestamp, to, from, message);
    }

    public ArrayList<Chat> findChatsByAlias(String alias) {
        ArrayList<Chat> chats = new ArrayList<>();
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
        Cursor cursor = eventsSQLiteHelper.findChatsByAlias("to LIKE '" + alias + "' OR from LIKE '" + alias + "'");
        while (cursor.moveToNext()) {
            chats.add(
                new Chat(
                    cursor.getLong(0),
                    cursor.getLong(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
                )
            );
        }
        cursor.close();

        // Ordenamos el resultado
        //TODO: Setear el ordenamiento
        chats.sort(new Comparator<Chat>() {
            @Override
            public int compare(Chat c1, Chat c2) {
                if (MeegosPreferences.getHistoryOrder(context).equals("fecha ASC")) {
                    return Long.compare(c2.getTimestamp(), c1.getTimestamp());
                } else {
                    return Long.compare(c1.getTimestamp(), c2.getTimestamp());
                }
            }
        });

        return chats;
    }
}
