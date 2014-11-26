package com.example.mapTest;

import java.io.Serializable;

/**
 * Created by tk1114632 on 11/10/14.
 */
public class CompanyInfo implements Serializable {

    public String Name;
    public String Phone;
    public String Website;
    public String Address;
    public String Field;
    public int Billing_address_id;
    public int Contact_id;
    public int Event_id;
    public int Industry_id;
    public int Sale_id;
    public int Shipping_address_id;

    private int db_id = -1;

    public String getField() {
        return Field;
    }

    public void setField(String field) {
        Field = field;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getDb_id() {
        return db_id;
    }

    public void setDb_id(int db_id) {
        this.db_id = db_id;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public String getPhone() {

        return Phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getWebsite() {return Website;}

    public void setWebsite(String website) {Website = website;}

    public int getBilling_address_id(){return Billing_address_id;}

    public void setBilling_address_id(int billing_address_id) {Billing_address_id = billing_address_id;}

    public int getContact_id(){return Contact_id;}

    public void setContact_id(int contact_id){Contact_id = contact_id;}

    public int getEvent_id(){return Event_id;}

    public void setEvent_id(int event_id){Event_id = event_id;}

    public int getIndustry_id(){return Industry_id;}

    public void setIndustry_id(int industry_id){Industry_id=industry_id;}

    public int getSale_id(){return Sale_id;}

    public void setSale_id(int sale_id){Sale_id=sale_id;}

    public int getShipping_address_id() {return Shipping_address_id;}

    public void setShipping_address_id(int shipping_address_id) {Shipping_address_id = shipping_address_id;}

    public CompanyInfo(String name, String phone, String website,int billing_address_id, int contact_id, int event_id,int industry_id, int sale_id, int shipping_address_id) {
        Name = name;
        Phone = phone;
        Website = website;
        Billing_address_id = billing_address_id;
        Contact_id = contact_id;
        Event_id = event_id;
        Industry_id  = industry_id;
        Sale_id = sale_id;
        Shipping_address_id = shipping_address_id;
    }
    public CompanyInfo(){

    }

    //public AddressInfo(String 宋明亨, String 上海交大, String 上海市闵行区东川路800号, double v, double v1, String s, String s1, double s2, double s3, double s4) {
    //}
}
