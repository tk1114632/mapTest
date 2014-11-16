package com.example.mapTest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by tk1114632 on 11/15/14.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "text.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        //CuresorFacory 设置null
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //数据库第一次调用时候
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Person" +
                "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR(10), " +
                "company VARCHAR(50), " +
                "addr VARCHAR(100), " +
                "position_lat DOUBLE, " +
                "position_lng DOUBLE, " +
                "tel VARCHAR(20)" +
                ")");
        Log.e("数据库表建立完毕","DONE!!!!!");
    }

    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE Person ADD COLUMN other STRING");
    }
}