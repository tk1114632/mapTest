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
public class companyListAdapter extends BaseAdapter {
    Context context;
    ArrayList<CompanyInfo> list;
    private LayoutInflater inflater;
    public companyListAdapter(Context context, ArrayList list){
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CompanyInfo getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.company_item_list, null);
        TextView name = (TextView)convertView.findViewById(R.id.company_item_name);
        name.setText(list.get(position).getName());
        name.setTextColor(Color.BLACK);
        if (position%2 == 1) {
            convertView.setBackgroundColor(Color.WHITE);
        }
        return convertView;
    }


}
