package com.odfbox.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.odfbox.OdfboxApplication;
import com.odfbox.R;
import com.odfbox.adapter.ComAddressAdapter;
import com.odfbox.entity.Address;
import com.odfbox.utils.GlobalSetting;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetComLocationActivity extends AppCompatActivity implements OnGetSuggestionResultListener {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.search)
    EditText search;
    @Bind(R.id.image1)
    ImageView image1;
    @Bind(R.id.address1)
    TextView address1;
    @Bind(R.id.line1)
    ImageView line1;
    @Bind(R.id.image2)
    ImageView image2;
    @Bind(R.id.address2)
    TextView address2;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.ll_search)
    RelativeLayout llSearch;
    @Bind(R.id.where)
    RelativeLayout where;
    @Bind(R.id.my_address)
    RelativeLayout myAddress;
    @Bind(R.id.set_address1)
    TextView setAddress1;
    @Bind(R.id.set_address2)
    TextView setAddress2;
    @Bind(R.id.to_mybox)
    TextView to_mybox;

    Context mContext;
    public int SETLOCATIONCODE = 12;

    GlobalSetting pref;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    private SuggestionSearch mSuggestionSearch = null;
    private ArrayList<Address> suggest = new ArrayList<>();
    private ComAddressAdapter sugAdapter;
    Address address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);
        OdfboxApplication.addActivity(this);
        ButterKnife.bind(this);
        mContext = this;
        pref = GlobalSetting.getInstance(mContext);
        // 初始化建议搜索模块，注册建议搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

        tvAddress.setText("我的位置：" + pref.getCurrentAddress()[2]);
        /**
         * 当输入关键字变化时，动态更新建议列表
         */
        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                if (cs.length() <= 0) {
                    return;
                }

                /**
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */
                mSuggestionSearch
                        .requestSuggestion((new SuggestionSearchOption())
                                .keyword(cs.toString()).city("昆明"));
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                search.setText(sugAdapter.getItem(position).name);
                address = sugAdapter.getItem(position);
                Intent data = new Intent();
                data.putExtra("address", address.area + address.name);
                setResult(SETLOCATIONCODE, data);
                finish();

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (pref.getAddress1()[0].equals("")) {
            setAddress1.setTextColor(getResources().getColor(R.color.button_bg));
            image1.setImageResource(R.mipmap.ic_star2);
        } else {
            setAddress1.setTextColor(getResources().getColor(R.color.grey));
            setAddress1.setText(pref.getAddress1()[1]);
            address1.setText(pref.getAddress1()[0]);
            image1.setImageResource(R.mipmap.ic_star1);
        }
        if (pref.getAddress2()[0].equals("")) {
            setAddress2.setTextColor(getResources().getColor(R.color.button_bg));
            image2.setImageResource(R.mipmap.ic_star2);
        } else {
            setAddress2.setTextColor(getResources().getColor(R.color.grey));
            setAddress2.setText(pref.getAddress2()[1]);
            address2.setText(pref.getAddress2()[0]);
            image2.setImageResource(R.mipmap.ic_star1);
        }
    }


    /**
     * 获取在线建议搜索结果，得到requestSuggestion返回的搜索结果
     *
     * @param res
     */
    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        if (res == null || res.getAllSuggestions() == null) {
            return;
        }
        suggest = new ArrayList<Address>();
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

    @OnClick({R.id.back, R.id.to_mybox, R.id.address1, R.id.set_address1, R.id.address2, R.id.set_address2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.set_address1:

                if (setAddress1.getText().toString().equals("设置地址")) {
                    Intent intent = new Intent();
                    intent.putExtra("type", 1);
                    intent.setClass(mContext, IuputAddressActivity.class);
                    startActivity(intent);
                } else {
                    Intent data = new Intent();
                    data.putExtra("address", pref.getAddress1()[0] + pref.getAddress1()[1]);
                    setResult(SETLOCATIONCODE, data);
                    finish();
                }

                break;
            case R.id.set_address2:
                if (setAddress2.getText().toString().equals("设置地址")) {
                    Intent intent1 = new Intent();
                    intent1.putExtra("type", 2);
                    intent1.setClass(mContext, IuputAddressActivity.class);
                    startActivity(intent1);
                } else {
                    Intent data = new Intent();
                    data.putExtra("address", pref.getAddress2()[0] + pref.getAddress2()[1]);
                    setResult(SETLOCATIONCODE, data);
                    finish();
                }

                break;
            case R.id.to_mybox:
                startActivity(new Intent(mContext, MyBoxActivity.class));
                break;
            case R.id.address1:
                if (!setAddress1.getText().toString().equals("设置地址")) {
                    Intent data = new Intent();
                    data.putExtra("address", pref.getAddress1()[0] + pref.getAddress1()[1]);
                    setResult(SETLOCATIONCODE, data);
                    finish();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("type", 1);
                    intent.setClass(mContext, IuputAddressActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.address2:
                if (!setAddress1.getText().toString().equals("设置地址")) {
                    Intent data = new Intent();
                    data.putExtra("address", pref.getAddress2()[0] + pref.getAddress1()[1]);
                    setResult(SETLOCATIONCODE, data);
                    finish();
                } else {
                    Intent intent1 = new Intent();
                    intent1.putExtra("type", 2);
                    intent1.setClass(mContext, IuputAddressActivity.class);
                    startActivity(intent1);
                }
                break;
        }
    }
}
