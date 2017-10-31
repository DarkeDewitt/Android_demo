package com.example.chshux.lab3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by chshux on 2017/10/23.
 */

public class trolleyItemAdapter extends ArrayAdapter<TrolleyItem>{
    private int resourceId;

    public trolleyItemAdapter(Context context, int textViewResourceId, List<TrolleyItem> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TrolleyItem trolleyItem = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView name = (TextView) view.findViewById(R.id.trolley_name);
        TextView firstLetter = (TextView) view.findViewById(R.id.trolley_firstLetter);
        TextView price = (TextView) view.findViewById(R.id.price);
        name.setText(trolleyItem.getName());
        firstLetter.setText(trolleyItem.getFirstLetter());
        price.setText(trolleyItem.getPrice());
        return view;
    }
}
