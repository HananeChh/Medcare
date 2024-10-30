package com.moncefadj.medcare.Medicaments;



import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;

import com.moncefadj.medcare.R;

import java.util.Random;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotification(context , " Temps de votre médicament", "Vous avez un médicament à prendre ... cliquer ici ");
    }
    public void createNotification(Context context, String title, String msgText)
    {
        Intent i = new Intent(context , liste_medicaments.class);
        Random r = new Random();

        PendingIntent notificIntent = PendingIntent.getActivity(context,
                r.nextInt(1000), i ,PendingIntent.FLAG_CANCEL_CURRENT);


        NotificationCompat.Builder mBuilder = new
                NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(msgText)
                .setSmallIcon(R.drawable.ic_baseline_medical_services_24)
                .setLargeIcon(BitmapFactory.decodeResource( context.getResources(), R.drawable.ic_baseline_medical_services_24));


        mBuilder.setContentIntent(notificIntent);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_VIBRATE);
        mBuilder.setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NotificationID.getID(), mBuilder.build());

    }

}