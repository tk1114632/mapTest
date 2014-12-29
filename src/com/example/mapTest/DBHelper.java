package com.example.mapTest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by tk1114632 on 11/15/14.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "text1.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        //CuresorFacory 设置null
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //数据库第一次调用时候
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Person" +
                "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR(10), " +
                "company VARCHAR(50), " +
                "addr VARCHAR(100), " +
                "position_lat DOUBLE, " +
                "position_lng DOUBLE, " +
                "tel VARCHAR(20)" +
                ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS Company" +
                "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR(25), " +
                "address VARCHAR(50), " +
                "phone VARCHAR(30), " +
                "website VARCHAR(50), " +
                "field VARCHAR(20), " +
                "billing_address_id INTEGER, " +
                "contact_id INTEGER, " +
                "event_id INTEGER, " +
                "industry_id INTEGER, " +
                "sale_id INTEGER, " +
                "shipping_address_id INTEGER," +
                "position_lat DOUBLE, " +
                "position_lng DOUBLE " +
                ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS Contact" +
                "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email_personal VARCHAR(25), " +
                "email_work VARCHAR(25), " +
                "firstname VARCHAR(15), " +
                "lastname VARCHAR(15)," +
                "phone_home VARCHAR(15)," +
                "phone_mobile VARCHAR(20)," +
                "phone_work VARCHAR(15)," +
                "note VARCHAR(10)," +
                "qq VARCHAR(13)," +
                "skype VARCHAR(15)," +
                "title VARCHAR(10)," +
                "wechat VARCHAR(15)," +
                "weibo VARCHAR(30)," +
                "address VARCHAR(50), " +
                "duty VARCHAR(10), " +
                "address_id INT," +
                "company_id INT,"+
                "event_id INT,"+
                "project_id INT," +
                "sale_id INT,"+
                "position_lat DOUBLE, " +
                "position_lng DOUBLE " +
                ")" );
        db.execSQL("CREATE TABLE IF NOT EXISTS AddressInfo" +
                   "( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "company_id INT," +
                        "company_billing_address VARCHAR(50)," +
                        "company_shipping_address VARCHAR(50)" +
                    ")");
        Log.e("数据库表建立完毕","DONE!!!!!");
    }

    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE Company ADD COLUMN position_lat DOUBLE");
        db.execSQL("ALTER TABLE Company ADD COLUMN position_lng DOUBLE");
    }
}
