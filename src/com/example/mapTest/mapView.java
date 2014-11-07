package com.example.mapTest;

/**
 * Created by tk1114632 on 11/3/14.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.map.MapView;

import java.util.ArrayList;
class Car {
    public int chepai;
    public String leixing;
}


public class mapView extends Activity {
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;

    MapView mMapView;
    BaiduMap mBaiduMap;
    private   ArrayList<String> ss;
    private  ArrayList<Car> cars;



    // UI相关
    //RadioGroup.OnCheckedChangeListener radioButtonListener;
    Button requestLocButton;
    Button displayNearContactButton;
    boolean isFirstLoc = false;// 是否首次定位
    boolean isDisplayed = false; //是否已经显示附近联系人

    private Marker marker1;
    private Marker marker2;
    private InfoWindow mInfoWindow;

    LatLng testPoint = new LatLng(31.239788,121.581575);
    LatLng testPoint2 = new LatLng(31.239381,121.48575);
    BitmapDescriptor positionIcon = BitmapDescriptorFactory.fromResource(R.drawable.bubble_pink);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mapview);

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
                    OverlayOptions option1 = new MarkerOptions()
                            .position(testPoint)
                            .icon(positionIcon)
                            .title("测试地点")
                            .zIndex(1);
                    marker1 = (Marker) mBaiduMap.addOverlay(option1);
                    OverlayOptions option2 = new MarkerOptions()
                            .position(testPoint2)
                            .icon(positionIcon);
                    marker2 = (Marker) mBaiduMap.addOverlay(option2);
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
                button.setBackgroundResource(R.drawable.popup);
                InfoWindow.OnInfoWindowClickListener listener = null;
                if (marker == marker1) {
                    button.setText("宋明亨");
                    listener = new InfoWindow.OnInfoWindowClickListener() {
                        public void onInfoWindowClick() {
                            Toast.makeText(
                                    mapView.this,
                                    "公司：上海交大  " +
                                            "地址：上海市闵行区东川路800号",
                                    Toast.LENGTH_LONG).show();
                            mBaiduMap.hideInfoWindow();
                        }
                    };
                    LatLng ll = marker.getPosition();
                    mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
                    mBaiduMap.showInfoWindow(mInfoWindow);
                }
                return true;
            }
        });

        ss = new ArrayList<String>();
        ss.add(100, "Henry");
        ss.add("ssss");

        ss.clear();
        cars = new ArrayList<Car>();
        Car car1;
        car1 = new Car();
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
