package utn.tdm.meegos.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import utn.tdm.meegos.database.EventsSQLiteHelper;

public class PhoneReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        EventsSQLiteHelper db = new EventsSQLiteHelper(
                context,
                EventsSQLiteHelper.DB_NAME,
                null,
                EventsSQLiteHelper.CURRENT_DB_VERSION);

        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
                Log.d("PhoneReceiver: ", "PHONE_STATE");
                if (extras.getString(TelephonyManager.EXTRA_STATE)
                        .equals(TelephonyManager.EXTRA_STATE_RINGING)) {

                    String phoneNumber = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    if (phoneNumber != null) {
                        // TODO: Registrar llamado
                    }
                }
            }
        }
    }
}
