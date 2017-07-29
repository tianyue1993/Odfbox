package com.odfbox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.odfbox.OdfboxApplication;
import com.odfbox.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by tianyue on 2017/6/29.
 * 我的相关设置页面
 */

public class MineActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.avator)
    ImageView avator;
    @Bind(R.id.ll_warn)
    RelativeLayout llWarn;
    @Bind(R.id.add_box)
    RelativeLayout addBox;
    @Bind(R.id.my_box)
    RelativeLayout myBox;
    @Bind(R.id.add_workorder)
    RelativeLayout addWorkorder;
    @Bind(R.id.my_manager)
    RelativeLayout myManager;
    @Bind(R.id.user_help)
    RelativeLayout userHelp;
    @Bind(R.id.my_setting)
    RelativeLayout mySetting;
    @Bind(R.id.phone)
    TextView phone;
    @Bind(R.id.company)
    TextView company;
    @Bind(R.id.count)
    TextView count;
    @Bind(R.id.probability)
    TextView probability;
    @Bind(R.id.ll_box)
    LinearLayout llBox;
    @Bind(R.id.ll_manager)
    LinearLayout llManager;
    @Bind(R.id.ll_user)
    LinearLayout llUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        OdfboxApplication.addActivity(this);
        ButterKnife.bind(this);
        phone.setText(prefs.getUserPhone());
        company.setText(prefs.getOrgName());
        count.setText("光交箱总数：" + prefs.getBoxData()[1]);
        probability.setText("监控覆盖率：" + prefs.getBoxData()[0] + "%");
    }

    @OnClick({R.id.back, R.id.ll_warn, R.id.add_box, R.id.my_box, R.id.add_workorder, R.id.my_manager, R.id.user_help, R.id.my_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ll_warn:
                startActivity(new Intent(mContext, WarnListActivity.class));
                break;
            case R.id.add_box:
                startActivity(new Intent(mContext, AddBoxActivity.class));
                break;
            case R.id.my_box:
                startActivity(new Intent(mContext, MyBoxActivity.class));
                break;
            case R.id.add_workorder:
                startActivity(new Intent(mContext, WorkOrderActivity.class));
                break;
            case R.id.my_manager:
                startActivity(new Intent(mContext, WorkManageActivity.class));
                break;
            case R.id.user_help:
                startActivity(new Intent(mContext, UserHelpActivity.class));
                break;
            case R.id.my_setting:
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
        }
    }
}