package com.odfbox.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.odfbox.R;
import com.odfbox.adapter.ComAddressAdapter;
import com.odfbox.adapter.MyOdfboxAdapter;
import com.odfbox.entity.Address;
import com.odfbox.entity.BoxList;
import com.odfbox.entity.Odfbox;
import com.odfbox.handle.BoxListHandler;
import com.odfbox.handle.CommentHandler;
import com.odfbox.utils.BoxUtils;
import com.odfbox.views.DialogFactory;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchBoxNewActivity extends BaseActivity implements OnGetSuggestionResultListener {

    @Bind(R.id.search_button)
    TextView searchButton;
    @Bind(R.id.rl_search)
    RelativeLayout rlSearch;
    @Bind(R.id.text_name_search)
    TextView textNameSearch;
    @Bind(R.id.ll_name_search)
    LinearLayout llNameSearch;
    @Bind(R.id.text_location_search)
    TextView textLocationSearch;
    @Bind(R.id.ll_location_search)
    LinearLayout llLocationSearch;
    @Bind(R.id.ll_choose_search)
    LinearLayout llChooseSearch;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.mapview)
    MapView mMapView;
    @Bind(R.id.checkbox1)
    CheckBox checkbox1;
    @Bind(R.id.checkbox2)
    CheckBox checkbox2;
    @Bind(R.id.activity_search_box_new)
    RelativeLayout activitySearchBoxNew;
    @Bind(R.id.input_news)
    EditText inputNews;
    @Bind(R.id.door)
    TextView door;
    @Bind(R.id.type)
    TextView type;
    @Bind(R.id.caizhi)
    TextView caizhi;
    @Bind(R.id.code)
    TextView code;
    @Bind(R.id.code1)
    TextView code1;
    @Bind(R.id.content)
    TextView content;
    @Bind(R.id.phone)
    TextView phone;
    @Bind(R.id.address)
    TextView address;
    @Bind(R.id.detail)
    TextView detail;
    @Bind(R.id.box_image)
    ImageView boxImage;
    @Bind(R.id.open)
    Button open;
    @Bind(R.id.openes)
    RelativeLayout openes;
    @Bind(R.id.icon)
    ImageView icon;
    public BaiduMap mBaiduMap;
    private int search_type = 1;
    private SuggestionSearch mSuggestionSearch = null;
    private ArrayList<Address> suggest = new ArrayList<>();
    private ComAddressAdapter sugAdapter;
    private ArrayList<Odfbox> box = new ArrayList<>();
    private MyOdfboxAdapter myOdfboxAdapter;
    BitmapDescriptor warnbitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_marka_warn);
    BitmapDescriptor markabitmap = BitmapDescriptorFactory.fromResource(R.mipmap.icon_marka);
    private Dialog use;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_box_new);
        ButterKnife.bind(this);
        setTitleTextView("查找光交箱", null);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        final MapStatus.Builder builder = new MapStatus.Builder().zoom(16);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//         初始化建议搜索模块，注册建议搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    suggest.clear();
                    if (sugAdapter != null)
                        sugAdapter.notifyDataSetChanged();
                    checkbox2.setChecked(false);
                    inputNews.setHint("请输入一个位置描述或者编号");
                    listview.setVisibility(View.VISIBLE);
                    mMapView.setVisibility(View.GONE);
                    icon.setVisibility(View.GONE);
                    search_type = 1;
                } else {
                    checkbox2.setChecked(true);
                }
            }
        });

        checkbox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    box.clear();
                    if (myOdfboxAdapter != null)
                        myOdfboxAdapter.notifyDataSetChanged();
                    checkbox1.setChecked(false);
                    inputNews.setHint("请输入一个位置");
                    listview.setVisibility(View.VISIBLE);
                    mMapView.setVisibility(View.GONE);
                    icon.setVisibility(View.GONE);
                    search_type = 2;
                } else {
                    checkbox1.setChecked(true);
                }
            }
        });

        llLocationSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkbox2.isChecked()) {
                    search_type = 2;
                    if (myOdfboxAdapter != null)
                        myOdfboxAdapter.notifyDataSetChanged();
                    checkbox2.setChecked(true);
                    checkbox1.setChecked(false);
                    listview.setVisibility(View.VISIBLE);
                    mMapView.setVisibility(View.GONE);
                    icon.setVisibility(View.GONE);
                    inputNews.setHint("请输入一个位置");
                }
            }
        });
        llNameSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkbox1.isChecked()) {
                    search_type = 1;
                    suggest.clear();
                    if (sugAdapter != null)
                        sugAdapter.notifyDataSetChanged();
                    checkbox1.setChecked(true);
                    checkbox2.setChecked(false);
                    listview.setVisibility(View.VISIBLE);
                    mMapView.setVisibility(View.GONE);
                    icon.setVisibility(View.GONE);
                    inputNews.setHint("请输入一个位置描述或者编号");
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView.setVisibility(View.GONE);
                icon.setVisibility(View.GONE);
                listview.setVisibility(View.VISIBLE);
                getResult();
            }
        });

        searchButton.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getResult();
                }
                return false;
            }
        });

        /**
         * 当输入关键字变化时，动态更新建议列表
         */
        inputNews.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (cs.length() <= 0) {
                    return;
                }
                /**
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */
                if (search_type == 2) {
                    mMapView.setVisibility(View.GONE);
                    icon.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                    mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(cs.toString()).city("昆明"));
                }
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (search_type == 2) {
                    mBaiduMap.clear();
                    Address address = sugAdapter.getItem(position);
                    mMapView.setVisibility(View.VISIBLE);
                    icon.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.GONE);
                    final GeoCoder geoCoder = GeoCoder.newInstance();
                    geoCoder.geocode(new GeoCodeOption().city("昆明").address(address.area + address.name));
                    geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                        @Override
                        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                            mBaiduMap.clear();
                            final MapStatus.Builder builder = new MapStatus.Builder().zoom(16);
                            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
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
        });


        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChangeStart(MapStatus status) {

            }

            // 移动结束，在这里需要计算出中心点坐标
            @Override
            public void onMapStatusChangeFinish(final MapStatus status) {
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

    }


    public void getList(String lat, String lon, String distance) {

        client.getNearBox(mContext, lat, lon, distance, new BoxListHandler() {
            @Override
            public void onSuccess(BoxList commen) {
                super.onSuccess(commen);
                cancelmDialog();
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

    public void getList(String s) {
        cancelmDialog();
        showProgress(0, true);
        client.getBoxList(mContext, "0", s, new BoxListHandler() {
            @Override
            public void onCancel() {
                super.onCancel();
                cancelmDialog();
            }

            @Override
            public void onSuccess(BoxList boxList) {
                super.onSuccess(boxList);
                cancelmDialog();
                if (boxList.results.size() > 0) {
                    box = boxList.results;
                    myOdfboxAdapter = new MyOdfboxAdapter(mContext, box);
                    listview.setAdapter(myOdfboxAdapter);
                }

                if (box.size() == 0) {
                    showToast("没有查找到符合条件的光交箱！");
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
    public void onGetSuggestionResult(SuggestionResult res) {
        if (res == null || res.getAllSuggestions() == null) {
            return;
        }
        suggest = new ArrayList<>();
        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            if (info.key != null) {
                Address address = new Address();
                address.name = info.key;
                address.area = info.district;
                suggest.add(address);
            }
        }
        sugAdapter = new ComAddressAdapter(mContext, suggest);
        listview.setAdapter(sugAdapter);
        sugAdapter.notifyDataSetChanged();
    }

    private void getResult() {
        box.clear();
        suggest.clear();
        if (sugAdapter != null)
            sugAdapter.notifyDataSetChanged();
        if (myOdfboxAdapter != null)
            myOdfboxAdapter.notifyDataSetChanged();
        if (search_type == 1) {
            if (inputNews.getText().toString().length() > 0) {
                getList("&address_like=" + inputNews.getText().toString());
                getList("&serial_text_like=" + inputNews.getText().toString());
            } else {
                showToast("请输入关键字！");
            }
        } else {
            if (inputNews.getText().toString().length() > 0)
                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(inputNews.getText().toString()).city("昆明"));

        }
    }

}
