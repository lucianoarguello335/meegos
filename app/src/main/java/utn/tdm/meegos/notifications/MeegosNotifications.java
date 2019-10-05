package utn.tdm.meegos.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import androidx.core.app.NotificationCompat;

import utn.tdm.meegos.R;
import utn.tdm.meegos.activity.ContactActivity;

/*
* https://github.com/lucianoarguello335/meegos/issues?q=is%3Aissue+is%3Aopen+label%3A%22Notification+Manager%22
*
* La aplicación debe notificar el resultado de envío de los mensajes utilizando el servicio web. #7
* La aplicación debe mostrar mensajes de error cuando no pudo ser enviado un mensaje a través del servicio web. #8
*
/
 */

public class MeegosNotifications {



    public static void messageResultNotification(Context context, boolean isSuccesful){

        //TODO: Obtener textos de "strings.xml"

        int smallIcon;
        Bitmap largeIcon;
        String contentTitle;
        String contentText;
        String ticker;

        if(!isSuccesful){
            // If message send failed, fill notification with failure icons and messages
            smallIcon = R.drawable.baseline_sms_failed_black_18;
            largeIcon = ((BitmapDrawable) context.getResources().getDrawable(R.drawable.baseline_sms_failed_black_36)).getBitmap();
            contentTitle = context.getResources().getString(R.string.notification_message_result_title_error);
            contentText = context.getResources().getString(R.string.notification_message_result_text_error);
            ticker = context.getResources().getString(R.string.notification_message_result_ticker_error);
        }

        // If message send succeeded, fill notification with success icons and messages
        else{
            smallIcon = R.drawable.baseline_done_black_18;
            largeIcon = ((BitmapDrawable) context.getResources().getDrawable(R.drawable.baseline_done_black_36)).getBitmap();
            contentTitle = context.getResources().getString(R.string.notification_message_result_title_success);
            contentText = context.getResources().getString(R.string.notification_message_result_title_success);
            ticker = context.getResources().getString(R.string.notification_message_result_title_success);
        }

        // build notification using the outcome (success or fail)
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context,"")
                        .setSmallIcon(smallIcon)
                        .setLargeIcon(largeIcon)
                        .setContentTitle(contentTitle)
                        .setContentText(contentText)
                        //.setContentInfo("4")
                        .setTicker(ticker);

        mBuilder.notify();
        /*
        Intent notIntent = new Intent(context, ContactActivity.class);
        PendingIntent contIntent = PendingIntent.getActivity(ContactActivity.this, 0, notIntent, 0);
        mBuilder.setContentIntent(contIntent);
         */
    }

    public static void messageReceived(Context context, CharSequence messageText){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,"")
                .setSmallIcon(R.drawable.baseline_sms_black_18)
                .setLargeIcon(((BitmapDrawable) context.getResources().getDrawable(R.drawable.baseline_sms_black_36)).getBitmap())
                .setContentTitle(context.getResources().getString(R.string.notification_message_received_title))
                .setContentText(messageText)
                .setTicker(context.getResources().getText(R.string.notification_message_received_ticker));
        mBuilder.notify();
    }

    public static void userRegistered(Context context){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,"")
                .setSmallIcon(R.drawable.baseline_account_box_black_18)
                .setLargeIcon(((BitmapDrawable) context.getResources().getDrawable(R.drawable.baseline_account_box_black_36)).getBitmap())
                .setContentTitle(context.getResources().getString(R.string.notification_user_registered_title))
                .setContentText(context.getResources().getString(R.string.notification_user_registered_text))
                .setTicker(context.getResources().getString(R.string.notification_user_registered_ticker));
        mBuilder.notify();
    }

}