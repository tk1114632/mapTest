package com.example.mapTest;

/**
 * Created by tk1114632 on 11/3/14.
 */
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.VersionInfo;

import java.util.ArrayList;

public class main extends Activity {
    //数据库ContactDB
    DBManager ContactDBManager;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_company);

        ContactDBManager = new DBManager(this);
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
        ContactInfo temp = new ContactInfo("宋明亨","上海交大","上海市闵行区东川路800号",31.030579,121.435007,"18916924886");
        list.add(temp);
        temp = new ContactInfo("Vincent","Logic Solutions", "上海市浦东新区博霞路50号",31.205939,121.609884,"13549998877");
        list.add(temp);

        ContactDBManager.add_person(list);


    }

    public void addTwo(View view){
        ArrayList<ContactInfo> list = new ArrayList<ContactInfo>();
        ContactInfo temp = new ContactInfo("人民广场", "人民广场","上海市人民广场",31.238802,121.481033,"110");
        list.add(temp);
        temp = new ContactInfo("孟汀阳","上海交通大学", "上海市陆家嘴",31.242559,121.510786,"+86 131342");
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
        //ArrayList<String> arrForList = new ArrayList<String>();
        ListView list1 = (ListView) findViewById(R.id.companies_List);
        ArrayList<CompanyInfo> contentInDB = new ArrayList<CompanyInfo>();
        contentInDB = ContactDBManager.company_queryAll();
        //for (CompanyInfo currentContact : contentInDB) {
        //    arrForList.add(currentContact.getName());
        //}
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, arrForList);
        Context context = getApplicationContext();
        companyListAdapter arrayAdapter = new companyListAdapter(context, contentInDB);
        list1.setAdapter(arrayAdapter);

    }

    public void refreshCompanyButton(View view) {
        refreshCompanyList();
    }

    public void toMapViewButton(View view){
        Intent i = new Intent(main.this , mapView.class);
        startActivity(i);
    }

    public void add_new_company(View view) {
        LinearLayout old_layout = (LinearLayout)findViewById(R.id.company_i);
        LinearLayout new_layout = (LinearLayout)findViewById(R.id.add_co_page);
        if(old_layout.getVisibility()==view.VISIBLE){
            old_layout.setVisibility(view.GONE);
            new_layout.setVisibility(view.VISIBLE);
        }
    }
    //����������
    public void add_co_page_back(View view) {
        LinearLayout old_layout = (LinearLayout)findViewById(R.id.add_co_page);
        LinearLayout new_layout = (LinearLayout)findViewById(R.id.company_i);
        old_layout.setVisibility(view.GONE);
        new_layout.setVisibility(view.VISIBLE);
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
        LinearLayout old_layout = (LinearLayout) findViewById(R.id.add_co_page);
        LinearLayout new_layout = (LinearLayout) findViewById(R.id.company_i);
        old_layout.setVisibility(view.GONE);
        new_layout.setVisibility(view.VISIBLE);
    }

    //�����༭��˾ҳ
    public void Turn_to_edit_co_page(View view) {
        LinearLayout old_layout = (LinearLayout)findViewById(R.id.company_i);
        LinearLayout new_layout = (LinearLayout)findViewById(R.id.edit_co_page);
        old_layout.setVisibility(view.GONE);
        new_layout.setVisibility(view.VISIBLE);
    }
    //����������
    public void edit_co_page_back(View view) {
        LinearLayout old_layout = (LinearLayout)findViewById(R.id.edit_co_page);
        LinearLayout new_layout = (LinearLayout)findViewById(R.id.company_i);
        old_layout.setVisibility(view.GONE);
        new_layout.setVisibility(view.VISIBLE);
    }


    //չ���رյ�ַ��Ϣ��������ַ���ջ���ַ��
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
        LinearLayout old_layout = (LinearLayout)findViewById(R.id.company_i);
        LinearLayout new_layout = (LinearLayout)findViewById(R.id.kddz_page);
        old_layout.setVisibility(View.GONE);
        new_layout.setVisibility(View.VISIBLE);
    }
    //·µ»ØÖ÷½çÃæ
    public void Kddz_page_back(View view) {
        LinearLayout old_layout = (LinearLayout)findViewById(R.id.kddz_page);
        LinearLayout new_layout = (LinearLayout)findViewById(R.id.company_i);
        old_layout.setVisibility(View.GONE);
        new_layout.setVisibility(View.VISIBLE);
    }
    //ÌøÖÁ¿ªµ¥µØÖ·±à¼­Ò³
    public void Turn_to_edit_kddz_page(View view) {
        LinearLayout old_layout = (LinearLayout)findViewById(R.id.kddz_page);
        LinearLayout new_layout = (LinearLayout)findViewById(R.id.edit_kddz_page);
        old_layout.setVisibility(View.GONE);
        new_layout.setVisibility(View.VISIBLE);
    }
    //·µ»Ø¿ªµ¥µØÖ·²é¿´Ò³
    public void edit_kddz_page_back(View view) {
        LinearLayout old_layout = (LinearLayout)findViewById(R.id.edit_kddz_page);
        LinearLayout new_layout = (LinearLayout)findViewById(R.id.kddz_page);
        old_layout.setVisibility(View.GONE);
        new_layout.setVisibility(View.VISIBLE);
    }
    //±£´æ±à¼­¿ªµ¥µØÖ·
    public void edit_kddz_page_save(View view) {

    }

    //ÌøÖÁÊÕ»õµØÖ·²é¿´Ò³
    public void Turn_to_shdz_page(View view) {
        LinearLayout old_layout = (LinearLayout)findViewById(R.id.company_i);
        LinearLayout new_layout = (LinearLayout)findViewById(R.id.shdz_page);
        old_layout.setVisibility(View.GONE);
        new_layout.setVisibility(View.VISIBLE);
    }

    //·µ»ØÖ÷½çÃæ
    public void Shdz_page_back(View view) {
        LinearLayout old_layout = (LinearLayout)findViewById(R.id.shdz_page);
        LinearLayout new_layout = (LinearLayout)findViewById(R.id.company_i);
        old_layout.setVisibility(View.GONE);
        new_layout.setVisibility(View.VISIBLE);
    }
    //ÌøÖÁÊÕ»õµØÖ·±à¼­Ò³
    public void Turn_to_edit_shdz_page(View view) {
        LinearLayout old_layout = (LinearLayout)findViewById(R.id.shdz_page);
        LinearLayout new_layout = (LinearLayout)findViewById(R.id.edit_shdz_page);
        old_layout.setVisibility(View.GONE);
        new_layout.setVisibility(View.VISIBLE);
    }

    //·µ»ØÊÕ»õµØÖ·²é¿´Ò³
    public void edit_shdz_page_back(View view) {
        LinearLayout old_layout = (LinearLayout)findViewById(R.id.edit_shdz_page);
        LinearLayout new_layout = (LinearLayout)findViewById(R.id.shdz_page);
        old_layout.setVisibility(View.GONE);
        new_layout.setVisibility(View.VISIBLE);
    }

    public void edit_shdz_page_save(View view) {

    }
    //µØÖ·ÐÅÏ¢¼°Ïà¹Ø   end------------------------------------------------------------------

    //ҵ����
    //�����½�ҵ����ҳ
    public void Turn_to_add_ywjc_page(View view) {
        LinearLayout old_layout = (LinearLayout)findViewById(R.id.company_i);
        LinearLayout new_layout = (LinearLayout)findViewById(R.id.add_ywjc_page);
        old_layout.setVisibility(View.GONE);
        new_layout.setVisibility(View.VISIBLE);
    }

    //����������
    public void Ywjc_page_back(View view) {
        LinearLayout old_layout = (LinearLayout)findViewById(R.id.add_ywjc_page);
        LinearLayout new_layout = (LinearLayout)findViewById(R.id.company_i);
        old_layout.setVisibility(View.GONE);
        new_layout.setVisibility(View.VISIBLE);
    }

    //ҵ���̱���
    public void Ywjc_save(View view) {

    }

    //���ʷ
    //�����½����ʷҳ
    public void Turn_to_add_hdls_page(View view) {
        LinearLayout old_layout = (LinearLayout)findViewById(R.id.company_i);
        LinearLayout new_layout = (LinearLayout)findViewById(R.id.add_hdls_page);
        old_layout.setVisibility(View.GONE);
        new_layout.setVisibility(View.VISIBLE);
    }

    //����������
    public void Hdls_page_back(View view) {
        LinearLayout old_layout = (LinearLayout)findViewById(R.id.add_hdls_page);
        LinearLayout new_layout = (LinearLayout)findViewById(R.id.company_i);
        old_layout.setVisibility(View.GONE);
        new_layout.setVisibility(View.VISIBLE);
    }

    //���ʷ����
    public void Hdls_save(View view) {

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        //refreshList();
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ContactDBManager.company_deleteAll();
        ContactDBManager.closeDB();
    }

}
