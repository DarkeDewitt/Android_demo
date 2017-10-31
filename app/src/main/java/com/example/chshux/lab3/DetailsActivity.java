package com.example.chshux.lab3;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;

public class DetailsActivity extends AppCompatActivity {
    private String[] itemName ={"Enchated Forest", "Arla Milk", "Devondale Milk", "Kindle Oasis", "Waitrose 早餐麦片", "Mcvitie's 饼干", "Ferrero Rocher", "Maltesers", "Lindt", "Borggreve"};
    private String[] itemPrice ={"¥ 5.00", "¥ 59.00", "¥ 79.00", "¥ 2399.00", "¥ 179.00", "¥ 14.90", "¥ 132.59", "¥ 141.43", "¥ 139.43", "¥ 28.90"};
    private String[] other = {"作者    Johanna Basford", "产地    德国", "产地    澳大利亚", "版本    8GB", "重量    2Kg", "产地    英国", "重量    300g", "重量    118g", "重量    249g", "重量    640g"};
    private int[] star_id = {R.drawable.empty_star, R.drawable.full_star};
    private DynamicRecevier dynamicRecevier = new DynamicRecevier();
    int index_return = 0;
    public static void actionStart(Context context, int index) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("index", index);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);
        Intent intent = getIntent();
        final int index = intent.getIntExtra("index", 0);
        index_return = index;
        ImageView imageView = (ImageView) findViewById(R.id.item_image);
        String name = turn(itemName[index]);

        Class drawable = R.drawable.class;
        Field field = null;
        int rid = 0;
        try {
            field = drawable.getField(name);
            rid = field.getInt(field.getName());
        } catch (Exception e) {
            rid = R.drawable.circle;
        }
        imageView.setImageResource(rid);
        TextView detail_name = (TextView) findViewById(R.id.item_detail_name);
        detail_name.setText(itemName[index]);

        TextView detail_price = (TextView) findViewById(R.id.item_detail_price);
        detail_price.setText(itemPrice[index]);

        TextView detail_other = (TextView) findViewById(R.id.item_detail_other);
        detail_other.setText(other[index]);
        final ImageButton buy = (ImageButton) findViewById(R.id.buy);
        buy.setTag(0);
        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                finish();
            }
        });
        final ImageButton star = (ImageButton) findViewById(R.id.star);
        star.setTag(1);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (Integer)star.getTag();
                star.setImageResource(star_id[tag % 2]);
                star.setTag((tag + 1) % 2);
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.chshux.lab3.MyDynamicFliter");
        registerReceiver(dynamicRecevier, intentFilter);
        final Intent intent1 = new Intent("com.chshux.lab3.MyDynamicFliter");
        intent1.putExtra("rid", rid);
        intent1.putExtra("itemName", itemName[index]);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailsActivity.this, "商品已添加到购物车", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new ItemEvent(index));
                sendBroadcast(intent1);
            }
        });

    }

    private String turn(String s) {
        int i = 0;
        for(i = 0; i < s.length(); i++) {
            if(!Character.isLetter(s.charAt(i))) {
                break;
            }
        }
        return s.substring(0, i).toLowerCase();
    }
    public class DynamicRecevier extends BroadcastReceiver {
        private static final String DYNAMICACTION = "com.chshux.lab3.MyDynamicFliter";
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DYNAMICACTION)) {
                int rid = intent.getIntExtra("rid", R.drawable.circle);
                String itemName = intent.getStringExtra("itemName");
                RemoteViews remoteViews = new RemoteViews("com.example.chshux.lab3", R.layout.mynotification);
                remoteViews.setTextViewText(R.id.nf_title, "马上下单");
                remoteViews.setTextViewText(R.id.nf_text, itemName + "已添加到购物车");
                remoteViews.setImageViewResource(R.id.nf_img, rid);
                final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                        .setContent(remoteViews)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.circle);
                Intent resultInent = new Intent(context, MainActivity.class);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultInent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, mBuilder.build());
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(dynamicRecevier);
    }
}
