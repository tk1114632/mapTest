package com.example.mapTest;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by tk1114632 on 11/19/14.
 */
public class popListAdapter extends BaseAdapter {
    Context context;
    ArrayList<ContactInfo> list;
    private LayoutInflater inflater;
    public popListAdapter(Context context,ArrayList list){
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ContactInfo getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView==null){
            holder = new Holder();
            convertView = inflater.inflate(R.layout.item_list, null);
            holder.name = (TextView) convertView.findViewById(R.id.item_name);
            holder.company = (TextView) convertView.findViewById(R.id.item_company);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        holder.name.setText(list.get(position).getName());
        holder.company.setText(list.get(position).getCompany());
        return convertView;
    }

    protected class Holder{
        TextView name;
        TextView company;
    }
}
