package com.example.mapTest;

/**
 * Created by tk1114632 on 11/3/14.
 */
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.VersionInfo;

import java.util.ArrayList;

public class main extends Activity {
    //数据库ContactDB
    DBManager ContactDBManager;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ContactDBManager = new DBManager(this);

        Button buttonMap = (Button)this.findViewById(R.id.buttonMap);
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(main.this , mapView.class);
                startActivity(i);
            }
        });
    }

    public void addOne(View view){
        ArrayList<ContactInfo> list = new ArrayList<ContactInfo>();
        ContactInfo temp = new ContactInfo("宋明亨","上海交大","上海市闵行区东川路800号",31.030579,121.435007,"18916924886");
        list.add(temp);
        temp = new ContactInfo("Vincent","Logic Solutions", "上海市浦东新区博霞路50号",31.205939,121.609884,"13549998877");
        list.add(temp);

        ContactDBManager.add_person(list);


    }

    public void addTwo(View view){
        ArrayList<ContactInfo> list = new ArrayList<ContactInfo>();
        ContactInfo temp = new ContactInfo("人民广场", "人民广场","上海市人民广场",31.238802,121.481033,"110");
        list.add(temp);
        temp = new ContactInfo("孟汀阳","上海交通大学", "上海市陆家嘴",31.242559,121.510786,"+86 131342");
        list.add(temp);
        ContactDBManager.add_person(list);
    }

    public void deleteAll(View view){
        ContactDBManager.deleteOldPerson(new ContactInfo("人民广场", "人民广场","上海市人民广场",31.238802,121.481033,"110"));
        ContactDBManager.deleteOldPerson(new ContactInfo("孟汀阳","上海交通大学", "上海市陆家嘴",31.242559,121.510786,"+86 131342"));
        ContactDBManager.deleteOldPerson(new ContactInfo("宋明亨","上海交大","上海市闵行区东川路800号",31.030579,121.435007,"18916924886"));
        ContactDBManager.deleteOldPerson(new ContactInfo("Vincent","Logic Solutions", "上海市浦东新区博霞路50号",31.205939,121.609884,"13549998877"));
    }

    protected void OnDestory() {
        super.onDestroy();
        ContactDBManager.closeDB();
    }



}
