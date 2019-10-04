package utn.tdm.meegos.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;

import androidx.core.app.NotificationCompat;

import utn.tdm.meegos.R;
import utn.tdm.meegos.activity.ContactActivity;

public class MeegosNotifications {

    public static void notifyMsjReceived(Context context){

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context,"")
                        .setSmallIcon(android.R.drawable.stat_sys_warning)
                        .setLargeIcon((( (BitmapDrawable) context.getResources().getDrawable(R.drawable.ic_launcher_background)).getBitmap()))
                        .setContentTitle("Tiene nuevos mensajes")
                        .setContentText("El mensaje fue enviado con exito")
                        //.setContentInfo("4")
                        .setTicker("Envio");

        Intent notIntent = new Intent(ContactActivity.this, ContactActivity.class);
        PendingIntent contIntent = PendingIntent.getActivity(ContactActivity.this, 0, notIntent, 0);
        mBuilder.setContentIntent(contIntent);
    }
}