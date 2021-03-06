package com.odfbox;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.odfbox.activity.BaseActivity;
import com.odfbox.activity.BoxDetailActivityNew;
import com.odfbox.activity.MineActivity;
import com.odfbox.activity.OpenGuideActivity;
import com.odfbox.activity.SearchBoxNewActivity;
import com.odfbox.activity.WarnListActivity;
import com.odfbox.entity.BoxList;
import com.odfbox.entity.Odfbox;
import com.odfbox.entity.VersionOdfbox;
import com.odfbox.entity.WarnsList;
import com.odfbox.handle.BoxListHandler;
import com.odfbox.handle.CommentHandler;
import com.odfbox.handle.VersionHandler;
import com.odfbox.handle.WarnHandler;
import com.odfbox.utils.BoxUtils;
import com.odfbox.utils.StringUtils;
import com.odfbox.utils.UpdateCheckUtils;
import com.odfbox.views.DialogFactory;
import com.odfbox.zxing.activity.CaptureActivity;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends BaseActivity {


    MapStatus mapStatus = null;
    public MapView mMapView;
    public BaiduMap mBaiduMap;
    public int SETLOCATIONCODE = 12;
    public LocationClient mLocClient;
    public boolean isFirstLoc = true;
    public boolean visible = true;
    BDLocation mLocation = null;
    public boolean ifOpen = true;
    public float zoom = 16;
    BitmapDescriptor warnbitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_marka_warn);
    BitmapDescriptor markabitmap = BitmapDescriptorFactory.fromResource(R.mipmap.icon_marka);
    SharedPreferences preferences;
    @Bind(R.id.mapview)
    MapView mapview;
    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.ll_warns_title)
    RelativeLayout llWarnsTitle;
    @Bind(R.id.text1)
    TextView text1;
    @Bind(R.id.text1_time)
    TextView text1Time;
    @Bind(R.id.text2)
    TextView text2;
    @Bind(R.id.text2_time)
    TextView text2Time;
    @Bind(R.id.text3)
    TextView text3;
    @Bind(R.id.text3_time)
    TextView text3Time;
    @Bind(R.id.ll_warns)
    RelativeLayout llWarns;
    @Bind(R.id.warn_layout)
    RelativeLayout warnLayout;
    @Bind(R.id.address)
    TextView address;
    @Bind(R.id.detail)
    TextView detail;
    @Bind(R.id.box_image)
    ImageView boxImage;
    @Bind(R.id.code)
    TextView code;
    @Bind(R.id.caizhi)
    TextView caizhi;
    @Bind(R.id.code1)
    TextView code1;
    @Bind(R.id.content)
    TextView content;
    @Bind(R.id.phone)
    TextView phone;
    @Bind(R.id.open)
    Button open;
    @Bind(R.id.openes)
    RelativeLayout openes;
    @Bind(R.id.refresh)
    ImageView refresh;
    @Bind(R.id.back_current)
    ImageView backCurrent;
    @Bind(R.id.sacn_openlock)
    ImageView openLock;
    @Bind(R.id.rl_text1)
    RelativeLayout rlText1;
    @Bind(R.id.rl_text2)
    RelativeLayout rlText2;
    @Bind(R.id.rl_text3)
    RelativeLayout rlText3;
    @Bind(R.id.iamge1)
    ImageView iamge1;
    @Bind(R.id.iamge2)
    ImageView iamge2;
    @Bind(R.id.door)
    TextView door;
    @Bind(R.id.type)
    TextView type;
    private Dialog use;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        OdfboxApplication.addActivity(this);
        getPemmission();
        getVersion();
        preferences = mContext.getSharedPreferences("CHANNELID", Context.MODE_PRIVATE);
//        dialog = DialogFactory.getDialogFactory().showCommonDialog(mContext, preferences.getString("channelid", "未取到"), "取消", "确定", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        }, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                login();
//                dialog.dismiss();
//            }
//        });
        login();
        setTitleTextView("物联网光交箱", null);
        timer.start();
        mMapView = (MapView) findViewById(R.id.mapview);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);

        llWarns.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.VISIBLE);
        backCurrent.setVisibility(View.VISIBLE);
        getEventList();
        setRightImage(R.mipmap.ic_white_search, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivityForResult(new Intent(mContext, SetComLocationActivity.class), SETLOCATIONCODE);
                startActivity(new Intent(mContext, SearchBoxNewActivity.class));
            }
        });
        setLeftTextView(R.mipmap.ic_menu, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MineActivity.class));
            }
        });

        // 定位初始化
        mLocClient = new LocationClient(mContext);
        mLocClient.registerLocationListener(new MyLocationListenner());
        final LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        LatLng ll = new LatLng(100, 100);
        final MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(zoom);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        warnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ifOpen) {
                    llWarns.setVisibility(View.GONE);
                    llWarnsTitle.setVisibility(View.VISIBLE);
                    iamge1.setVisibility(View.GONE);
                    iamge2.setVisibility(View.GONE);
                    ifOpen = false;
                } else {
                    llWarns.setVisibility(View.VISIBLE);
                    llWarnsTitle.setVisibility(View.GONE);
                    iamge1.setVisibility(View.VISIBLE);
                    iamge2.setVisibility(View.VISIBLE);
                    ifOpen = true;
                }
            }
        });
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, WarnListActivity.class));
            }
        });
        backCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLocation != null) {
                    MapStatus mMapStatus = new MapStatus.Builder()
                            .target(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()))
                            .build();
                    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                    mBaiduMap.animateMapStatus(mMapStatusUpdate);
                }

            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLocation != null && mapStatus != null) {
                    getList(mLocation.getLongitude() + "", mLocation.getLatitude() + "", BoxUtils.getZoom(BoxUtils.getZoom((int) mapStatus.zoom)) + "");
                }

            }
        });


        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChangeStart(MapStatus status) {

            }

            // 移动结束，在这里需要计算出中心点坐标
            @Override
            public void onMapStatusChangeFinish(final MapStatus status) {
                mapStatus = status;
                final GeoCoder geoCoder = GeoCoder.newInstance();
                geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(status.target));
                geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

                    }

                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                        String adress = reverseGeoCodeResult.getAddress();
                        if (!TextUtils.isEmpty(adress)) {
                            showToast(reverseGeoCodeResult.getAddress() + "");
                            getList(reverseGeoCodeResult.getLocation().longitude + "", reverseGeoCodeResult.getLocation().latitude + "", BoxUtils.getZoom((int) status.zoom) + "");
                        }
                    }
                });
            }

            @Override
            public void onMapStatusChange(MapStatus status) {
            }
        });

        //地图点击事件
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public boolean onMapPoiClick(MapPoi arg0) {
                return false;
            }

            @Override
            public void onMapClick(LatLng arg0) {
                openes.setVisibility(View.GONE);
            }
        });
        //光交箱图标点击事件
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng latLng = marker.getPosition();
                MapStatus mMapStatus = new MapStatus.Builder()
                        .target(latLng)
                        .build();
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                mBaiduMap.animateMapStatus(mMapStatusUpdate);
                final Bundle bundle = marker.getExtraInfo();
                final Odfbox odfbox;
                if (bundle != null) {
                    odfbox = (Odfbox) bundle.getSerializable("info");
                } else {
                    return true;
                }
                openes.setVisibility(View.VISIBLE);
                address.setText(odfbox.address);
                if (odfbox.smart_lock != null) {
                    code.setText("终端编号：" + odfbox.smart_lock.serial_no);
                } else {
                    code.setText("终端编号：----");
                }

                if (odfbox.model != null) {
                    type.setText("型号：" + odfbox.model);
                } else {
                    type.setText("型号：----");
                }

                if (odfbox.door_type != null) {
                    door.setText("门数：" + odfbox.door_type);
                } else {
                    door.setText("门数： ----");
                }

                if (odfbox.material != null) {
                    caizhi.setText("材质：" + odfbox.material);
                } else {
                    caizhi.setText("材质：----");
                }
                if (odfbox.business_capacity != null) {
                    content.setText("光交箱容量：" + odfbox.business_capacity);
                } else {
                    content.setText("光交箱容量：----");
                }


                if (odfbox.picture != null && odfbox.picture.url != null) {
                    ImageLoader.getInstance().displayImage("http:" + odfbox.picture.url + "", boxImage);
                }

                detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtras(bundle);
                        intent.setClass(mContext, BoxDetailActivityNew.class);
                        startActivity(intent);
                    }
                });

                open.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        use = DialogFactory.getDialogFactory().showCommonDialog(mContext, "确定开锁？", "取消", "确定", new View.OnClickListener() {
                            @SuppressWarnings("unused")
                            @Override
                            public void onClick(View v) {
                                use.dismiss();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (use != null && use.isShowing()) {
                                    use.dismiss();
                                    if (odfbox.smart_lock != null) {
                                        showProgress(0, true);
                                        JSONObject params = new JSONObject();
                                        params.put("command", "unlock");
                                        params.put("lock_pk", odfbox.id);
                                        try {
                                            StringEntity entity = new StringEntity(params.toString(), "UTF-8");
                                            client.getOpenBox(mContext, odfbox.smart_lock.id + "", entity, new CommentHandler() {
                                                @Override
                                                public void onSuccess(String commen) {
                                                    super.onSuccess(commen);
                                                    cancelmDialog();
                                                    Intent intent = new Intent(mContext, OpenGuideActivity.class);
                                                    intent.putExtra("id", odfbox.id + "");
                                                    startActivity(intent);
                                                }

                                                @Override
                                                public void onFailure(String msg) {
                                                    super.onFailure(msg);
                                                    cancelmDialog();
                                                }

                                                @Override
                                                public void onCancel() {
                                                    super.onCancel();
                                                    cancelmDialog();
                                                }
                                            });
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        showToast("未能获得智能锁信息，请联系管理员！");
                                    }


                                }
                            }
                        });
                    }
                });

                return true;
            }
        });

        openLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, CaptureActivity.class), 5);
            }
        });

    }


    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            if (location == null || mMapView == null) {
                return;
            }
            mLocation = location;
            mBaiduMap.clear();
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            final GeoCoder geoCoder = GeoCoder.newInstance();
            geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(location.getLatitude(), location.getLongitude())));
            geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                @Override
                public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                }

                @Override
                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                    String adress = reverseGeoCodeResult.getAddress();
                    if (!TextUtils.isEmpty(adress)) {
                        prefs.saveCurrentAddress(location.getLongitude() + "", location.getLatitude() + "", adress);
                    }
                }
            });
            mBaiduMap.setMyLocationData(locData);

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(zoom);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
            getList(location.getLongitude() + "", location.getLatitude() + "", 2000 + "");
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == 5) {
                if (!StringUtils.isEmpty(data.getStringExtra("code"))) {
                    getList(data.getStringExtra("code"));
                }
            } else {
                final GeoCoder geoCoder = GeoCoder.newInstance();
                geoCoder.geocode(new GeoCodeOption().city("昆明").address(data.getStringExtra("address")));
                geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                        mBaiduMap.clear();
                        getList(geoCodeResult.getLocation().longitude + "", geoCodeResult.getLocation().latitude + "", "2000");
                        MapStatus mMapStatus = new MapStatus.Builder()
                                .target(new LatLng(geoCodeResult.getLocation().latitude, geoCodeResult.getLocation().longitude))
                                .build();
                        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                        mBaiduMap.animateMapStatus(mMapStatusUpdate);
                    }

                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

                    }
                });
            }

        }
    }

    public void getList(String string) {
        cancelmDialog();
        showProgress(0, true);
        client.getBoxList(mContext, "0", "&lock_serial=" + string, new BoxListHandler() {
            @Override
            public void onCancel() {
                super.onCancel();
                cancelmDialog();
            }

            @Override
            public void onSuccess(BoxList boxList) {
                super.onSuccess(boxList);
                cancelmDialog();
                if (boxList.results.size() != 0) {
                    LatLng latLng = new LatLng(boxList.results.get(0).latitude_baidu, boxList.results.get(0).longitude_baidu);
                    MapStatus mMapStatus = new MapStatus.Builder()
                            .target(latLng)
                            .build();
                    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                    mBaiduMap.animateMapStatus(mMapStatusUpdate);
                } else {
                    showToast("没有找到符合要求的光交箱");
                }

            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                cancelmDialog();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        warnbitmap.recycle();
        markabitmap.recycle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        openes.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    public void getList(String lat, String lon, String distance) {

        client.getNearBox(mContext, lat, lon, distance, new BoxListHandler() {
            @Override
            public void onSuccess(BoxList commen) {
                super.onSuccess(commen);
                cancelmDialog();
                Log.d("Reqeust:", "Reqeust: " + commen.results.toString());
                if (commen.results.size() != 0) {
                    for (int i = 0; i < commen.results.size(); i++) {
                        //定义Maker坐标点
                        final Odfbox odfbox = commen.results.get(i);
                        LatLng point = new LatLng(odfbox.latitude_baidu, odfbox.longitude_baidu);
                        //构建Marker图标
                        BitmapDescriptor bitmap;
                        if (odfbox.alarming) {
                            bitmap = warnbitmap;
                        } else {
                            bitmap = markabitmap;
                        }
                        //构建MarkerOption，用于在地图上添加Marker
                        OverlayOptions option = new MarkerOptions()
                                .position(point)
                                .icon(bitmap);
                        //在地图上添加Marker，并显示
                        Marker marker = (Marker) mBaiduMap.addOverlay(option);
                        //使用marker携带info信息，当点击事件的时候可以通过marker获得info信息
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("info", odfbox);
                        marker.setExtraInfo(bundle);

                    }
                } else {
                    showToast("当前位置附近没有光交箱！");
                }

            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                cancelmDialog();
            }

            @Override
            public void onCancel() {
                super.onCancel();
                cancelmDialog();
            }
        });
    }

    public void login() {
        JSONObject object = new JSONObject();
        object.put("action", "login");
        object.put("platform", "Android");
        object.put("channel_id_baidu", preferences.getString("channelid", ""));
        try {
            StringEntity entity = new StringEntity(object.toString(), "UTF-8");
            client.getLoginState(mContext, entity, new CommentHandler());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public void getEventList() {
        client.getMainWarnList(mContext, new WarnHandler() {
            @Override
            public void onSuccess(final WarnsList commen) {
                super.onSuccess(commen);
                int size = commen.results.size();
                if (size > 2) {
                    warnLayout.setVisibility(View.VISIBLE);
                    text1.setText(commen.results.get(0).event_text);
                    text.setText(commen.results.get(0).event_text);
                    text1Time.setText(commen.results.get(0).time);
                    text2.setText(commen.results.get(1).event_text);
                    text2Time.setText(commen.results.get(1).time);
                    text3.setText(commen.results.get(2).event_text);
                    text3Time.setText(commen.results.get(2).time);
                    rlText1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (commen.results.get(0).position != null) {
                                MapStatus mMapStatus = new MapStatus.Builder()
                                        .target(new LatLng(commen.results.get(0).position.latitude_baidu, commen.results.get(0).position.longitude_baidu))
                                        .build();
                                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                                mBaiduMap.animateMapStatus(mMapStatusUpdate);
                            } else {
                                showToast("没有找到对应的光交箱");
                            }
                        }
                    });

                    rlText2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (commen.results.get(1).position != null) {
                                MapStatus mMapStatus = new MapStatus.Builder()
                                        .target(new LatLng(commen.results.get(1).position.latitude_baidu, commen.results.get(1).position.longitude_baidu))
                                        .build();
                                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                                mBaiduMap.animateMapStatus(mMapStatusUpdate);
                            } else {
                                showToast("没有找到对应的光交箱");
                            }
                        }
                    });

                    rlText3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (commen.results.get(2).position != null) {
                                MapStatus mMapStatus = new MapStatus.Builder()
                                        .target(new LatLng(commen.results.get(2).position.latitude_baidu, commen.results.get(2).position.longitude_baidu))
                                        .build();
                                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                                mBaiduMap.animateMapStatus(mMapStatusUpdate);
                            } else {
                                showToast("没有找到对应的光交箱");
                            }
                        }
                    });


                } else {
                    warnLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    public void getPemmission() {


        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
        } else if (ContextCompat.checkSelfPermission(this, ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_NETWORK_STATE}, 3);
        } else if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, 5);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 6);
        }
    }


    CountDownTimer timer = new CountDownTimer(6000 * 1000, 5000) {
        @Override
        public void onTick(long millisUntilFinished) {
            getEventList();
        }

        @Override
        public void onFinish() {
            timer.start();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != 6) {
            getPemmission();
        }

    }


    public void getVersion() {
        client.getVersion(mContext, new VersionHandler() {
            @Override
            public void onSuccess(VersionOdfbox commen) {
                super.onSuccess(commen);
                if (commen.results.get(0) != null) {
                    if (commen.results.get(0).revision.contains("v") || commen.results.get(0).revision.contains("V")) {
                        if (BoxUtils.getIsUpdate(OdfboxApplication.getAppVersions(), commen.results.get(0).revision.substring(1, commen.results.get(0).revision.length()))) {
                            UpdateCheckUtils.getInstanse().lookVersion(MainActivity.this, true, commen);
                        }
                    } else {
                        if (BoxUtils.getIsUpdate(OdfboxApplication.getAppVersions(), commen.results.get(0).revision.substring(0, commen.results.get(0).revision.length()))) {
                            UpdateCheckUtils.getInstanse().lookVersion(MainActivity.this, true, commen);
                        }
                    }

                }
            }
        });
    }


}
