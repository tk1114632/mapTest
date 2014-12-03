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
import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class main extends Activity {
    //数据库ContactDB
    DBManager ContactDBManager;
    ListView list1;
    ArrayList<CompanyInfo> contentInDB;
    int currentCompanyDBiD = 0;
    View lastItemSelectedView;
    View currentViewCompany;
    View currentViewContact;
    int lastItemPosition;
    boolean companyMode;




    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_company);

        ContactDBManager = new DBManager(this);

        list1 = (ListView) findViewById(R.id.companies_List);
        contentInDB = ContactDBManager.company_queryAll();
        Context context = getApplicationContext();
        companyListAdapter arrayAdapter = new companyListAdapter(context, contentInDB);
        list1.setAdapter(arrayAdapter);
        refreshCompanyDetailDisplay();
        currentViewContact = findViewById(R.id.display_contact_info_page);
        companyMode = true;


        //短按
        list1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentCompanyDBiD = contentInDB.get(position).getDb_id();
                Log.e("","DBID ========"+contentInDB.get(position).getDb_id());

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
                new AlertDialog.Builder(main.this)
                        .setTitle("删除联系人")
                        .setMessage("确定删除"+contentInDB.get(position).getName()+"吗?")
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
        contentInDB = ContactDBManager.company_queryAll();
        Context context = getApplicationContext();
        companyListAdapter arrayAdapter = new companyListAdapter(context, contentInDB);
        list1.setAdapter(arrayAdapter);
    }
    public void refreshCompanyDetailDisplay() {
        View newView = findViewById(R.id.company_i);
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
            Log.e("=========","=="+currentCompany.getName());
            CompanyName.setText(currentCompany.getName());
            WebAddr.setText(currentCompany.getWebsite());
            Tel.setText(currentCompany.getPhone());
            Addr.setText(currentCompany.getAddress());
            Field.setText(currentCompany.getField());


        }
        else {
            TextView CompanyName = (TextView) findViewById(R.id.company_name);
            TextView WebAddr = (TextView) findViewById(R.id.web_address_content);
            TextView Tel = (TextView) findViewById(R.id.phone_number_content);
            TextView Addr = (TextView) findViewById(R.id.eddasd);
            TextView Field = (TextView) findViewById(R.id.education_type_content);

            CompanyName.setText("公司信息");
            WebAddr.setText("暂无");
            Tel.setText("暂无");
            Addr.setText("暂无");
            Field.setText("暂无");
        }
    }

    public void refreshCompanyButton(View view) {
        refreshCompanyList();
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

            EditText lastname = (EditText) findViewById(R.id.xjlxr_xs_input);
            EditText firstname = (EditText) findViewById(R.id.xjlxr_xs_input);
            EditText zw = (EditText) findViewById(R.id.xjlxr_zw_input);
            EditText gzyx= (EditText) findViewById(R.id.xjlxr_dzyj_gz_input);
            EditText sryx = (EditText) findViewById(R.id.xjlxr_dzyj_sr_input);
            EditText phone = (EditText) findViewById(R.id.xjlxr_sj_input);
            EditText tel_gz = (EditText) findViewById(R.id.xjlxr_dh_gz_input);
            EditText tel_sr = (EditText) findViewById(R.id.xjlxr_dh_sr_input);




            View newView = findViewById(R.id.add_contact_fromContact);
            if (currentViewContact != null) {
                currentViewContact.setVisibility(View.GONE);
            }
            newView.setVisibility(View.VISIBLE);
            currentViewContact = newView;
        }

    }
    //����������
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

    //�����༭��˾ҳ
    public void Turn_to_edit_co_page(View view) {
        if (currentCompanyDBiD > 0) {
            TextView CompanyName = (TextView) findViewById(R.id.edit_co_page_companyName);
            TextView WebAddr = (TextView) findViewById(R.id.edit_co_page_webSite);
            TextView Tel = (TextView) findViewById(R.id.phone_number_content);
            TextView Addr = (TextView) findViewById(R.id.eddasd);
            TextView Field = (TextView) findViewById(R.id.education_type_content);

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
    //����������
    public void edit_co_page_back(View view) {
        View newView = findViewById(R.id.company_i);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
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
        View newView = findViewById(R.id.kddz_page);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }
    //·µ»ØÖ÷½çÃæ
    public void Kddz_page_back(View view) {
        View newView = findViewById(R.id.company_i);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }
    //ÌøÖÁ¿ªµ¥µØÖ·±à¼­Ò³
    public void Turn_to_edit_kddz_page(View view) {
        View newView = findViewById(R.id.edit_kddz_page);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }
    //·µ»Ø¿ªµ¥µØÖ·²é¿´Ò³
    public void edit_kddz_page_back(View view) {
        View newView = findViewById(R.id.kddz_page);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }
    //±£´æ±à¼­¿ªµ¥µØÖ·
    public void edit_kddz_page_save(View view) {
        View newView = findViewById(R.id.kddz_page);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;

    }

    //ÌøÖÁÊÕ»õµØÖ·²é¿´Ò³
    public void Turn_to_shdz_page(View view) {
        View newView = findViewById(R.id.shdz_page);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }

    //·µ»ØÖ÷½çÃæ
    public void Shdz_page_back(View view) {
        View newView = findViewById(R.id.company_i);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }
    //ÌøÖÁÊÕ»õµØÖ·±à¼­Ò³
    public void Turn_to_edit_shdz_page(View view) {
        View newView = findViewById(R.id.edit_shdz_page);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }

    //·µ»ØÊÕ»õµØÖ·²é¿´Ò³
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
    //µØÖ·ÐÅÏ¢¼°Ïà¹Ø   end------------------------------------------------------------------
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
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }
    //ҵ����
    //�����½�ҵ����ҳ
    public void Turn_to_add_ywjc_page(View view) {
        View newView = findViewById(R.id.add_ywjc_page);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }

    //����������
    public void Ywjc_page_back(View view) {
        View newView = findViewById(R.id.company_i);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }

    //ҵ���̱���
    public void Ywjc_save(View view) {
        View newView = findViewById(R.id.company_i);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }

    //���ʷ
    //�����½����ʷҳ
    public void Turn_to_add_hdls_page(View view) {
        View newView = findViewById(R.id.add_hdls_page);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }

    //����������
    public void Hdls_page_back(View view) {
        View newView = findViewById(R.id.company_i);
        if (currentViewCompany != null) {
            currentViewCompany.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewCompany = newView;
    }

    //���ʷ����
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
        if (currentViewContact != null) {
            currentViewContact.setVisibility(View.GONE);
        }
        newView.setVisibility(view.VISIBLE);
        currentViewContact = newView;
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
    }

}
