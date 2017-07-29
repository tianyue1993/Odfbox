package com.odfbox.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.baidu.mapapi.search.poi.PoiSearch;
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

public class IuputAddressActivity extends AppCompatActivity implements OnGetSuggestionResultListener {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.search)
    EditText search;
    @Bind(R.id.listview)
    ListView listview;
    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;
    private ArrayList<Address> suggest;
    private ComAddressAdapter sugAdapter;
    private Context mContext;
    private int type;
    GlobalSetting prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iuput_address);
        ButterKnife.bind(this);
        OdfboxApplication.addActivity(this);
        mContext = this;
        prefs = GlobalSetting.getInstance(mContext);
        type = getIntent().getIntExtra("type", 1);
        // 初始化建议搜索模块，注册建议搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

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
                Address address = sugAdapter.getItem(position);
                if (type == 1) {
                    prefs.saveAddress1(address.name, address.area);
                } else {
                    prefs.saveAddress2(address.name, address.area);
                }
                finish();

            }
        });

    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
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
}
