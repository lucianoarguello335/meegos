package utn.tdm.meegos.manager;

import android.os.AsyncTask;

import java.util.Hashtable;

import utn.tdm.meegos.Task.ServerTask;
import utn.tdm.meegos.util.XMLDataBlock;

public class ChatManager {
    private ServerTask userRegisterTask;

    public void registerUser(String username, String password){
        XMLDataBlock requestBodyBlock = new XMLDataBlock("action",null, null);
        XMLDataBlock actionDetailBlock = new XMLDataBlock("action-detail",requestBodyBlock, null);
        Hashtable<String, String> user = new Hashtable<>();
        user.put("username", username);
        user.put("password", password);
        XMLDataBlock userBlock = new XMLDataBlock(
                "user",
                actionDetailBlock,
                user
        );
//        new ServerTask(null).execute(requestBodyBlock);

        int a = 0;
    }
}
