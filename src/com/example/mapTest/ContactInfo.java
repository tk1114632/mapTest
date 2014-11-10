package com.example.mapTest;

import java.io.Serializable;

/**
 * Created by tk1114632 on 11/10/14.
 */
public class ContactInfo implements Serializable {

    public String Name;
    public String Company;
    public String Address;
    public double PositionLat;
    public double PositionLng;
    public String tel;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

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

    public ContactInfo(String company, String name, String address, double positionLat, double positionLng, String tel) {
        Company = company;
        Name = name;
        Address = address;
        PositionLat = positionLat;
        PositionLng = positionLng;
        this.tel = tel;
    }

    public ContactInfo() {
    }
}
