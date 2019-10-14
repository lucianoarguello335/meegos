package utn.tdm.meegos.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Date;

import utn.tdm.meegos.database.MeegosSQLHelper;
import utn.tdm.meegos.domain.Contacto;
import utn.tdm.meegos.domain.Evento;
import utn.tdm.meegos.manager.ContactManager;

public class PhoneReceiver extends BroadcastReceiver {

    ContactManager contactManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        contactManager = new ContactManager(context);
        MeegosSQLHelper db = new MeegosSQLHelper(context);

        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
                Log.d("PhoneReceiver: ", "PHONE_STATE");
                if (extras.getString(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    String phoneNumber = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    if (phoneNumber != null) {
                        Contacto contacto = contactManager.findContactByPhoneNumbre(phoneNumber);
                        if(contacto != null){
                            db.insertEvento(
                                    Evento.LLAMADA,
                                    new Date().getTime(),
                                    contacto.getId(),
                                    contacto.getLookupKey(),
                                    contacto.getNombre(),
                                    phoneNumber,
                                    Evento.ENTRANTE,
                                    ""
                            );
                        }
                    }
                }
            }
        }
    }
}
