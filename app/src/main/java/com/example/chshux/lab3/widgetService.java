package com.example.chshux.lab3;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.RemoteViews;

public class widgetService extends Service {
    private DynamicRecevier dynamicRecevier = new DynamicRecevier();
    public widgetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.chshux.lab3.MyDynamicFliter");
        registerReceiver(dynamicRecevier, intentFilter);
    }
    public class DynamicRecevier extends BroadcastReceiver {
        private static final String DYNAMICACTION = "com.chshux.lab3.MyDynamicFliter";

        public void onReceive(Context context, Intent intent) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            if (intent.getAction().equals(DYNAMICACTION)) {
                int rid = intent.getIntExtra("rid", R.drawable.circle);
                String itemName = intent.getStringExtra("itemName");
                RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.shop_widget);
                updateViews.setImageViewResource(R.id.widgetImg, rid);
                updateViews.setTextViewText(R.id.widgetText, itemName + "已添加到购物车");
                Intent newIntent = new Intent(context, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                updateViews.setOnClickPendingIntent(R.id.shopWidget, pendingIntent);
                ComponentName componentName = new ComponentName(context, ShopWidget.class);
                appWidgetManager.updateAppWidget(componentName, updateViews);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(dynamicRecevier);
    }
}
