package com.example.mapTest;

/**
 * Created by tk1114632 on 11/3/14.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.TextView;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.List;




public class mapView extends Activity {
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;

    MapView mMapView;
    BaiduMap mBaiduMap;
    private  ArrayList<ContactInfo> ContactList;
    private ArrayList<Marker> markerList;


    // UI相关
    //RadioGroup.OnCheckedChangeListener radioButtonListener;
    Button requestLocButton;
    Button displayNearContactButton;
    boolean isFirstLoc = true;// 是否首次定位
    boolean isDisplayed = false; //是否已经显示附近联系人

    private Marker marker1;
    private Marker marker2;

    private InfoWindow mInfoWindow;
    private RelativeLayout ContactDetailInfo;

    LatLng testPoint = new LatLng(31.239788,121.581575);
    LatLng testPoint2 = new LatLng(31.239381,121.48575);
    BitmapDescriptor positionIcon = BitmapDescriptorFactory.fromResource(R.drawable.maker);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mapview);

        //填充ArrayList<ContactInfo>
        ContactList = new ArrayList<ContactInfo>();
        ContactInfo temp = new ContactInfo("上海交大","宋明亨","上海市闵行区东川路800号",31.030579,121.435007,"18916924886");
        ContactList.add(temp);
        temp = new ContactInfo("Logic Solutions", "Vincent","上海市浦东新区博霞路50号",31.205939,121.609884,"13549998877");
        ContactList.add(temp);
        temp = new ContactInfo("人民广场", "人民","上海市人民广场",31.238802,121.481033,"110");
        ContactList.add(temp);

        ContactDetailInfo = (RelativeLayout) findViewById(R.id.info_detail);

        //初始化地图
        requestLocButton = (Button) findViewById(R.id.button1);
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        requestLocButton.setText("我的位置");
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
        if (intent.hasExtra("x") && intent.hasExtra("y")) {
            // 当用intent参数时，设置中心点为指定点
            Bundle b = intent.getExtras();
            LatLng p = new LatLng(b.getDouble("y"), b.getDouble("x"));
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(p)
                    .zoom(13)
                    .build();
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            mBaiduMap.setMapStatus(mMapStatusUpdate);
            /*mMapView = new MapView(this,
                    new BaiduMapOptions().mapStatus(new MapStatus.Builder()
                            .target(p).build()));*/
        } else {
            LatLng m = new LatLng(31.238788,121.481575);//上海中心坐标
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(m)
                    .zoom(13)
                    .build();
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            mBaiduMap.setMapStatus(mMapStatusUpdate);
        }

        //监听显示附近联系人
        displayNearContactButton = (Button) findViewById(R.id.button2);
        displayNearContactButton.setText("显示联系人");
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

                    displayNearContactButton.setText("隐藏联系人");
                    isDisplayed = true;
                }
                else {
                    mBaiduMap.clear();
                    isDisplayed = false;
                    displayNearContactButton.setText("显示联系人");
                }
            }
        };
        displayNearContactButton.setOnClickListener(btnClickListener2);

        //设置marker点击响应
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                Button button = new Button(getApplicationContext());
                button.setBackgroundResource(R.drawable.location_tips);
                button.setWidth(250);
                InfoWindow.OnInfoWindowClickListener listener = null;
                //if (marker == marker1) {
                Bundle bundle = marker.getExtraInfo();
                final ContactInfo currentContact = (ContactInfo) bundle.get("ContactInformation");
                button.setText(currentContact.getName());
                button.setTextColor(Color.WHITE);

                //弹出信息
                /*Toast.makeText(mapView.this,
                     "公司：" + currentContact.getCompany() + "\n" +
                            "地址：" + currentContact.getAddress(),
                     Toast.LENGTH_LONG).show();*/

                listener = new InfoWindow.OnInfoWindowClickListener() {
                        public void onInfoWindowClick() {

                            mBaiduMap.hideInfoWindow();
                        }
                };
                LatLng ll = marker.getPosition();
                mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
                mBaiduMap.showInfoWindow(mInfoWindow);

                //显示详细信息
                ContactDetailInfo.setVisibility(View.VISIBLE);
                popupInfo(ContactDetailInfo, currentContact);

                //}

                return true;
            }
        });
    }

    protected void popupInfo(final RelativeLayout mMarkerDetail, final ContactInfo info)
    {
        ViewHolder CurrentDetail = null;
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
        // 构建 导航参数
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
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
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
