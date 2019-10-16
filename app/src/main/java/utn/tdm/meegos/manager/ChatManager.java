package utn.tdm.meegos.manager;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Comparator;

import utn.tdm.meegos.database.MeegosSQLHelper;
import utn.tdm.meegos.domain.Chat;
import utn.tdm.meegos.preferences.MeegosPreferences;

public class ChatManager {

    private Context context;
    MeegosSQLHelper meegosSQLHelper;

    public ChatManager(Context context) {
        this.context = context;
        this.meegosSQLHelper = new MeegosSQLHelper(context);
    }

    public void saveChat(Chat chat) {
        meegosSQLHelper.insertChat(
                chat.getTimestamp(),
                chat.getFrom(),
                chat.getTo(),
                chat.getOrigen(),
                chat.getMessage()
        );
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
        Cursor cursor = meegosSQLHelper.findChatsByAlias(alias);
        while (cursor.moveToNext()) {
            chats.add(
                new Chat(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(4),
                    cursor.getString(5)
                )
            );
        }
        cursor.close();

        // Ordenamos el resultado
        //TODO: Setear el ordenamiento
        chats.sort(new Comparator<Chat>() {
            @Override
            public int compare(Chat c1, Chat c2) {
                    return c2.getTimestamp().compareTo(c1.getTimestamp());
//                if (MeegosPreferences.getHistoryOrder(context).equals("fecha ASC")) {
//                    return c2.getTimestamp().compareTo(c1.getTimestamp());
//                } else {
//                    return c1.getTimestamp().compareTo(c2.getTimestamp());
//                }
            }
        });

        return chats;
    }
}
