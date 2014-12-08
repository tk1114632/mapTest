package com.example.mapTest;

/**
 * Created by tk1114632 on 11/3/14.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.VersionInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.*;
import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class main extends Activity {
    //鏁版嵁搴揅ontactDB
    DBManager ContactDBManager;
    ListView list1;
    ListView list2;
    ArrayList<CompanyInfo> contentInDB;
    ArrayList<ContactInfo_new> contactInDB;
    int currentCompanyDBiD = 0;
    int currentContactDBiD = 0;
    View lastItemSelectedView;
    View lastContactSeletedView;
    View currentViewCompany;
    View currentViewContact;
    int lastItemPosition;
    int lastContactPosition;
    boolean companyMode;

    GeoCoder mSearch = null;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_company);

        ContactDBManager = new DBManager(this);

        list1 = (ListView) findViewById(R.id.companies_List);
        list2 = (ListView) findViewById(R.id.contact_List);
        contentInDB = ContactDBManager.company_queryAll();
        contactInDB = ContactDBManager.contact_queryAll();
        Context context = getApplicationContext();
        companyListAdapter arrayAdapter = new companyListAdapter(context, contentInDB);
        list1.setAdapter(arrayAdapter);
        refreshCompanyDetailDisplay();

        companyMode = true;
        //鐭寜
        list1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentCompanyDBiD = contentInDB.get(position).getDb_id();
                Log.e("","DBID ========"+contentInDB.get(position).getDb_id());
                Log.e("Position=================",""+contentInDB.get(position).getPositionLng()+",,,,,,"+contentInDB.get(position).getPositionLat());

                if (lastItemSelectedView != null) {
                    if (lastItemPosition%2 == 1){
                        lastItemSelectedView.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                    else {
                        lastItemSelectedView.setBackgroundColor(getResources().getColor(R.color.grey_3));
                    }
                }

                view.setBackgroundColor(getResources().getColor(R.color.grey_2));
                lastItemSelectedView = view;
                lastItemPosition = position;
                refreshCompanyDetailDisplay();
            }
        });
        list1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                currentCompanyDBiD = contentInDB.get(position).getDb_id();
                new AlertDialog.Builder(main.this)
                        .setTitle("删除公司信息")
                        .setMessage("确认删除"+contentInDB.get(position).getName()+"全部信息吗?")
                        .setPositiveButton("否", null)
                        .setNegativeButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContactDBManager.deleteOldCompany(contentInDB.get(position).getDb_id());
                                refreshCompanyList();
                                currentCompanyDBiD = 0;
                                refreshCompanyDetailDisplay();
                            }
                        })
                        .setIcon(R.drawable.ic_launcher)
                        .show();
                return true;
            }
        });

        contactListAdapter arrayAdapter_contact = new contactListAdapter(context, contactInDB);
        list2.setAdapter(arrayAdapter_contact);
        refreshContactDetailDisplay();
        list2.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentContactDBiD = contactInDB.get(position).getDb_id();
                if (lastContactSeletedView != null) {
                    if (lastContactPosition%2 == 1){
                        lastContactSeletedView.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                    else {
                        lastContactSeletedView.setBackgroundColor(getResources().getColor(R.color.grey_3));
                    }
                }

                view.setBackgroundColor(getResources().getColor(R.color.grey_2));
                lastContactSeletedView = view;
                lastContactPosition = position;
                Log.e("","DBID ========"+contactInDB.get(position).getDb_id() + "========"+contactInDB.get(position).getLastname());

                refreshContactDetailDisplay();

            }
        });
        list2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                currentContactDBiD = contactInDB.get(position).getDb_id();
                new AlertDialog.Builder(main.this)
                        .setTitle("删除联系人")
                        .setMessage("确认删除"+contactInDB.get(position).getLastname()+contactInDB.get(position).getFirstname()+"吗?")
                        .setPositiveButton("否", null)
                        .setNegativeButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContactDBManager.deleteOldContact(contactInDB.get(position).getDb_id());
                                refreshContactList();
                                currentContactDBiD = 0;
                                refreshContactDetailDisplay();
                                refreshCompanyDetailDisplay();
                            }
                        })
                        .setIcon(R.drawable.ic_launcher)
                        .show();
                return true;
            }
        });

    }

        /*ContactDBManager = new DBManager(this);

        refreshList();

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
        ContactInfo temp = new ContactInfo("瀹嬫槑浜�","涓婃捣浜ゅぇ","涓婃捣甯傞椀琛屽尯涓滃窛璺�800鍙�",31.030579,121.435007,"18916924886");
        list.add(temp);
        temp = new ContactInfo("Vincent","Logic Solutions", "涓婃捣甯傛郸涓滄柊鍖哄崥闇炶矾50鍙�",31.205939,121.609884,"13549998877");
        list.add(temp);

        ContactDBManager.add_person(list);


    }

    public void addTwo(View view){
        ArrayList<ContactInfo> list = new ArrayList<ContactInfo>();
        ContactInfo temp = new ContactInfo("浜烘皯骞垮満", "浜烘皯骞垮満","涓婃捣甯備汉姘戝箍鍦�",31.238802,121.481033,"110");
        list.add(temp);
        temp = new ContactInfo("瀛熸眬闃�","涓婃捣浜ら�氬ぇ瀛�", "涓婃捣甯傞檰瀹跺槾",31.242559,121.510786,"+86 131342");
        list.add(temp);
        ContactDBManager.add_person(list);
    }

    public void addNewPerson (View view){
        Intent i = new Intent(main.this , newContact.class);
        startActivity(i);
    }

    public void deleteAll(View view){
        ContactDBManager.deleteAll();
    }

    public void refreshList(){
        ArrayList<String> arrForList = new ArrayList<String>();
        ListView list1 = (ListView) findViewById(R.id.contactsList);
        ArrayList<ContactInfo> contentInDB = new ArrayList<ContactInfo>();
        contentInDB = ContactDBManager.queryAll();
        for (ContactInfo currentContact : contentInDB) {
            arrForList.add(currentContact.getName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, arrForList);
        list1.setAdapter(arrayAdapter);
    }

    public void refreshButton(View view){
        refreshList();
    }*/

    public void refreshCompanyList(){
        contentInDB = ContactDBManager.company_queryAll();
        Context context = getApplicationContext();
        RankCompany(contentInDB);
        companyListAdapter arrayAdapter = new companyListAdapter(context, contentInDB);
        list1.setAdapter(arrayAdapter);
    }
    public void refreshContactList() {
        contactInDB = ContactDBManager.contact_queryAll();
        Context context = getApplicationContext();
        RankContact(contactInDB);
        contactListAdapter arrayAdapter_contact = new contactListAdapter(context, contactInDB);
        list2.setAdapter(arrayAdapter_contact);
    }
    public void refreshCompanyDetailDisplay() {
        View newView = findViewById(R.id.company_i);
        TextView button = (TextView) findViewById(R.id.buttonOfAddr);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }

        newView.setVisibility(View.VISIBLE);
        currentViewCompany = newView;
        if (currentCompanyDBiD > 0) {
            TextView CompanyName = (TextView) findViewById(R.id.company_name);
            TextView WebAddr = (TextView) findViewById(R.id.web_address_content);
            TextView Tel = (TextView) findViewById(R.id.phone_number_content);
            TextView Addr = (TextView) findViewById(R.id.eddasd);
            TextView Field = (TextView) findViewById(R.id.education_type_content);

            CompanyInfo currentCompany = ContactDBManager.company_queryByID(currentCompanyDBiD);
            Log.e("=========","=="+currentCompany.getDb_id());
            CompanyName.setText(currentCompany.getName());
            WebAddr.setText(currentCompany.getWebsite());
            Tel.setText(currentCompany.getPhone());
            Addr.setText(currentCompany.getAddress());
            Field.setText(currentCompany.getField());

            //地址栏旁边的按钮
            if (Addr.getText().toString().equals("")) {
                button.setTextColor(getResources().getColor(R.color.grey_3));
                button.setClickable(false);

            }
            else {
                button.setTextColor(getResources().getColor(R.color.grey));
                button.setClickable(true);
            }
            //与该公司相关的联系人
            Context context = getApplicationContext();
            ListView listForContactInCompany = (ListView) findViewById(R.id.contactInCompany);
            ArrayList<ContactInfo_new> contactOfThisCompany = ContactDBManager.contact_queryByCompanyID(currentCompanyDBiD);
            contactInCompalyListAdapter arrayAdapter = new contactInCompalyListAdapter(context, contactOfThisCompany);
            listForContactInCompany.setAdapter(arrayAdapter);
        }
        else {
            TextView CompanyName = (TextView) findViewById(R.id.company_name);
            TextView WebAddr = (TextView) findViewById(R.id.web_address_content);
            TextView Tel = (TextView) findViewById(R.id.phone_number_content);
            TextView Addr = (TextView) findViewById(R.id.eddasd);
            TextView Field = (TextView) findViewById(R.id.education_type_content);

            CompanyName.setText("公司名称(未选定)");
            WebAddr.setText("");
            Tel.setText("");
            Addr.setText("");
            Field.setText("");
            button.setTextColor(getResources().getColor(R.color.grey_3));
            button.setClickable(false);
        }
    }
    
    void refreshContactDetailDisplay() {
        View newView = findViewById(R.id.display_contact_info_page);
        if (currentViewContact != null) {
            currentViewContact.setVisibility(View.GONE);
        }
        newView.setVisibility(View.VISIBLE);
        currentViewContact = newView;
        TextView last_name = (TextView) findViewById(R.id.display_contact_xs);
        TextView first_name = (TextView) findViewById(R.id.display_contact_mz);
        TextView job = (TextView) findViewById(R.id.display_contact_zw);
        TextView email_gz = (TextView) findViewById(R.id.display_contact_email_gz);
        TextView email_sr = (TextView) findViewById(R.id.display_contact_email_sr);
        TextView cellphone = (TextView) findViewById(R.id.display_contact_sj);
        TextView tel_gz = (TextView) findViewById(R.id.display_contact_phone_gz);
        TextView tel_sr = (TextView) findViewById(R.id.display_contact_phone_sr);
        TextView notes = (TextView) findViewById(R.id.display_contact_bz);

        TextView qq_num = (TextView) findViewById(R.id.display_contact_qq);
        TextView wechat = (TextView) findViewById(R.id.display_contact_wc);
        TextView weibo = (TextView) findViewById(R.id.display_contact_wb);
        TextView skype = (TextView) findViewById(R.id.display_contact_sk);

        TextView country = (TextView) findViewById(R.id.display_contact_gj);
        TextView province = (TextView) findViewById(R.id.display_contact_ss);
        TextView city = (TextView) findViewById(R.id.display_contact_cs);
        TextView address = (TextView) findViewById(R.id.display_contact_dz);
        TextView zip = (TextView) findViewById(R.id.display_contact_yzbm);
        if (currentContactDBiD > 0) {
            ContactInfo_new currentPerson = ContactDBManager.contact_queryByID(currentContactDBiD);
            last_name.setText(currentPerson.getLastname());
            first_name.setText(currentPerson.getFirstname());
            job.setText(currentPerson.getDuty());
            email_gz.setText(currentPerson.getEmail_work());
            email_sr.setText(currentPerson.getEmail_personal());
            cellphone.setText(currentPerson.getPhone_mobile());
            tel_gz.setText(currentPerson.getPhone_work());
            tel_sr.setText(currentPerson.getEmail_personal());
            notes.setText(currentPerson.getNote());
            qq_num.setText(currentPerson.getQq());
            wechat.setText(currentPerson.getWechat());
            weibo.setText(currentPerson.getWeibo());
            skype.setText(currentPerson.getSkype());
            address.setText(currentPerson.getAddress());

            country.setText("中国");
            province.setText("上海");
            city.setText("上海");

        }
        else {
            last_name.setText("");
            first_name.setText("");
            job.setText("");
            email_gz.setText("");
            email_sr.setText("");
            cellphone.setText("");
            tel_gz.setText("");
            tel_sr.setText("");
            notes.setText("");
            qq_num.setText("");
            wechat.setText("");
            weibo.setText("");
            skype.setText("");
            address.setText("");
        }
    	
    }


    public void companyOnclick(View view) {
        TextView companyText = (TextView) findViewById(R.id.link_to_companies);
        TextView contactText = (TextView) findViewById(R.id.link_to_contacts);
        if (companyText.getCurrentTextColor() == getResources().getColor(R.color.blue_1)) {
            return;
        }
        contactText.setBackgroundColor(getResources().getColor(R.color.blue_1));
        contactText.setTextColor(getResources().getColor(R.color.grey_3));
        companyText.setBackgroundColor(getResources().getColor(R.color.white));
        companyText.setTextColor(getResources().getColor(R.color.blue_1));
        LinearLayout layout = (LinearLayout) findViewById(R.id.companyDetail);
        layout.setVisibility(View.VISIBLE);
        layout = (LinearLayout) findViewById(R.id.contactDetail);
        layout.setVisibility(View.GONE);
        companyMode = true;
        list2.setVisibility(View.GONE);
        list1.setVisibility(View.VISIBLE);

    }

    public void contactOnclick(View view) {
        TextView companyText = (TextView) findViewById(R.id.link_to_companies);
        TextView contactText = (TextView) findViewById(R.id.link_to_contacts);
        if (contactText.getCurrentTextColor() == getResources().getColor(R.color.blue_1)) {
            return;
        }
        contactText.setBackgroundColor(getResources().getColor(R.color.white));
        contactText.setTextColor(getResources().getColor(R.color.blue_1));
        companyText.setBackgroundColor(getResources().getColor(R.color.blue_1));
        companyText.setTextColor(getResources().getColor(R.color.grey_3));
        LinearLayout layout = (LinearLayout) findViewById(R.id.companyDetail);
        layout.setVisibility(View.GONE);
        layout = (LinearLayout) findViewById(R.id.contactDetail);
        layout.setVisibility(View.VISIBLE);
        companyMode = false;
        list1.setVisibility(View.GONE);
        list2.setVisibility(View.VISIBLE);
    }

    public void toMapViewButton(View view){
        Intent i = new Intent(main.this , mapView.class);
        startActivity(i);
    }

    public void add_new_companyorContact(View view) {
        if (companyMode) {
            View newView = findViewById(R.id.add_co_page);
            EditText company_name = (EditText) findViewById(R.id.xjgs_gsm_input);
            EditText tel = (EditText) findViewById(R.id.xjgs_lxdh_input);
            EditText addr = (EditText) findViewById(R.id.xjgs_dz_input);
            EditText web = (EditText) findViewById(R.id.xjgs_wz_input);
            EditText field = (EditText) findViewById(R.id.xjgs_hy_input);

            company_name.setText("");
            tel.setText("");
            addr.setText("");
            web.setText("");
            field.setText("");
            if (currentViewCompany != null) {
                currentViewCompany.setVisibility(View.GONE);
            }
            newView.setVisibility(view.VISIBLE);
            currentViewCompany = newView;
        }
        else {
            View newView = findViewById(R.id.add_contact_fromContact);
            if (currentViewContact != null) {
                currentViewContact.setVisibility(View.GONE);
            }
            newView.setVisibility(View.VISIBLE);
            currentViewContact = newView;
            EditText key = (EditText) findViewById(R.id.xjlxr_xs_input);
            key.setText("");
            key = (EditText) findViewById(R.id.xjlxr_mz_input);
            key.setText("");
            key = (EditText) findViewById(R.id.xjlxr_zw_input);
            key = (EditText) findViewById(R.id.xjlxr_dzyj_gz_input);
            key.setText("");
            key = (EditText) findViewById(R.id.xjlxr_dzyj_sr_input);
            key.setText("");
            key = (EditText) findViewById(R.id.xjlxr_sj_input);
            key.setText("");
            key = (EditText) findViewById(R.id.xjlxr_dh_gz_input);
            key.setText("");
            key = (EditText) findViewById(R.id.xjlxr_dh_sr_input);
            key.setText("");
            key = (EditText) findViewById(R.id.xjlxr_bz_input);
            key.setText("");

            key = (EditText) findViewById(R.id.jstx_qq_input);
            key.setText("");
            key = (EditText) findViewById(R.id.jstx_wc_input);
            key.setText("");
            key = (EditText) findViewById(R.id.jstx_sk_input);
            key.setText("");
            key = (EditText) findViewById(R.id.jstx_wb_input);
            key.setText("");

            key = (EditText) findViewById(R.id.dzxx_gj_input);
            key.setText("");
            key = (EditText) findViewById(R.id.dzxx_ss_input);
            key.setText("");
            key = (EditText) findViewById(R.id.dzxx_cs_input);
            key.setText("");
            key = (EditText) findViewById(R.id.dzxx_dz_input);
            key.setText("");
            key = (EditText) findViewById(R.id.dzxx_yzbm_input);
            key.setText("");
        }

    }
    //锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
    public void add_co_page_back(View view) {
        View newView = findViewById(R.id.company_i);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }
    public void add_co_page_save_and_new(View view){
        EditText company_name = (EditText) findViewById(R.id.xjgs_gsm_input);
        EditText tel = (EditText) findViewById(R.id.xjgs_lxdh_input);
        EditText addr = (EditText) findViewById(R.id.xjgs_dz_input);
        EditText web = (EditText) findViewById(R.id.xjgs_wz_input);
        EditText field = (EditText) findViewById(R.id.xjgs_hy_input);


        ArrayList<CompanyInfo> currentCompanyList = new ArrayList<CompanyInfo>();
        CompanyInfo currentCompany = new CompanyInfo();
        currentCompany.setName(company_name.getText().toString());
        currentCompany.setPhone(tel.getText().toString());
        currentCompany.setAddress(addr.getText().toString());
        currentCompany.setWebsite(web.getText().toString());
        currentCompany.setField(field.getText().toString());

        currentCompanyList.add(currentCompany);

        ContactDBManager.add_company(currentCompanyList);
        View newView = findViewById(R.id.company_i);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
        refreshCompanyList();

    }

    //锟斤拷锟斤拷锟洁辑锟斤拷司页
    public void Turn_to_edit_co_page(View view) {
        if (currentCompanyDBiD > 0) {
            TextView CompanyName = (TextView) findViewById(R.id.edit_co_page_companyName);
            TextView WebAddr = (TextView) findViewById(R.id.edit_co_page_webSite);
            TextView Tel = (TextView) findViewById(R.id.edit_co_page_tel);
            TextView Addr = (TextView) findViewById(R.id.edit_co_page_address);
            TextView Field = (TextView) findViewById(R.id.edit_co_page_field);

            CompanyInfo currentCompany = ContactDBManager.company_queryByID(currentCompanyDBiD);
            CompanyName.setText(currentCompany.getName());
            WebAddr.setText(currentCompany.getWebsite());
            Tel.setText(currentCompany.getPhone());
            Addr.setText(currentCompany.getAddress());
            Field.setText(currentCompany.getField());
        }
        View newView = findViewById(R.id.edit_co_page);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }
    //锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
    public void edit_co_page_back(View view) {
        View newView = findViewById(R.id.company_i);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }

    public void co_edit_page_save(View view) {
        View newView = findViewById(R.id.company_i);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
        TextView CompanyName = (TextView) findViewById(R.id.edit_co_page_companyName);
        TextView WebAddr = (TextView) findViewById(R.id.edit_co_page_webSite);
        TextView Tel = (TextView) findViewById(R.id.edit_co_page_tel);
        TextView Addr = (TextView) findViewById(R.id.edit_co_page_address);
        TextView Field = (TextView) findViewById(R.id.edit_co_page_field);

        ContactDBManager.company_updateName(currentCompanyDBiD,CompanyName.getText().toString());
        ContactDBManager.company_updateWebsite(currentCompanyDBiD,WebAddr.getText().toString());
        ContactDBManager.company_updatePhone(currentCompanyDBiD,Tel.getText().toString());
        ContactDBManager.company_updateAddress(currentCompanyDBiD,Addr.getText().toString());
        ContactDBManager.company_updateField(currentCompanyDBiD,Field.getText().toString());
        refreshCompanyDetailDisplay();
    }

    //展锟斤拷锟截闭碉拷址锟斤拷息锟斤拷锟斤拷锟斤拷锟斤拷址锟斤拷锟秸伙拷锟斤拷址锟斤拷
    public void address_list_change(View view) {
        LinearLayout ly = (LinearLayout)findViewById(R.id.dzxx_fo);
        if(ly.getVisibility() == View.VISIBLE){
            ly.setVisibility(View.GONE);
        }
        else {
            ly.setVisibility(View.VISIBLE);
        }
    }

    public void Turn_to_kddz_page(View view) {
        View newView = findViewById(R.id.kddz_page);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }
    //路碌禄脴脰梅陆莽脙忙
    public void Kddz_page_back(View view) {
        View newView = findViewById(R.id.company_i);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }
    //脤酶脰脕驴陋碌楼碌脴脰路卤脿录颅脪鲁
    public void Turn_to_edit_kddz_page(View view) {
        View newView = findViewById(R.id.edit_kddz_page);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }
    //路碌禄脴驴陋碌楼碌脴脰路虏茅驴麓脪鲁
    public void edit_kddz_page_back(View view) {
        View newView = findViewById(R.id.kddz_page);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }
    //卤拢麓忙卤脿录颅驴陋碌楼碌脴脰路
    public void edit_kddz_page_save(View view) {
        View newView = findViewById(R.id.kddz_page);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;

    }

    //脤酶脰脕脢脮禄玫碌脴脰路虏茅驴麓脪鲁
    public void Turn_to_shdz_page(View view) {
        View newView = findViewById(R.id.shdz_page);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }

    //路碌禄脴脰梅陆莽脙忙
    public void Shdz_page_back(View view) {
        View newView = findViewById(R.id.company_i);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }
    //脤酶脰脕脢脮禄玫碌脴脰路卤脿录颅脪鲁
    public void Turn_to_edit_shdz_page(View view) {
        View newView = findViewById(R.id.edit_shdz_page);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }

    //路碌禄脴脢脮禄玫碌脴脰路虏茅驴麓脪鲁
    public void edit_shdz_page_back(View view) {
        View newView = findViewById(R.id.shdz_page);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }

    public void edit_shdz_page_save(View view) {
        View newView = findViewById(R.id.shdz_page);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }
    //碌脴脰路脨脜脧垄录掳脧脿鹿脴   end------------------------------------------------------------------
    public void Turn_to_add_contact_page(View view) {
        View newView = findViewById(R.id.add_contact_page_fromCompany);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }
    public void add_contact_page_back_fromCompany(View view) {
        View newView = findViewById(R.id.company_i);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }
    public void add_contact_page_save_and_new_fromCompany(View view) {
        View newView = findViewById(R.id.company_i);

        EditText lastname = (EditText) findViewById(R.id.xjlxr_xs_input_fromCompany);
        EditText firstname = (EditText) findViewById(R.id.xjlxr_mz_input_fromCompany);
        EditText zw = (EditText) findViewById(R.id.xjlxr_zw_input_fromCompany);
        EditText gzyx= (EditText) findViewById(R.id.xjlxr_dzyj_gz_input_fromCompany);
        EditText sryx = (EditText) findViewById(R.id.xjlxr_dzyj_sr_input_fromCompany);
        EditText phone = (EditText) findViewById(R.id.xjlxr_sj_input_fromCompany);
        EditText tel_gz = (EditText) findViewById(R.id.xjlxr_dh_gz_input_fromCompany);
        EditText tel_sr = (EditText) findViewById(R.id.xjlxr_dh_sr_input_fromCompany);
        EditText notes = (EditText) findViewById(R.id.xjlxr_bz_input_fromCompany);

        EditText qq_num = (EditText) findViewById(R.id.jstx_qq_input_fromCompany);
        EditText wechat = (EditText) findViewById(R.id.jstx_wc_input_fromCompany);
        EditText skype = (EditText) findViewById(R.id.jstx_sk_input_fromCompany);
        EditText  weibo = (EditText) findViewById(R.id.jstx_wb_input_fromCompany);

        EditText country = (EditText) findViewById(R.id.dzxx_gj_input_fromCompany);
        EditText province = (EditText) findViewById(R.id.dzxx_ss_input_fromCompany);
        EditText city = (EditText) findViewById(R.id.dzxx_cs_input_fromCompany);
        EditText address = (EditText) findViewById(R.id.dzxx_dz_input_fromCompany);
        EditText zip = (EditText) findViewById(R.id.dzxx_yzbm_input_fromCompany);


        ContactInfo_new newAddContact = new ContactInfo_new();
        ArrayList<ContactInfo_new> newAddList = new ArrayList<ContactInfo_new>();
        newAddContact.setFirstname(firstname.getText().toString());
        newAddContact.setLastname(lastname.getText().toString());
        newAddContact.setDuty(zw.getText().toString());
        newAddContact.setEmail_work(gzyx.getText().toString());
        newAddContact.setEmail_personal(sryx.getText().toString());
        newAddContact.setPhone_mobile(phone.getText().toString());
        newAddContact.setPhone_work(tel_gz.getText().toString());
        newAddContact.setPhone_home(tel_sr.getText().toString());
        newAddContact.setNote(notes.getText().toString());

        newAddContact.setQq(qq_num.getText().toString());
        newAddContact.setWechat(wechat.getText().toString());
        newAddContact.setSkype(skype.getText().toString());
        newAddContact.setWeibo(weibo.getText().toString());
        newAddContact.setAddress(country.getText().toString()
                + province.getText().toString()
                + city.getText().toString()
                + address.getText().toString());

        newAddContact.setCompany_id(currentCompanyDBiD);
        newAddList.add(newAddContact);

        ContactDBManager.add_contact(newAddList);

        refreshContactList();
        refreshCompanyDetailDisplay();

        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }
    //业锟斤拷锟斤拷
    //锟斤拷锟斤拷锟铰斤拷业锟斤拷锟斤拷页
    public void Turn_to_add_ywjc_page(View view) {
        View newView = findViewById(R.id.add_ywjc_page);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }

    //锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
    public void Ywjc_page_back(View view) {
        View newView = findViewById(R.id.company_i);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }

    //业锟斤拷锟教憋拷锟斤拷
    public void Ywjc_save(View view) {
        View newView = findViewById(R.id.company_i);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }

    //锟筋动锟斤拷史
    //锟斤拷锟斤拷锟铰斤拷锟筋动锟斤拷史页
    public void Turn_to_add_hdls_page(View view) {
        View newView = findViewById(R.id.add_hdls_page);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }

    //锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
    public void Hdls_page_back(View view) {
        View newView = findViewById(R.id.company_i);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }

    //锟筋动锟斤拷史锟斤拷锟斤拷
    public void Hdls_save(View view) {
        View newView = findViewById(R.id.company_i);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }

    public void to_edit_contact(View view) {
        View newView = findViewById(R.id.display_contact_edit_page);
        if (currentViewContact != null) {
            currentViewContact.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewContact = newView;
        EditText last_name = (EditText) findViewById(R.id.edit_contact_xs);
        EditText first_name = (EditText) findViewById(R.id.edit_contact_mz);
        EditText job = (EditText) findViewById(R.id.edit_contact_zw);
        EditText email_gz = (EditText) findViewById(R.id.edit_contact_email_gz);
        EditText email_sr = (EditText) findViewById(R.id.edit_contact_email_sr);
        EditText cellphone = (EditText) findViewById(R.id.edit_contact_sj);
        EditText tel_gz = (EditText) findViewById(R.id.edit_contact_phone_gz);
        EditText tel_sr = (EditText) findViewById(R.id.edit_contact_phone_sr);
        EditText notes = (EditText) findViewById(R.id.edit_contact_bz);

        EditText qq_num = (EditText) findViewById(R.id.edit_contact_qq);
        EditText wechat = (EditText) findViewById(R.id.edit_contact_wc);
        EditText weibo = (EditText) findViewById(R.id.edit_contact_wb);
        EditText skype = (EditText) findViewById(R.id.edit_contact_sk);

        EditText country = (EditText) findViewById(R.id.edit_contact_gj);
        EditText province = (EditText) findViewById(R.id.edit_contact_ss);
        EditText city = (EditText) findViewById(R.id.edit_contact_cs);
        EditText address = (EditText) findViewById(R.id.edit_contact_dz);
        EditText zip = (EditText) findViewById(R.id.edit_contact_yzbm);

        ContactInfo_new thisContact = ContactDBManager.contact_queryByID(currentContactDBiD);
        last_name.setText(thisContact.getFirstname());
        first_name.setText(thisContact.getLastname());
        job.setText(thisContact.getDuty());
        email_gz.setText(thisContact.getEmail_work());
        email_sr.setText(thisContact.getEmail_personal());
        cellphone.setText(thisContact.getPhone_mobile());
        tel_gz.setText(thisContact.getPhone_work());
        tel_sr.setText(thisContact.getPhone_home());
        notes.setText(thisContact.getNote());

        qq_num.setText(thisContact.getQq());
        wechat.setText(thisContact.getWechat());
        weibo.setText(thisContact.getWeibo());
        skype.setText(thisContact.getSkype());

        country.setText("中国");
        address.setText(thisContact.getAddress());

    }

    public void edit_contact_page_back(View view) {
        View newView = findViewById(R.id.display_contact_info_page);
        if (currentViewContact != null) {
            currentViewContact.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewContact = newView;
    }

    public void edit_contact_page_save(View view) {
        View newView = findViewById(R.id.display_contact_info_page);
        if (currentViewContact != null) {
            currentViewContact.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewContact = newView;
        
        EditText last_name = (EditText) findViewById(R.id.edit_contact_xs);
        EditText first_name = (EditText) findViewById(R.id.edit_contact_mz);
        EditText job = (EditText) findViewById(R.id.edit_contact_zw);
        EditText email_gz = (EditText) findViewById(R.id.edit_contact_email_gz);
        EditText email_sr = (EditText) findViewById(R.id.edit_contact_email_sr);
        EditText cellphone = (EditText) findViewById(R.id.edit_contact_sj);
        EditText tel_gz = (EditText) findViewById(R.id.edit_contact_phone_gz);
        EditText tel_sr = (EditText) findViewById(R.id.edit_contact_phone_sr);
        EditText notes = (EditText) findViewById(R.id.edit_contact_bz);
        
        EditText qq_num = (EditText) findViewById(R.id.edit_contact_qq);
        EditText wechat = (EditText) findViewById(R.id.edit_contact_wc);
        EditText weibo = (EditText) findViewById(R.id.edit_contact_wb);
        EditText skype = (EditText) findViewById(R.id.edit_contact_sk);
        EditText country = (EditText) findViewById(R.id.edit_contact_gj);
        EditText province = (EditText) findViewById(R.id.edit_contact_ss);
        EditText city = (EditText) findViewById(R.id.edit_contact_cs);
        EditText address = (EditText) findViewById(R.id.edit_contact_dz);
        EditText zip = (EditText) findViewById(R.id.edit_contact_yzbm);

        ContactDBManager.contact_updateLastname(currentContactDBiD, last_name.getText().toString());
        ContactDBManager.contact_updateFirstname(currentContactDBiD, first_name.getText().toString());
        ContactDBManager.contact_updateTitle(currentContactDBiD, job.getText().toString());
        ContactDBManager.contact_updateEmail_work(currentContactDBiD, email_gz.getText().toString());
        ContactDBManager.contact_updateEmail_personal(currentContactDBiD, email_sr.getText().toString());
        ContactDBManager.contact_updatePhone_mobile(currentContactDBiD, cellphone.getText().toString());
        ContactDBManager.contact_updateNote(currentContactDBiD,notes.getText().toString());
        ContactDBManager.contact_updatePhone_work(currentContactDBiD, tel_gz.getText().toString());
        ContactDBManager.contact_updatePhone_home(currentContactDBiD, tel_sr.getText().toString());

        ContactDBManager.contact_updateQq(currentContactDBiD, qq_num.getText().toString());
        ContactDBManager.contact_updateWechat(currentContactDBiD,wechat.getText().toString());
        ContactDBManager.contact_updateWeibo(currentContactDBiD, weibo.getText().toString());
        ContactDBManager.contact_updateSkype(currentContactDBiD, skype.getText().toString());
        ContactDBManager.contact_updateAddress(currentContactDBiD, address.getText().toString());

        refreshContactDetailDisplay();
    }

    public void add_contact_page_back_fromContact(View view) {
        View newView = findViewById(R.id.display_contact_info_page);
        if (currentViewContact != null) {
            currentViewContact.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewContact = newView;
    }
    public void add_contact_page_save_and_new_fromContact(View view) {
        View newView = findViewById(R.id.display_contact_info_page);

        EditText lastname = (EditText) findViewById(R.id.xjlxr_xs_input);
        EditText firstname = (EditText) findViewById(R.id.xjlxr_mz_input);
        EditText zw = (EditText) findViewById(R.id.xjlxr_zw_input);
        EditText gzyx= (EditText) findViewById(R.id.xjlxr_dzyj_gz_input);
        EditText sryx = (EditText) findViewById(R.id.xjlxr_dzyj_sr_input);
        EditText phone = (EditText) findViewById(R.id.xjlxr_sj_input);
        EditText tel_gz = (EditText) findViewById(R.id.xjlxr_dh_gz_input);
        EditText tel_sr = (EditText) findViewById(R.id.xjlxr_dh_sr_input);
        EditText notes = (EditText) findViewById(R.id.xjlxr_bz_input);

        EditText qq_num = (EditText) findViewById(R.id.jstx_qq_input);
        EditText wechat = (EditText) findViewById(R.id.jstx_wc_input);
        EditText skype = (EditText) findViewById(R.id.jstx_sk_input);
        EditText  weibo = (EditText) findViewById(R.id.jstx_wb_input);

        EditText country = (EditText) findViewById(R.id.dzxx_gj_input);
        EditText province = (EditText) findViewById(R.id.dzxx_ss_input);
        EditText city = (EditText) findViewById(R.id.dzxx_cs_input);
        EditText address = (EditText) findViewById(R.id.dzxx_dz_input);
        EditText zip = (EditText) findViewById(R.id.dzxx_yzbm_input);


        ContactInfo_new newAddContact = new ContactInfo_new();
        ArrayList<ContactInfo_new> newAddList = new ArrayList<ContactInfo_new>();
        newAddContact.setFirstname(firstname.getText().toString());
        newAddContact.setLastname(lastname.getText().toString());
        newAddContact.setDuty(zw.getText().toString());
        newAddContact.setEmail_work(gzyx.getText().toString());
        newAddContact.setEmail_personal(sryx.getText().toString());
        newAddContact.setPhone_mobile(phone.getText().toString());
        newAddContact.setPhone_work(tel_gz.getText().toString());
        newAddContact.setPhone_home(tel_sr.getText().toString());
        newAddContact.setNote(notes.getText().toString());

        newAddContact.setQq(qq_num.getText().toString());
        newAddContact.setWechat(wechat.getText().toString());
        newAddContact.setSkype(skype.getText().toString());
        newAddContact.setWeibo(weibo.getText().toString());
        newAddContact.setAddress(country.getText().toString()
                + province.getText().toString()
                + city.getText().toString()
                + address.getText().toString());
        newAddList.add(newAddContact);

        ContactDBManager.add_contact(newAddList);

        if (currentViewContact != null) {
            currentViewContact.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewContact = newView;
        refreshContactList();

    }
    public void address_OnClick(View view) {
        TextView addrText = (TextView) findViewById(R.id.eddasd);
        final TextView companyName = (TextView) findViewById(R.id.company_name);
        final TextView tel = (TextView) findViewById(R.id.phone_number_content);
        final String addr = addrText.getText().toString();
        mSearch = GeoCoder.newInstance();
        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(main.this, "抱歉，地图上未找到该地址", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                double lat = result.getLocation().latitude;
                double lng = result.getLocation().longitude;
                ContactDBManager.company_updateCoordinate(currentCompanyDBiD, lat, lng);
                ContactInfo selectedContact = new ContactInfo();
                selectedContact.setName(companyName.getText().toString());
                selectedContact.setAddress(addr);
                selectedContact.setCompany(companyName.getText().toString());
                selectedContact.setTel(tel.getText().toString());
                selectedContact.setPositionLat(lat);
                selectedContact.setPositionLng(lng);

                Intent newIntent = new Intent(main.this, mapView.class);
                Bundle newBundle = new Bundle();
                newBundle.putSerializable("selectedInfo", selectedContact);
                newIntent.putExtras(newBundle);

                startActivity(newIntent);
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                return;
            }
        };
        mSearch.setOnGetGeoCodeResultListener(listener);
        mSearch.geocode(new GeoCodeOption().city("上海").address(addr));
    }

    public void contactOfCompany_Button(View view) {
        View theList = findViewById(R.id.contactInCompany);
        if (theList.getVisibility() == View.GONE) {
            theList.setVisibility(View.VISIBLE);
        }
        else {
            theList.setVisibility(View.GONE);
        }
        Log.e("==================","listOnClick");
    }

    public void RankCompany(ArrayList<CompanyInfo> currentList) {
        PinyinComparator thiscomparator = new PinyinComparator();
        Collections.sort(currentList, thiscomparator);
    }
    public void RankContact(ArrayList<ContactInfo_new> currentList) {
        PinyinComparator2 thiscomparator = new PinyinComparator2();
        Collections.sort(currentList, thiscomparator);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        refreshCompanyList();
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ContactDBManager.closeDB();
        if (mSearch != null) {mSearch.destroy();}
    }

}
