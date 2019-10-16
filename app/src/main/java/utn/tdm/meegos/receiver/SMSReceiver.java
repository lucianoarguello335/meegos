package utn.tdm.meegos.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.Date;

import utn.tdm.meegos.database.MeegosSQLHelper;
import utn.tdm.meegos.domain.Contacto;
import utn.tdm.meegos.domain.Evento;
import utn.tdm.meegos.manager.ContactManager;

public class SMSReceiver extends BroadcastReceiver {

    ContactManager contactManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        contactManager = new ContactManager(context);
        MeegosSQLHelper db = new MeegosSQLHelper(context);

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

                Contacto contacto = contactManager.findContactByPhoneNumber(phoneNumber);

                if(contacto != null){
                    db.insertEvento(
                            Evento.SMS,
                            new Date().getTime(),
                            contacto.getId(),
                            contacto.getLookupKey(),
                            contacto.getNombre(),
                            phoneNumber,
                            Evento.ENTRANTE,
                            message
                    );
                }
            }
        }
    }
}
