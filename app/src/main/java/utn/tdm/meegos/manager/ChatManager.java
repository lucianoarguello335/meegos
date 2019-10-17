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
        String[] arguments = new String[]{};

//        Filtro por las preferencias
        if (MeegosPreferences.isChatSentFiltered(context)) {
            selection = "toAlias = ?";
            arguments = new String[]{alias};
            if (MeegosPreferences.isChatReceivedFiltered(context)){
                selection += " OR fromAlias = ?";
                arguments = new String[]{alias, alias};
            }
        } else if (MeegosPreferences.isChatReceivedFiltered(context)) {
            selection = "fromAlias = ?";
            arguments = new String[]{alias};
        }

        if (!selection.isEmpty()) {
            Cursor cursor = meegosSQLHelper.findChats(
                    selection,
                    arguments,
                    MeegosPreferences.getChatOrder(context)
            );
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
        }
        return chats;
    }

    public int deleteChat(Chat chat) {
        return meegosSQLHelper.deleteChat(chat.getId());
    }
}
