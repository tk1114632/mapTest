package com.example.mapTest;

import java.io.Serializable;

/**
 * Created by tk1114632 on 11/10/14.
 */
public class ContactInfo_new implements Serializable {

    public String Email_personal;
    public String Email_work;
    public String Firstname;
    public String Lastname;
    public String Note;
    public String Phone_home;
    public String  Phone_mobile;
    public String Phone_work;
    public String Qq;
    public String Skype;
    public String Title;
    public String Wechat;
    public String Weibo;
    private double PositionLat;
    private double PositionLng;
    public double getPositionLat() {
        return PositionLat;
    }

    public void setPositionLat(double positionLat) {
        PositionLat = positionLat;
    }

    public double getPositionLng() {
        return PositionLng;
    }

    public void setPositionLng(double positionLng) {
        PositionLng = positionLng;
    }



    public String address;
    public String duty;

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int Address_id;
    public int Company_id;
    public int Event_id;
    public int Project_id;
    public int Sale_id;


    private int db_id = -1;

    public int getDb_id() {
        return db_id;
    }

    public void setDb_id(int db_id) {
        this.db_id = db_id;
    }

    public void setEmail_personal(String email_personal) {
        this.Email_personal = email_personal;
    }

    public String getEmail_personal() {return Email_personal;}

    public String getEmail_work() {
        return Email_work;
    }

    public void setEmail_work(String email_personal) {Email_personal = email_personal;}

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {Firstname = firstname;}

    public String getLastname() {return Lastname;}

    public void setLastname(String lastname) {Lastname = lastname; }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getPhone_home() {
        return Phone_home;
    }

    public void setPhone_home(String phone_home) {Phone_home = phone_home;}

    public String getPhone_mobile() {return Phone_mobile;}

    public void setPhone_mobile(String phone_mobile){Phone_mobile = phone_mobile;}

    public String getPhone_work() {return Phone_work;}

    public void setPhone_work(String phone_work) {Phone_work = phone_work;}

    public String getQq(){return Qq;}

    public void setQq(String qq){Qq = qq;}

    public String getSkype(){return Skype;}

    public void setSkype(String skype) {Skype = skype;}

    public String getTitle(){return  Title;}

    public void setTitle(String title){Title = title;}

    public String getWechat() {return  Wechat;}

    public void setWechat(String wechat){Wechat = wechat;}

    public String getWeibo(){return Weibo;}

    public void setWeibo(String weibo){Weibo = weibo;}

    public int getAddress_id(){return Address_id;}

    public void setAddress_id(int address_id) {Address_id = address_id;}

    public int getCompany_id() {return Company_id;}

    public void setCompany_id(int company_id) {Company_id = company_id;}

    public int getEvent_id(){return Event_id;}

    public void setEvent_id(int event_id){Event_id = event_id;}

    public int getProject_id() {return Project_id;}

    public void setProject_id(int project_id) {Project_id = project_id;}

    public int getSale_id(){return Sale_id;}

    public void setSale_id(int sale_id){Sale_id=sale_id;}


    public ContactInfo_new(String email_personal, String email_work, String firstname, String lastname,String note,
                       String phone_home, String phone_mobile, String phone_work, String qq, String skype,
                       String title, String wechat, String weibo, int address_id, int company_id, int event_id,
                       int project_id, int sale_id) {
        Email_personal = email_personal;
        Email_work = email_work;
        Firstname = firstname;
        Lastname = lastname;
        Note = note;
        Phone_mobile = phone_mobile;
        Phone_work = phone_work;
        Qq = qq;
        Skype = skype;
        Title = title;
        Wechat = wechat;
        Weibo = weibo;
        Address_id = address_id;
        Company_id = company_id;
        Event_id =event_id;
        Project_id = project_id;
        Sale_id = sale_id;
    }

    public ContactInfo_new() {
    }
}
