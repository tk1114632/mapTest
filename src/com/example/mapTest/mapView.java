package com.example.mapTest;

/**
 * Created by tk1114632 on 11/3/14.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviPara;
import com.baidu.mapapi.utils.DistanceUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;





public class mapView extends Activity {
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    DistanceUtil distanceManager;

    MapView mMapView;
    BaiduMap mBaiduMap;
    private  ArrayList<ContactInfo> ContactList;
    private ArrayList<Marker> markerList;

    //数据库
    DBManager ContactDBManager = null;


    // UI相关
    //RadioGroup.OnCheckedChangeListener radioButtonListener;
    Button requestLocButton;
    Button displayNearContactButton;
    boolean isFirstLoc = false;// 是否首次定
    boolean isDisplayed = false; //是否已经显示附近联系人

    private InfoWindow mInfoWindow;
    private RelativeLayout ContactDetailInfo;

    BitmapDescriptor positionIcon = BitmapDescriptorFactory.fromResource(R.drawable.maker);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mapview);

        //填充ArrayList<ContactInfo>
        ContactList = new ArrayList<ContactInfo>();
        ContactDBManager = new DBManager(this);
        ContactList = ContactDBManager.queryAll();

        ArrayList<CompanyInfo> companyInDB = ContactDBManager.company_queryAll();
        ArrayList<ContactInfo_new> contactInDB = ContactDBManager.contact_queryAll();
        for (CompanyInfo thisInfo : companyInDB) {
            ContactInfo x = new ContactInfo();
            x.setName(thisInfo.getName());
            x.setCompany(thisInfo.getName());
            x.setAddress(thisInfo.getAddress());
            x.setTel(thisInfo.getPhone());
            x.setPositionLat(thisInfo.getPositionLat());
            x.setPositionLng(thisInfo.getPositionLng());
            x.setDb_id(thisInfo.getDb_id());
            Log.e("Address of"+x.getAddress()+ "========",""+ x.getPositionLat());
            ContactList.add(x);
        }


        distanceManager = new DistanceUtil();

        ContactDetailInfo = (RelativeLayout) this.findViewById(R.id.info_detail);

        //初始化地图
        requestLocButton = (Button) findViewById(R.id.button1);
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        requestLocButton.setText("");
        requestLocButton.setBackgroundResource(R.drawable.custom_loc);
        View.OnClickListener btnClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                mBaiduMap
                    .setMyLocationConfigeration(new MyLocationConfiguration(
                            mCurrentMode, true, mCurrentMarker));
                mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
                mBaiduMap
                        .setMyLocationConfigeration(new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker));

            }
        };
        requestLocButton.setOnClickListener(btnClickListener);


        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            // 当用intent参数时，设置中心点为指定点
            ContactInfo comeInInfo = (ContactInfo) intent.getExtras().getSerializable("selectedInfo");

            LatLng p = new LatLng(comeInInfo.getPositionLat(), comeInInfo.getPositionLng());
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(p)
                    .zoom(13)
                    .build();
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            mBaiduMap.setMapStatus(mMapStatusUpdate);
            popupInfo(ContactDetailInfo, comeInInfo);
            LatLng currentPosition = new LatLng(comeInInfo.getPositionLat(),comeInInfo.getPositionLng());
            OverlayOptions currentOption = new MarkerOptions()
                    .position(currentPosition)
                    .icon(positionIcon)
                    .zIndex(5);
            Marker marker = (Marker) mBaiduMap.addOverlay(currentOption);
            Bundle bundle = new Bundle();
            bundle.putSerializable("ContactInformation",comeInInfo);
            marker.setExtraInfo(bundle);
        } else {
            isFirstLoc = true;
            LatLng m = new LatLng(31.238788,121.481575);//上海中心坐标
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(m)
                    .zoom(11)
                    .build();
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            mBaiduMap.setMapStatus(mMapStatusUpdate);
        }

        //监听显示附近联系人
        displayNearContactButton = (Button) findViewById(R.id.button2);
        displayNearContactButton.setText("显示标记");
        View.OnClickListener btnClickListener2 = new View.OnClickListener() {
            public void onClick(View v) {
                if (!isDisplayed) {

                    for (ContactInfo currentContact : ContactList) {
                        LatLng currentPosition = new LatLng(currentContact.getPositionLat(),currentContact.getPositionLng());
                        OverlayOptions currentOption = new MarkerOptions()
                                .position(currentPosition)
                                .icon(positionIcon)
                                .zIndex(5);
                        Marker marker = (Marker) mBaiduMap.addOverlay(currentOption);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("ContactInformation",currentContact);
                        marker.setExtraInfo(bundle);
                    }

                    displayNearContactButton.setText("隐藏标记");
                    isDisplayed = true;
                }
                else {
                    mBaiduMap.clear();
                    isDisplayed = false;
                    displayNearContactButton.setText("显示标记");
                }
            }
        };
        displayNearContactButton.setOnClickListener(btnClickListener2);

        //设置marker点击响应
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                Button button = new Button(getApplicationContext());
                button.setBackgroundResource(R.drawable.location_tips);
                button.setWidth(200);
                InfoWindow.OnInfoWindowClickListener listener = null;
                //if (marker == marker1) {
                Bundle bundle = marker.getExtraInfo();
                final ContactInfo currentContact = (ContactInfo) bundle.get("ContactInformation");
                button.setText(currentContact.getName());
                button.setTextColor(Color.WHITE);


                listener = new InfoWindow.OnInfoWindowClickListener() {
                        public void onInfoWindowClick() {

                            mBaiduMap.hideInfoWindow();
                        }
                };
                LatLng ll = marker.getPosition();
                mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
                mBaiduMap.showInfoWindow(mInfoWindow);

                //显示详细信息
                popupInfo(ContactDetailInfo, currentContact);

                //}

                return true;
            }
        });
    }

    protected void popupInfo(final RelativeLayout mMarkerDetail, final ContactInfo info)
    {

        ContactDetailInfo.setVisibility(View.VISIBLE);
        ViewHolder CurrentDetail;
        if (mMarkerDetail.getTag() == null)
        {
            CurrentDetail = new ViewHolder();
            CurrentDetail.infoname = (TextView) mMarkerDetail.findViewById(R.id.info_name);
            CurrentDetail.infocompany = (TextView) mMarkerDetail.findViewById(R.id.info_company);
            CurrentDetail.infoaddr = (TextView) mMarkerDetail.findViewById(R.id.info_address);
            CurrentDetail.infotel = (TextView) mMarkerDetail.findViewById(R.id.info_tel);
            CurrentDetail.naviButton = (Button) mMarkerDetail.findViewById(R.id.navigation_button);
            mMarkerDetail.setTag(CurrentDetail);
        }
        CurrentDetail = (ViewHolder) mMarkerDetail.getTag();
        CurrentDetail.infoname.setText("姓名："+info.getName());
        CurrentDetail.infocompany.setText("公司："+info.getCompany());
        CurrentDetail.infoaddr.setText("地址："+info.getAddress());
        CurrentDetail.infotel.setText("电话："+info.tel);
        View.OnClickListener NaviButtonListener = new View.OnClickListener(){
            public void onClick(View v){
                //导航。默认客户端，如果没有则用web
                startNavi(mMapView, info);
                mBaiduMap.hideInfoWindow();
                mMarkerDetail.setVisibility(View.GONE);
            }
        };
        CurrentDetail.naviButton.setOnClickListener(NaviButtonListener);

        //地图单击事件
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMarkerDetail.setVisibility(View.GONE);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    //导航函数
    public void startNavi(View view, ContactInfo currentInfo) {
        LatLng pt1 = new LatLng(mBaiduMap.getLocationData().latitude, mBaiduMap.getLocationData().longitude);
        LatLng pt2 = new LatLng(currentInfo.getPositionLat(), currentInfo.getPositionLng());
        // 构建 导航参数
        NaviPara para = new NaviPara();
        para.startPoint = pt1;
        para.startName = "从这里开始";
        para.endPoint = pt2;
        para.endName = "到这里结束";

        try {

            BaiduMapNavigation.openBaiduMapNavi(para, this);

        } catch (BaiduMapAppNotSupportNaviException e) {
            startWebNavi(view,pt1,pt2);
        }
    }

    public void startWebNavi(View view, LatLng p1, LatLng p2) {
        LatLng pt1 = p1;
        LatLng pt2 = p2;
        // 构建 导航
        NaviPara para = new NaviPara();
        para.startPoint = pt1;
        para.endPoint = pt2;
        BaiduMapNavigation.openWebBaiduMapNavi(para, this);
    }


    public class ViewHolder
    {
        TextView infoname;
        TextView infoaddr;
        TextView infotel;
        TextView infocompany;
        Button naviButton;
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }
            for (ContactInfo currentPerson : ContactList){
                LatLng currentLatlng = new LatLng(currentPerson.getPositionLat(),currentPerson.getPositionLng());
                LatLng myLatlng = new LatLng(locData.latitude,locData.longitude);
                int distance = (int) distanceManager.getDistance(currentLatlng,myLatlng);
                currentPerson.setDistance(distance);
                //Log.e("Distance of " + currentPerson.getName()+" :", currentPerson.getDistance()+" id: " + currentPerson.getDb_id()+" ================");
            }

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    public void rankList(ArrayList<ContactInfo> thisList) {
        Comparator<ContactInfo> contactDistanceComparator = new Comparator<ContactInfo>() {
            public int compare(ContactInfo s1, ContactInfo s2) {
                if (s1.getDistance() != s2.getDistance()) {
                    double result = s1.getDistance() - s2.getDistance();
                    return (int) result;
                } else {
                    //年龄相同则按姓名排序
                    if (!s1.getName().equals(s2.getName())) {
                        return s2.getName().compareTo(s1.getName());
                    } else {
                        //姓名也相同则按学号排序
                        return s2.getCompany().compareTo(s1.getCompany());
                    }
                }
            }
        };
        Collections.sort(thisList,contactDistanceComparator);
    }

    public void contactListButtonOnClick(View view){
        rankList(ContactList);
        Context context = getApplicationContext();
        final ListView myListView = (ListView) findViewById(R.id.poplistView);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactList.get(position).getDb_id();

            }
        });
        popListAdapter adapter = new popListAdapter(context,ContactList);
        myListView.setAdapter(adapter);
        if (myListView.getVisibility() == View.VISIBLE) {
            myListView.setVisibility(View.GONE);
        }
        else {
            myListView.setVisibility(View.VISIBLE);
        }
        //地图单击事件
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                myListView.setVisibility(View.GONE);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    public void back_to_contact(View view) {
        this.finish();
    }
    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

}
