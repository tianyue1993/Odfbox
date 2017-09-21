package com.odfbox.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.odfbox.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchBoxNewActivity extends BaseActivity {

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
    MapView mapview;
    @Bind(R.id.checkbox1)
    CheckBox checkbox1;
    @Bind(R.id.checkbox2)
    CheckBox checkbox2;
    @Bind(R.id.activity_search_box_new)
    RelativeLayout activitySearchBoxNew;
    @Bind(R.id.input_news)
    EditText inputNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_box_new);
        ButterKnife.bind(this);
        setTitleTextView("查找光交箱", null);
        setRightImage(R.mipmap.ic_scan_img, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkbox2.setChecked(false);
                    inputNews.setHint("请输入一个位置描述或者编号");
                } else {
                    checkbox2.setChecked(true);
                }
            }
        });

        checkbox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkbox1.setChecked(false);
                    inputNews.setHint("请输入一个位置");
                } else {
                    checkbox1.setChecked(true);
                }
            }
        });

        llLocationSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkbox2.isChecked()) {
                    checkbox2.setChecked(true);
                    checkbox1.setChecked(false);
                    inputNews.setHint("请输入一个位置");
                }
            }
        });
        llNameSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkbox1.isChecked()) {
                    checkbox1.setChecked(true);
                    checkbox2.setChecked(false);
                    inputNews.setHint("请输入一个位置描述或者编号");
                }
            }
        });
    }
}
