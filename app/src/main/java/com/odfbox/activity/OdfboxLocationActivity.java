package com.odfbox.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
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
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.odfbox.OdfboxApplication;
import com.odfbox.R;
import com.odfbox.entity.Odfbox;
import com.odfbox.handle.CommentHandler;
import com.odfbox.views.DialogFactory;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by admin on 2017/8/7.
 */

public class OdfboxLocationActivity extends BaseActivity {

    @Bind(R.id.text1)
    TextView text1;
    @Bind(R.id.text2)
    TextView text2;
    @Bind(R.id.text3)
    TextView text3;
    @Bind(R.id.ll_warns)
    RelativeLayout llWarns;
    @Bind(R.id.refresh)
    ImageView refresh;
    @Bind(R.id.back_current)
    ImageView backCurrent;
    private Dialog use;
    @Bind(R.id.address)
    TextView address;
    @Bind(R.id.detail)
    TextView detail;
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
    @Bind(R.id.box_image)
    ImageView box_image;
    @Bind(R.id.title)
    TextView titile;

    public MapView mMapView;
    public BaiduMap mBaiduMap;
    public boolean visible = true;
    public float zoom = 16;
    BitmapDescriptor warnbitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_marka_warn);
    BitmapDescriptor markabitmap = BitmapDescriptorFactory.fromResource(R.mipmap.icon_marka);

    Odfbox odfbox = new Odfbox();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        OdfboxApplication.addActivity(this);
        getPemmission();
        setTitleTextView("物联网光交箱", null);
        mMapView = (MapView) findViewById(R.id.mapview);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        //从列表跳转显示光交箱
        refresh.setVisibility(View.GONE);
        backCurrent.setVisibility(View.GONE);
        llWarns.setVisibility(View.GONE);
        setLeftTextView(R.mipmap.ic_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        odfbox = (Odfbox) bundle.getSerializable("odfbox");
        if (bundle.getString("lat") != null && bundle.getString("lon") != null) {
            LatLng latLng = new LatLng(Double.parseDouble(getIntent().getStringExtra("lat")), Double.parseDouble(getIntent().getStringExtra("lon")));
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(latLng).zoom(zoom)
                    .build();
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            mBaiduMap.animateMapStatus(mMapStatusUpdate);

            OverlayOptions option1;
            if (bundle.getString("type").equals("true")) {
                option1 = new MarkerOptions()
                        .position(latLng)
                        .icon(warnbitmap);
            } else {
                option1 = new MarkerOptions()
                        .position(latLng)
                        .icon(markabitmap);
            }
            //在地图上添加Marker，并显示
            //使用marker携带info信息，当点击事件的时候可以通过marker获得info信息
            Marker marker = (Marker) mBaiduMap.addOverlay(option1);
            Bundle bundle1 = new Bundle();
            bundle1.putSerializable("info", odfbox);
            marker.setExtraInfo(bundle1);
        } else {
            showToast("没有找到此光交箱的位置");
        }


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
                                                   if (bundle != null && bundle.containsKey("info")) {
                                                       odfbox = (Odfbox) bundle.getSerializable("info");
                                                   } else {
                                                       odfbox = new Odfbox();
                                                       return true;
                                                   }
                                                   openes.setVisibility(View.VISIBLE);
                                                   address.setText(odfbox.address);
                                                   if (odfbox.serial_text != null) {
                                                       code.setText("光交箱编号：" + odfbox.serial_text);
                                                   } else {
                                                       code.setText("光交箱编号：----");
                                                   }

                                                   if (odfbox.cable_serial != null) {
                                                       code1.setText("光缆编号：" + odfbox.cable_serial);
                                                   } else {
                                                       code1.setText("光缆编号：----");
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
                                                       ImageLoader.getInstance().displayImage("http:" + odfbox.picture.url + "", box_image);
                                                   }

                                                   detail.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {
                                                           Intent intent = new Intent();
                                                           intent.putExtras(bundle);
                                                           intent.setClass(mContext, BoxDetailActivity.class);
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
                                                                                           }

                                                                                   );
                                                                               }
                                                                           }

                                                   );

                                                   return true;
                                               }
                                           }

        );


    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (data != null) {
//            final GeoCoder geoCoder = GeoCoder.newInstance();
//            geoCoder.geocode(new GeoCodeOption().city("昆明").address(data.getStringExtra("address")));
//            geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
//                @Override
//                public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
//                    mBaiduMap.clear();
//                    getList(geoCodeResult.getLocation().longitude + "", geoCodeResult.getLocation().latitude + "", "2000");
//                    MapStatus mMapStatus = new MapStatus.Builder()
//                            .target(new LatLng(geoCodeResult.getLocation().latitude, geoCodeResult.getLocation().longitude))
//                            .build();
//                    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
//                    mBaiduMap.animateMapStatus(mMapStatusUpdate);
//                }
//
//                @Override
//                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
//
//                }
//            });
//        }
//    }

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

//    public void getList(String lat, String lon, String distance) {
//
//        client.getNearBox(mContext, lat, lon, distance, new BoxListHandler() {
//            @Override
//            public void onSuccess(BoxList commen) {
//                super.onSuccess(commen);
//                cancelmDialog();
//                Log.d("Reqeust:", "Reqeust: " + commen.results.toString());
//                if (commen.results.size() != 0) {
//                    for (int i = 0; i < commen.results.size(); i++) {
//                        //定义Maker坐标点
//                        final Odfbox odfbox = commen.results.get(i);
//                        LatLng point = new LatLng(odfbox.latitude_baidu, odfbox.longitude_baidu);
//                        //构建Marker图标
//                        BitmapDescriptor bitmap;
//                        if (odfbox.alarming) {
//                            bitmap = warnbitmap;
//                        } else {
//                            bitmap = markabitmap;
//                        }
//                        //构建MarkerOption，用于在地图上添加Marker
//                        OverlayOptions option = new MarkerOptions()
//                                .position(point)
//                                .icon(bitmap);
//                        //在地图上添加Marker，并显示
//                        Marker marker = (Marker) mBaiduMap.addOverlay(option);
//                        //使用marker携带info信息，当点击事件的时候可以通过marker获得info信息
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("info", odfbox);
//                        marker.setExtraInfo(bundle);
//                    }
//                } else {
//                    showToast("当前位置附近没有光交箱！");
//                }
//
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                super.onFailure(msg);
//                cancelmDialog();
//            }
//
//            @Override
//            public void onCancel() {
//                super.onCancel();
//                cancelmDialog();
//            }
//        });
//    }


    public void getPemmission() {
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
        } else if (ContextCompat.checkSelfPermission(this, ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_NETWORK_STATE}, 3);
        } else if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, 5);
        } else if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 6);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != 6) {
            getPemmission();
        }

    }

}
