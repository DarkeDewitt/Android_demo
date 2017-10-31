package com.example.chshux.lab3;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by chshux on 2017/10/23.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private List<Item> mItem;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemFirst;
        TextView itemName;
        View itemView;

        public ViewHolder(View view) {
            super(view);
            itemView = view;
            itemFirst = (TextView) view.findViewById(R.id.firstLetter);
            itemName = (TextView) view.findViewById(R.id.name);
        }
    }

    public ItemAdapter(List<Item> ItemList) {
        mItem = ItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = mItem.get(position);
        holder.itemFirst.setText(item.getFirst());
        holder.itemName.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return mItem.size();
    }
}