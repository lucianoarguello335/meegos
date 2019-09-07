package utn.tdm.meegos.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.time.Instant;
import java.util.Date;

import utn.tdm.meegos.database.EventsSQLiteHelper;

public class MeegosReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        EventsSQLiteHelper db = new EventsSQLiteHelper(
                context,
                EventsSQLiteHelper.DB_NAME,
                null,
                EventsSQLiteHelper.CURRENT_DB_VERSION);

        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Log.d("MeegosReceiver: ", "SMS_RECEIVED");
                String mensaje = "";
                String phoneNumber = "";
                int contactoId;
                String contactoNombre = "";

                String format = extras.getString("format"); //3gpp
                Object[] msgPDU = (Object[]) extras.get("pdus");
                final SmsMessage[] message = new SmsMessage[msgPDU.length];
                for (int i = 0; i < msgPDU.length; i++) {
                    message[i] = SmsMessage.createFromPdu((byte[])msgPDU[i], format);
                    mensaje = message[i].getMessageBody();
                    phoneNumber = message[i].getOriginatingAddress();
                }

                // TODO: Registrar mensaje
                db.insertEvento(
                        1,
                        new Date().getTime(),
                        1212,
                        "pepe",
                        1,
                        ""
                );
            } else if (false) {

            } else if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
                Log.d("MeegosReceiver: ", "PHONE_STATE");
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
