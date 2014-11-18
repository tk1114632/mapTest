package com.example.mapTest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.*;

import java.util.ArrayList;

public class newContact extends Activity implements
        OnGetGeoCoderResultListener {
    /**
     * Called when the activity is first created.
     */
    DBManager ContactDBManager = null;
    GeoCoder mSearch = null;
    ContactInfo currentContact = new ContactInfo();//新建一个contactinfo的实例，存以上数据
    ArrayList<ContactInfo> currentList = new ArrayList<ContactInfo>();//新建一个arraylist存放currentContact

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newcontact);



        //初始化DBManager
        ContactDBManager = new DBManager(this);

        ContactInfo currentContact = new ContactInfo();//新建一个contactinfo的实例，存以上数据
        ArrayList<ContactInfo> currentList = new ArrayList<ContactInfo>();//新建一个arraylist存放currentContact

        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);

        Button cancelButton = (Button)this.findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newContact.this.finish();
            }
        });
    }

    public void DoneButton(View view){

        EditText name = (EditText)findViewById(R.id.editText1);
        EditText phoneNumber  = (EditText)findViewById(R.id.editText2);
        EditText address  = (EditText)findViewById(R.id.editText3);
        EditText company  = (EditText)findViewById(R.id.editText4);
        EditText notes  = (EditText)findViewById(R.id.editText5);


        currentContact.setName(name.getText().toString());
        currentContact.setCompany(company.getText().toString());
        currentContact.setAddress(address.getText().toString());
        currentContact.setTel(phoneNumber.getText().toString());

        // Geo搜索
        mSearch.geocode(new GeoCodeOption().city(
                "上海").address(
                currentContact.getAddress()));


    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(newContact.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            currentList.add(currentContact);//添加到ArrayList里面
            //调用数据库方法addPerson
            ContactDBManager.add_person(currentList);
            newContact.this.finish();
            return;
        }
        String strInfo = String.format("纬度：%f 经度：%f",
                result.getLocation().latitude, result.getLocation().longitude);
        Toast.makeText(newContact.this, strInfo, Toast.LENGTH_LONG).show();

        //如果有数据返回，将经纬度加到currentContact里面
        currentContact.setPositionLat(result.getLocation().latitude);
        currentContact.setPositionLng(result.getLocation().longitude);
        currentList.add(currentContact);//添加到ArrayList里面
        //调用数据库方法addPerson
        ContactDBManager.add_person(currentList);

        newContact.this.finish();
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(newContact.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        Toast.makeText(newContact.this, result.getAddress(),
                Toast.LENGTH_LONG).show();

    }
}
