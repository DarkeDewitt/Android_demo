package com.example.chshux.lab3;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

public class MainActivity extends AppCompatActivity {

    private String[] itemName ={"Enchated Forest", "Arla Milk", "Devondale Milk", "Kindle Oasis", "Waitrose 早餐麦片", "Mcvitie's 饼干", "Ferrero Rocher", "Maltesers", "Lindt", "Borggreve"};
    private String[] itemPrice ={"¥ 5.00", "¥ 59.00", "¥ 79.00", "¥ 2399.00", "¥ 179.00", "¥ 14.90", "¥ 132.59", "¥ 141.43", "¥ 139.43", "¥ 28.90"};

    private List<Item> itemList = new ArrayList<>();
    private List<TrolleyItem> trolleyItems = new ArrayList<>(Arrays.asList(new TrolleyItem("购物车", "*", "价格", -1)));
    private trolleyItemAdapter trolley_adapter_out = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initItems();

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.shopItemsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        final ItemAdapter adapter = new ItemAdapter(itemList);

        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(adapter);
        animationAdapter.setDuration(1000);
        recyclerView.setAdapter(animationAdapter);
        recyclerView.setItemAnimator(new OvershootInLeftAnimator());
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,new
            RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtra("index", itemList.get(position).getIndex());
                    startActivity(intent);
                }

                @Override
                public void onLongClick(View view, int position) {
                    itemList.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(0, itemList.size() - position);
                    Toast.makeText(MainActivity.this, "移除第" + String.valueOf(position + 1) + "个商品", Toast.LENGTH_SHORT).show();
                }
            }
        ));

        final trolleyItemAdapter trolley_adapter = new trolleyItemAdapter(MainActivity.this, R.layout.trolley_item, trolleyItems);
        trolley_adapter_out = trolley_adapter;
        final ListView listView = (ListView) findViewById(R.id.trolley);
        listView.setAdapter(trolley_adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,View v, int position, long id) {
                if(position != 0) {
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtra("index", trolleyItems.get(position).getIndex());
                    startActivity(intent);
                }
            }
        });
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("移除商品");
        alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                String n = trolleyItems.get(position).getName();
                String[] items = {"从购物车移除" + n + "?"};
                alertDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {;}
                });
                alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        trolleyItems.remove(position);
                        trolley_adapter.notifyDataSetChanged();
                    }
                });
                if(position != 0) {
                    alertDialog.show();
                }
                return true;
            }
        });
        final FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recyclerView.getVisibility() == View.GONE) {
                    recyclerView.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    floatingActionButton.setImageResource(R.drawable.shoplist);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    floatingActionButton.setImageResource(R.drawable.mainpage);
                }
            }
        });
        Random r = new Random();
        int rindex = r.nextInt(10);
        String name = turn(itemName[rindex]);
        Class drawable = R.drawable.class;
        Field field = null;
        int rid = 0;
        try {
            field = drawable.getField(name);
            rid = field.getInt(field.getName());
        } catch (Exception e) {
            rid = R.drawable.circle;
        }
        Intent intent = new Intent("com.chshux.lab3.MyStaticFliter");
        intent.putExtra("index", rindex);
        intent.putExtra("rid", rid);
        intent.putExtra("itemName", itemName[rindex]);
        intent.putExtra("itemPrice", itemPrice[rindex]);
        sendBroadcast(intent);
        EventBus.getDefault().register(this);
    }

    private void initItems() {
        for(int i = 0; i < itemName.length; i++) {
            itemList.add(new Item(itemName[i], String.valueOf(itemName[i].charAt(0)), i));
        }
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onItemEvent(ItemEvent itemEvent) {
        int index = itemEvent.getIndex();
        trolleyItems.add(new TrolleyItem(itemName[index], String.valueOf(itemName[index].charAt(0)), itemPrice[index], index));
        trolley_adapter_out.notifyDataSetChanged();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.shopItemsList);
        ListView listView = (ListView) findViewById(R.id.trolley);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        recyclerView.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        floatingActionButton.setImageResource(R.drawable.mainpage);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

