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
                db.execSQL("INSERT INTO Person(name,company,addr,position_lat,position_lng,tel) VALUES(?, ?, ?, ?, ?, ?)", new Object[]{person.getName()
                                    , person.getCompany(), person.getAddress(), person.getPositionLat()
                                    , person.getPositionLng(), person.getTel()});
                //Cursor c = db.rawQuery("SELECT _id FROM Person WHERE name=?", new String[]{person.getName()});
                //person.setDb_id(c.getColumnIndex("_id"));
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
                newPerson.setDb_id(c.getInt((c.getColumnIndex("_id"))));
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

    //The Methods for the Table "Company"
    public void add_company(ArrayList<CompanyInfo> CompanyList){
        db.beginTransaction();
        try {
            for (CompanyInfo company : CompanyList) {
                //Log.e("=============!!!!!!!!!!!!!!!!", "EEca");

                db.execSQL("INSERT INTO Company(name, address, phone, website,billing_address_id, contact_id, event_id," +
                        "industry_id, sale_id, shipping_address_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new Object[]{
                        company.getName(),company.getAddress(),company.getPhone(),company.getWebsite(),
                        company.getBilling_address_id(),company.getContact_id(),company.getEvent_id(),company.getIndustry_id(),
                        company.getSale_id(),company.getShipping_address_id()
                });
                //Cursor c = db.rawQuery("SELECT _id FROM Company WHERE name=?", new String[]{company.getName()});
                //company.setDb_id(c.getColumnIndex("_id"));

                //Debug 信息
                //Log.e("Company name:" + c.getString(c.getColumnIndex("name")), " ============");
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
    public void company_updateName (CompanyInfo company){
        ContentValues cv = new ContentValues();
        cv.put("name", company.getName());
        db.update("Company", cv, "name = ?", new String[]{company.getName()});
    }

    public void company_updatePhone (CompanyInfo company){
        ContentValues cv = new ContentValues();
        cv.put("phone", company.getPhone());
        db.update("Company", cv, "phone = ?", new String[]{company.getPhone()});
    }
    public void company_updateWebsite (CompanyInfo company){
        ContentValues cv = new ContentValues();
        cv.put("website", company.getWebsite());
        db.update("Company", cv, "website = ?", new String[]{company.getWebsite()});
    }


    public void deleteOldCompany(CompanyInfo company){
        db.delete("Company", "name=?", new String[]{company.getName()});
    }

    public void company_deleteAll() {
        db.execSQL("DELETE FROM Company");
    }

    public ArrayList<CompanyInfo> company_queryAll() {
        ArrayList<CompanyInfo> currentList = new ArrayList<CompanyInfo>();
        Cursor c = db.rawQuery("SELECT * FROM Company", null);
        if (c != null) {
            while (c.moveToNext()) {
                CompanyInfo newCompany =  new CompanyInfo();
                newCompany.setName(c.getString((c.getColumnIndex("name"))));
                newCompany.setPhone(c.getString((c.getColumnIndex("phone"))));
                newCompany.setWebsite(c.getString((c.getColumnIndex("website"))));
                newCompany.setBilling_address_id(c.getInt((c.getColumnIndex("billing_address_id"))));
                newCompany.setContact_id(c.getInt((c.getColumnIndex("contact_id"))));
                newCompany.setEvent_id(c.getInt((c.getColumnIndex("event_id"))));
                newCompany.setIndustry_id(c.getInt(c.getColumnIndex("industry_id")));
                newCompany.setSale_id(c.getInt(c.getColumnIndex("sale_id")));
                newCompany.setShipping_address_id(c.getInt(c.getColumnIndex("shipping_address_id")));

                currentList.add(newCompany);
            }
        }

        c.close();
        return currentList;
    }

    public ArrayList<CompanyInfo> company_queryName(String key_name) {
        ArrayList<CompanyInfo> currentList = new ArrayList<CompanyInfo>();
        Cursor c = db.rawQuery("SELECT * FROM  Company WHERE name = ?", new String[]{key_name});
        if (c != null) {
            while (c.moveToNext()) {
                CompanyInfo newCompany =  new CompanyInfo();
                newCompany.setName(c.getString((c.getColumnIndex("name"))));
                newCompany.setPhone(c.getString((c.getColumnIndex("phone"))));
                newCompany.setWebsite(c.getString((c.getColumnIndex("website"))));
                newCompany.setBilling_address_id(c.getInt((c.getColumnIndex("billing_address_id"))));
                newCompany.setContact_id(c.getInt((c.getColumnIndex("contact_id"))));
                newCompany.setEvent_id(c.getInt((c.getColumnIndex("event_id"))));
                newCompany.setIndustry_id(c.getInt(c.getColumnIndex("industry_id")));
                newCompany.setSale_id(c.getInt(c.getColumnIndex("sale_id")));
                newCompany.setShipping_address_id(c.getInt(c.getColumnIndex("shipping_address_id")));

                currentList.add(newCompany);
            }
        }
        c.close();
        return currentList;
    }
    ////////////////////////////////////////////////////////////////////////////////

    //close database
    public void closeDB() {
        db.close();
    }
}
