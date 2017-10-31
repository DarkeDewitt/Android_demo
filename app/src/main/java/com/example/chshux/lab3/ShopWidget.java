package com.example.chshux.lab3;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class ShopWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.shop_widget);
        views.setTextViewText(R.id.widgetText, widgetText);
        views.setImageViewResource(R.id.widgetImg, R.drawable.shoplist);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.shop_widget);
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            updateViews.setOnClickPendingIntent(R.id.shopWidget, pendingIntent);
            ComponentName componentName = new ComponentName(context, ShopWidget.class);
            appWidgetManager.updateAppWidget(componentName, updateViews);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.appwidget.action.APPWIDGET_UPDATE");
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int index = intent.getIntExtra("index", 0);
        int rid = intent.getIntExtra("rid", R.drawable.circle);
        String itemName = intent.getStringExtra("itemName");
        String itemPrice = intent.getStringExtra("itemPrice");
        if (intent.getAction().equals("com.chshux.lab3.MyStaticFliter")) {
            RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.shop_widget);
            updateViews.setImageViewResource(R.id.widgetImg, rid);
            updateViews.setTextViewText(R.id.widgetText, itemName + "仅售 " + itemPrice);
            Intent newIntent = new Intent(context, DetailsActivity.class);
            newIntent.putExtra("index", index);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            updateViews.setOnClickPendingIntent(R.id.shopWidget, pendingIntent);
            ComponentName componentName = new ComponentName(context, ShopWidget.class);
            appWidgetManager.updateAppWidget(componentName, updateViews);
            context.startService(new Intent(context, widgetService.class));
        }

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        context.stopService(new Intent(context, widgetService.class));
    }
}

