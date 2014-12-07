package com.example.mapTest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.baidu.mapapi.model.LatLng;

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
                newPerson.setCompany(c.getString((c.getColumnIndex("name"))));
                newPerson.setAddress(c.getString((c.getColumnIndex("address"))));
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

                db.execSQL("INSERT INTO Company(name, address, phone, website, field, billing_address_id, contact_id, event_id," +
                        "industry_id, sale_id, shipping_address_id, position_lat, position_lng) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)", new Object[]{
                        company.getName(),company.getAddress(),company.getPhone(),company.getWebsite(), company.getField(),
                        company.getBilling_address_id(),company.getContact_id(),company.getEvent_id(),company.getIndustry_id(),
                        company.getSale_id(),company.getShipping_address_id(),company.getPositionLat(),company.getPositionLng()
                });
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
    public void company_updateCoordinate(int DbId, double lat, double lng) {
        db.execSQL("UPDATE Company set position_lat = " + lat + " where _id = "+DbId);
        db.execSQL("UPDATE Company set position_lng = " + lng + " where _id = "+DbId);
    }


    public void deleteOldCompany(int companyDB_id){
        db.delete("Company", "_id=?", new String[]{companyDB_id+""});
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
                newCompany.setDb_id(c.getInt(c.getColumnIndex("_id")));
                newCompany.setPositionLat(c.getDouble(c.getColumnIndex("position_lat")));
                newCompany.setPositionLng(c.getDouble(c.getColumnIndex("position_lng")));
                newCompany.setAddress(c.getString(c.getColumnIndex("address")));
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
                newCompany.setDb_id(c.getInt(c.getColumnIndex("_id")));
                currentList.add(newCompany);
            }
        }
        c.close();
        return currentList;
    }

    public CompanyInfo company_queryByID(int key_name) {
        Cursor c = db.rawQuery("SELECT * FROM  Company WHERE _id = ?", new String[]{key_name+""});
        CompanyInfo newCompany =  new CompanyInfo();
        if (c != null) {
            while (c.moveToNext()) {

                newCompany.setName(c.getString((c.getColumnIndex("name"))));
                newCompany.setPhone(c.getString((c.getColumnIndex("phone"))));
                newCompany.setWebsite(c.getString((c.getColumnIndex("website"))));
                newCompany.setAddress(c.getString(c.getColumnIndex("address")));
                newCompany.setField(c.getString(c.getColumnIndex("field")));
                newCompany.setBilling_address_id(c.getInt((c.getColumnIndex("billing_address_id"))));
                newCompany.setContact_id(c.getInt((c.getColumnIndex("contact_id"))));
                newCompany.setEvent_id(c.getInt((c.getColumnIndex("event_id"))));
                newCompany.setIndustry_id(c.getInt(c.getColumnIndex("industry_id")));
                newCompany.setSale_id(c.getInt(c.getColumnIndex("sale_id")));
                newCompany.setShipping_address_id(c.getInt(c.getColumnIndex("shipping_address_id")));
                newCompany.setPositionLat(c.getDouble(c.getColumnIndex("position_lat")));
                newCompany.setPositionLng(c.getDouble(c.getColumnIndex("position_lng")));
                newCompany.setDb_id(c.getInt(c.getColumnIndex("_id")));
            }
        }
        c.close();
        return newCompany;
    }
    ////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    //The Methods for Table "Contact" are shown below
    public void add_contact(ArrayList<ContactInfo_new> ContactList){
        db.beginTransaction();
        try {
            for (ContactInfo_new contact : ContactList) {
                //Log.e("=============!!!!!!!!!!!!!!!!", "EEca");
                db.execSQL("INSERT INTO Contact(email_personal, email_work, firstname, lastname, " +
                        " phone_home, phone_mobile,  phone_work, note, qq, skype," +
                        " title, wechat, weibo, address, duty, address_id, company_id, event_id," +
                        " project_id, sale_id, position_lat, position_lng) VALUES(?, ?, ?, ?, ?,? , ?, ?, ?, ?,?,?, ?,?,?,?,?,?,?,?, ?, ?)", new Object[]{contact.getEmail_personal()
                        , contact.getEmail_work(),contact.getFirstname(),contact.getLastname(),
                        contact.getPhone_home(),contact.getPhone_mobile(),contact.getPhone_work(),contact.getNote(),contact.getQq(),contact.getSkype(),
                        contact.getTitle(),contact.getWechat(), contact.getWeibo(),contact.getAddress(), contact.getDuty(),contact.getAddress_id(),contact.getCompany_id(),
                        contact.getEvent_id(),contact.getProject_id(),contact.getSale_id(), contact.getPositionLat(), contact.getPositionLng()});
                //Cursor c = db.rawQuery("SELECT _id FROM Contact WHERE fisrtname=? and lastname=?", new String[]{contact.getFirstname(),contact.getLastname()});
                //contact.setDb_id(c.getColumnIndex("_id"));
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
    public void contact_updateEmail_personal (ContactInfo_new contact){
        ContentValues cv = new ContentValues();
        cv.put("email_personal", contact.getEmail_personal());
        db.update("Contact", cv, "email_personal = ?", new String[]{contact.getEmail_personal()});
    }

    public void contact_updateEmail_work (ContactInfo_new contact){
        ContentValues cv = new ContentValues();
        cv.put("email_work", contact.getEmail_work());
        db.update("Contact", cv, "email_work = ?", new String[]{contact.getEmail_work()});
    }

    public void contact_updateFirstname (ContactInfo_new contact){
        ContentValues cv = new ContentValues();
        cv.put("firstname", contact.getFirstname());
        db.update("Contact", cv, "firstname = ?", new String[]{contact.getFirstname()});
    }

    public void contact_updateLastname (ContactInfo_new contact){
        ContentValues cv = new ContentValues();
        cv.put("lastname", contact.getLastname());
        db.update("Contact", cv, "lastname = ?", new String[]{contact.getLastname()});
    }

    public void contact_updateNote (ContactInfo_new contact){
        ContentValues cv = new ContentValues();
        cv.put("note", contact.getNote());
        db.update("Contact", cv, "note = ?", new String[]{contact.getNote()});
    }

    public void contact_updatePhone_home (ContactInfo_new contact){
        ContentValues cv = new ContentValues();
        cv.put("phone_home", contact.getPhone_home());
        db.update("Contact", cv, "phone_home = ?", new String[]{contact.getPhone_home()});
    }

    public void contact_updatePhone_mobile (ContactInfo_new contact){
        ContentValues cv = new ContentValues();
        cv.put("phone_mobile", contact.getPhone_mobile());
        db.update("Contact", cv, "phone_mobile = ?", new String[]{contact.getPhone_mobile()});
    }

    public void contact_updatePhone_work (ContactInfo_new contact){
        ContentValues cv = new ContentValues();
        cv.put("phone_work", contact.getPhone_work());
        db.update("Contact", cv, "phone_work = ?", new String[]{contact.getPhone_work()});
    }

    public void contact_updateQq (ContactInfo_new contact){
        ContentValues cv = new ContentValues();
        cv.put("qq", contact.getQq());
        db.update("Contact", cv, "qq = ?", new String[]{contact.getQq()});
    }

    public void contact_updateSkype (ContactInfo_new contact){
        ContentValues cv = new ContentValues();
        cv.put("skype", contact.getSkype());
        db.update("Contact", cv, "skype = ?", new String[]{contact.getSkype()});
    }

    public void contact_updateTitle (ContactInfo_new contact){
        ContentValues cv = new ContentValues();
        cv.put("title", contact.getTitle());
        db.update("Contact", cv, "title = ?", new String[]{contact.getTitle()});
    }

    public void contact_updateWechat (ContactInfo_new contact){
        ContentValues cv = new ContentValues();
        cv.put("wechat", contact.getWechat());
        db.update("Contact", cv, "wechat = ?", new String[]{contact.getWechat()});
    }

    public void contact_updateWeibo (ContactInfo_new contact){
        ContentValues cv = new ContentValues();
        cv.put("weibo", contact.getWeibo());
        db.update("Contact", cv, "weibo = ?", new String[]{contact.getWeibo()});
    }



    public void deleteOldContact(int contact){
        db.delete("Contact", "_id = ?", new String[]{contact+""});
    }

    public void contact_deleteAll() {
        db.delete("Contact", "_id=*",null);
    }

    public ArrayList<ContactInfo_new> contact_queryAll() {
        ArrayList<ContactInfo_new> currentList = new ArrayList<ContactInfo_new>();
        Cursor c = db.rawQuery("SELECT * FROM Contact", null);
        if (c != null) {
            while (c.moveToNext()) {
                ContactInfo_new newContact =  new ContactInfo_new();
                newContact.setEmail_personal(c.getString((c.getColumnIndex("email_personal"))));
                newContact.setEmail_work(c.getString((c.getColumnIndex("email_work"))));
                newContact.setFirstname(c.getString((c.getColumnIndex("firstname"))));
                newContact.setLastname(c.getString((c.getColumnIndex("lastname"))));
                newContact.setNote(c.getString((c.getColumnIndex("note"))));
                newContact.setPhone_home(c.getString((c.getColumnIndex("phone_home"))));
                newContact.setPhone_mobile(c.getString((c.getColumnIndex("phone_mobile"))));
                newContact.setPhone_work(c.getString((c.getColumnIndex("phone_work"))));
                newContact.setQq(c.getString((c.getColumnIndex("qq"))));
                newContact.setSkype(c.getString((c.getColumnIndex("skype"))));
                newContact.setTitle(c.getString((c.getColumnIndex("title"))));
                newContact.setWechat(c.getString((c.getColumnIndex("wechat"))));
                newContact.setWeibo(c.getString(c.getColumnIndex("weibo")));
                newContact.setAddress_id(c.getInt((c.getColumnIndex("address_id"))));
                newContact.setCompany_id(c.getInt((c.getColumnIndex("company_id"))));
                newContact.setEvent_id(c.getInt((c.getColumnIndex("event_id"))));
                newContact.setProject_id(c.getInt(c.getColumnIndex("project_id")));
                newContact.setSale_id(c.getInt(c.getColumnIndex("sale_id")));

                newContact.setDuty(c.getString(c.getColumnIndex("duty")));
                newContact.setDb_id(c.getInt(c.getColumnIndex("_id")));
                newContact.setAddress(c.getString(c.getColumnIndex("address")));

                newContact.setPositionLat(c.getDouble(c.getColumnIndex("position_lat")));
                newContact.setPositionLng(c.getDouble(c.getColumnIndex("position_lng")));

                currentList.add(newContact);
            }
        }

        c.close();
        return currentList;
    }

    public ContactInfo_new contact_queryByID(int keyID) {
        Cursor c = db.rawQuery("SELECT * FROM Contact WHERE _id =?", new String[]{keyID+""});
        ContactInfo_new newContact =  new ContactInfo_new();
        if (c != null) {
            while (c.moveToNext()) {
                newContact.setEmail_personal(c.getString((c.getColumnIndex("email_personal"))));
                newContact.setEmail_work(c.getString((c.getColumnIndex("email_work"))));
                newContact.setFirstname(c.getString((c.getColumnIndex("firstname"))));
                newContact.setLastname(c.getString((c.getColumnIndex("lastname"))));
                newContact.setNote(c.getString((c.getColumnIndex("note"))));
                newContact.setPhone_home(c.getString((c.getColumnIndex("phone_home"))));
                newContact.setPhone_mobile(c.getString((c.getColumnIndex("phone_mobile"))));
                newContact.setPhone_work(c.getString((c.getColumnIndex("phone_work"))));
                newContact.setQq(c.getString((c.getColumnIndex("qq"))));
                newContact.setSkype(c.getString((c.getColumnIndex("skype"))));
                newContact.setTitle(c.getString((c.getColumnIndex("title"))));
                newContact.setWechat(c.getString((c.getColumnIndex("wechat"))));
                newContact.setWeibo(c.getString(c.getColumnIndex("weibo")));
                newContact.setAddress_id(c.getInt((c.getColumnIndex("address_id"))));
                newContact.setCompany_id(c.getInt((c.getColumnIndex("company_id"))));
                newContact.setEvent_id(c.getInt((c.getColumnIndex("event_id"))));
                newContact.setProject_id(c.getInt(c.getColumnIndex("project_id")));
                newContact.setSale_id(c.getInt(c.getColumnIndex("sale_id")));

                newContact.setDuty(c.getString(c.getColumnIndex("duty")));
                newContact.setDb_id(c.getInt(c.getColumnIndex("_id")));
                newContact.setAddress(c.getString(c.getColumnIndex("address")));
            }
        }


        c.close();
        return newContact;
    }

    //close database
    public void closeDB() {
        db.close();
    }
}
