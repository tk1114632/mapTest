package com.example.mapTest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tk1114632 on 11/15/14.
 */
public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public void add_person(ArrayList<ContactInfo> personList){
        db.beginTransaction();
        try {
            for (ContactInfo person : personList) {
                //Log.e("=============!!!!!!!!!!!!!!!!", "EEca");
                db.execSQL("INSERT INTO Person(name,company,addr,position_lat,position_lng,tel) VALUES(?, ?, ?, ?, ?, ?)", new Object[]{person.getName()
                                    , person.getCompany(), person.getAddress(), person.getPositionLat()
                                    , person.getPositionLng(), person.getTel()});
                Cursor c = db.rawQuery("SELECT _id FROM Person WHERE name=?", new String[]{person.getName()});
                person.setDb_id(c.getColumnIndex("_id"));
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            Log.e("Person added completion!!!!","===============================");
        }
    }

    public void updateName (ContactInfo person){
        ContentValues cv = new ContentValues();
        cv.put("name", person.getName());
        db.update("Person", cv, "name = ?", new String[]{person.getName()});
    }

    public void updateCompany (ContactInfo person){
        ContentValues cv = new ContentValues();
        cv.put("company", person.getName());
        db.update("Person", cv, "company = ?", new String[]{person.getCompany()});
    }

    public void updateAddress (ContactInfo person){
        ContentValues cv = new ContentValues();
        cv.put("address", person.getName());
        db.update("Person", cv, "addr = ?", new String[]{person.getAddress()});
    }

    public void updateTel (ContactInfo person){
        ContentValues cv = new ContentValues();
        cv.put("tel", person.getName());
        db.update("Person", cv, "tel=?", new String[]{person.getTel()});
    }

    public void deleteOldPerson(ContactInfo person){
        db.delete("Person", "name=?", new String[]{person.getName()});
    }

    public void deleteAll() {
        db.execSQL("DELETE FROM Person");
    }

    public ArrayList<ContactInfo> queryAll() {
        ArrayList<ContactInfo> currentList = new ArrayList<ContactInfo>();
        Cursor c = db.rawQuery("SELECT * FROM Person", null);
        if (c != null) {
            while (c.moveToNext()) {
                ContactInfo newPerson =  new ContactInfo();
                newPerson.setName(c.getString((c.getColumnIndex("name"))));
                newPerson.setCompany(c.getString((c.getColumnIndex("company"))));
                newPerson.setAddress(c.getString((c.getColumnIndex("addr"))));
                newPerson.setPositionLat(c.getDouble((c.getColumnIndex("position_lat"))));
                newPerson.setPositionLng(c.getDouble((c.getColumnIndex("position_lng"))));
                newPerson.setTel(c.getString((c.getColumnIndex("tel"))));
                currentList.add(newPerson);
            }
        }

        c.close();
        return currentList;
    }

    public ArrayList<ContactInfo> queryName(String key_name) {
        ArrayList<ContactInfo> currentList = new ArrayList<ContactInfo>();
        Cursor c = db.rawQuery("SELECT * FROM Person WHERE name =?", new String[]{key_name});
        if (c != null) {
            while (c.moveToNext()) {
                ContactInfo newPerson =  new ContactInfo();
                newPerson.setName(c.getString((c.getColumnIndex("name"))));
                newPerson.setCompany(c.getString((c.getColumnIndex("company"))));
                newPerson.setAddress(c.getString((c.getColumnIndex("addr"))));
                newPerson.setPositionLat(c.getDouble((c.getColumnIndex("position_lat"))));
                newPerson.setPositionLng(c.getDouble((c.getColumnIndex("position_lng"))));
                newPerson.setTel(c.getString((c.getColumnIndex("tel"))));
                currentList.add(newPerson);
            }
        }
        c.close();
        return currentList;
    }

    public ArrayList<ContactInfo> queryCompany(String key_company) {
        ArrayList<ContactInfo> currentList = new ArrayList<ContactInfo>();
        Cursor c = db.rawQuery("SELECT * FROM Person WHERE company =?", new String[]{key_company});
        if (c != null) {
            while (c.moveToNext()) {
                ContactInfo newPerson =  new ContactInfo();
                newPerson.setName(c.getString((c.getColumnIndex("name"))));
                newPerson.setCompany(c.getString((c.getColumnIndex("company"))));
                newPerson.setAddress(c.getString((c.getColumnIndex("addr"))));
                newPerson.setPositionLat(c.getDouble((c.getColumnIndex("position_lat"))));
                newPerson.setPositionLng(c.getDouble((c.getColumnIndex("position_lng"))));
                newPerson.setTel(c.getString((c.getColumnIndex("tel"))));
                currentList.add(newPerson);
            }
        }
        c.close();
        return currentList;
    }

    //close database
    public void closeDB() {
        db.close();
    }
}
