package utn.tdm.meegos.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Vector;
import java.util.function.Consumer;

import utn.tdm.meegos.manager.ChatManager;
import utn.tdm.meegos.task.ServerTask;
import utn.tdm.meegos.preferences.MeegosPreferences;
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
//                        TODO: Actualizamos el Timestamp para la proxima busqueda
                         MeegosPreferences.setTimestamp(getApplicationContext(), Long.parseLong(responseXMLDataBlock.getAttribute("id")));

//                         Buscamos la lista de mensajes
                        Vector<XMLDataBlock> messagesListBlock = responseXMLDataBlock.getChildBlock("messages-list").getChildBlocks("message");

//                        Si hay mensajes
                        if (messagesListBlock.size() > 0) {
                            messagesListBlock.forEach(new Consumer<XMLDataBlock>() {
                                @Override
                                public void accept(XMLDataBlock xmlDataBlock) {
                                    String timestamp = xmlDataBlock.getAttribute("timestamp"),
                                            from = xmlDataBlock.getAttribute("from"),
                                            message = xmlDataBlock.getText();
                                    new ChatManager(getApplicationContext()).saveChat(
                                        Long.parseLong(timestamp),
                                        from,
                                        MeegosPreferences.getUsername(getApplicationContext()),
                                        message
                                    );
                                    Toast.makeText(getApplicationContext(), "LLEGO EL SERVICE", Toast.LENGTH_LONG).show();
                                }
                            });
                            // TODO: Notificar mensajes nuevos
                        }
                    }
                }
            });
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
