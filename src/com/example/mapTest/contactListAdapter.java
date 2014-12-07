package com.example.mapTest;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by tk1114632 on 11/19/14.
 */
public class contactListAdapter extends BaseAdapter {
    Context context;
    ArrayList<ContactInfo_new> list;
    private LayoutInflater inflater;
    public contactListAdapter(Context context, ArrayList list){
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ContactInfo_new getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.contact_item_list, null);
        TextView name = (TextView)convertView.findViewById(R.id.contact_item_name);
        name.setText(list.get(position).getLastname()+list.get(position).getFirstname());
        name.setTextColor(Color.BLACK);
        if (position%2 == 1) {
            convertView.setBackgroundColor(Color.WHITE);
        }

        convertView.setMinimumHeight(20);
        return convertView;
    }


}
