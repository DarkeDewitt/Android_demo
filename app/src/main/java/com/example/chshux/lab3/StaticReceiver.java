package com.example.chshux.lab3;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

/**
 * Created by chshux on 2017/10/30.
 */

public class StaticReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("com.chshux.lab3.MyStaticFliter")) {
            int index = intent.getIntExtra("index", 0);
            int rid = intent.getIntExtra("rid", R.drawable.circle);
            String itemName = intent.getStringExtra("itemName");
            String itemPrice = intent.getStringExtra("itemPrice");

            RemoteViews remoteViews = new RemoteViews("com.example.chshux.lab3", R.layout.mynotification);
            remoteViews.setTextViewText(R.id.nf_title, "新品特卖");
            remoteViews.setTextViewText(R.id.nf_text, itemName + "仅售"+itemPrice);
            remoteViews.setImageViewResource(R.id.nf_img, rid);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setContent(remoteViews)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.circle);
            Intent resultInent = new Intent(context, DetailsActivity.class);
            resultInent.putExtra("index", index);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultInent, PendingIntent.FLAG_CANCEL_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, mBuilder.build());
        }
    }
}
