package com.example.mapTest;

import java.io.Serializable;

/**
 * Created by tk1114632 on 11/10/14.
 */
public class AddressInfo implements Serializable {

    public String Company_billing_address;
    public String Company_shipping_address;
    public int Company_id;

    private int db_id = -1;

    public String getCompany_billing_address() {
        return Company_billing_address;
    }

    public void setCompany_billing_address(String company_billing_address) {
        Company_billing_address = company_billing_address;
    }

    public String getCompany_shipping_address() {
        return Company_shipping_address;
    }

    public void setCompany_shipping_address(String company_shipping_address) {
        Company_shipping_address = company_shipping_address;
    }

    public int getCompany_id() {
        return Company_id;
    }

    public void setCompany_id(int company_id) {
        Company_id = company_id;
    }

    public int getDb_id() {
        return db_id;
    }

    public void setDb_id(int db_id) {
        this.db_id = db_id;
    }
}
