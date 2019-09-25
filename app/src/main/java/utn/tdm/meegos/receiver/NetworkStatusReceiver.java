package utn.tdm.meegos.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import utn.tdm.meegos.R;
import utn.tdm.meegos.preferences.MeegosPreferences;

public class NetworkStatusReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        Bundle extras = intent.getExtras();
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected){
            MeegosPreferences.setNetworkStatus(context, "1");
        } else {
            MeegosPreferences.setNetworkStatus(context, "0");
            // TODO: ANDRES - cambiar el icono del chat.
        }
    }
}
