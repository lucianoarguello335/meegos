package utn.tdm.meegos.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Date;
import java.util.Vector;
import java.util.function.Consumer;

import utn.tdm.meegos.domain.Chat;
import utn.tdm.meegos.manager.ChatManager;
import utn.tdm.meegos.notifications.MeegosNotifications;
import utn.tdm.meegos.task.ServerTask;
import utn.tdm.meegos.preferences.MeegosPreferences;
import utn.tdm.meegos.util.Constants;
import utn.tdm.meegos.util.DateUtil;
import utn.tdm.meegos.util.XMLDataBlock;
import utn.tdm.meegos.util.XMLUtil;

public class MessageService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!MeegosPreferences.getUsername(this).isEmpty()) {
            XMLDataBlock requestBodyBlock = XMLUtil.getDataBlockGetMessages(this);
            new ServerTask(this, null, new ServerTask.ServerListener() {
                @Override
                public void toDoOnSuccessPostExecute(XMLDataBlock responseXMLDataBlock) {
                    if (responseXMLDataBlock.getAttribute("type").equals("success")) {
//                        Actualizamos el Timestamp para la proxima busqueda
                         MeegosPreferences.setTimestamp(getApplicationContext(), Long.parseLong(responseXMLDataBlock.getAttribute("id")));

//                         Buscamos la lista de mensajes
                        Vector<XMLDataBlock> messagesListBlock = responseXMLDataBlock.getChildBlock("messages-list").getChildBlocks("message");

//                        Si hay mensajes
                        if (messagesListBlock != null && messagesListBlock.size() > 0) {
                            final StringBuilder receivedMessages = new StringBuilder("");
                            messagesListBlock.forEach(new Consumer<XMLDataBlock>() {
                                @Override
                                public void accept(XMLDataBlock xmlDataBlock) {
                                    Chat chat = new Chat(
                                            xmlDataBlock.getAttribute("timestamp"),
                                            xmlDataBlock.getAttribute("from"),
                                            MeegosPreferences.getUsername(getApplicationContext()),
                                            Chat.RECIBIDO,
                                            xmlDataBlock.getText()
                                    );
                                    new ChatManager(getApplicationContext()).saveChat(chat);
                                    receivedMessages.append(chat.getFrom()).append(": ").append(chat.getMessage()).append("\n");
                                    MeegosNotifications.messageReceived(
                                            getApplicationContext(),
                                            receivedMessages,
                                            new StringBuilder("informacion")
                                    );
                                }
                            });
                        }
                    }
                }
            }).execute(requestBodyBlock);
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
