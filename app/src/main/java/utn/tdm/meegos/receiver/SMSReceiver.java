package utn.tdm.meegos.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.Date;

import utn.tdm.meegos.database.EventsSQLiteHelper;

public class SMSReceiver extends BroadcastReceiver {

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
                Log.d("SMSReceiver: ", "SMS_RECEIVED");
                String message = "";
                String phoneNumber = "";
                int contactId;
                String contactName = "";

                String format = extras.getString("format"); //3gpp
                Object[] msgPDU = (Object[]) extras.get("pdus");
                final SmsMessage[] pduMessage = new SmsMessage[msgPDU.length];
                for (int i = 0; i < msgPDU.length; i++) {
                    pduMessage[i] = SmsMessage.createFromPdu((byte[]) msgPDU[i], format);
                    message = pduMessage[i].getMessageBody();
                    phoneNumber = pduMessage[i].getOriginatingAddress();
                }

                // TODO: Registrar message
                db.insertEvento(
                        1,
                        new Date().getTime(),
                        1212,
                        "pepe",
                        1,
                        ""
                );
            }
        }
    }
}
