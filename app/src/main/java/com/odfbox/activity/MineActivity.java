package com.odfbox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.odfbox.OdfboxApplication;
import com.odfbox.R;
import com.odfbox.entity.WarnsList;
import com.odfbox.handle.WarnHandler;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
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
    @Bind(R.id.undo_count1)
    TextView undoCount1;
    @Bind(R.id.undo_count2)
    TextView undoCount2;
    @Bind(R.id.my_data)
    RelativeLayout myData;
    @Bind(R.id.iamge)
    ImageView iamge;
    @Bind(R.id.text2)
    TextView text2;
    @Bind(R.id.text1)
    TextView text1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        OdfboxApplication.addActivity(this);
        ButterKnife.bind(this);
        phone.setText("用户名称  " + prefs.getUserPhone());
        company.setText(prefs.getOrgName());
        count.setText("光交箱总数：" + prefs.getBoxData()[1]);
        probability.setText("监控覆盖率：" + prefs.getBoxData()[0] + "");
        if (prefs.getBannerImage().length() > 0) {
            ImageLoader.getInstance().displayImage(prefs.getBannerImage(), iamge);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        getUntreated();
        getOrderUntreated();
    }

    @OnClick({R.id.back, R.id.ll_warn, R.id.add_box, R.id.my_box, R.id.add_workorder, R.id.my_manager, R.id.user_help, R.id.my_setting, R.id.my_data})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ll_warn:
                startActivity(new Intent(mContext, WarnListActivity.class));
                break;
            case R.id.add_box:
                startActivity(new Intent(mContext, AddBoxActivityNew.class));
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
            case R.id.my_data:
                startActivity(new Intent(mContext, DataStatisticsActivity.class));
                break;
        }
    }

    public void getUntreated() {
        client.getUntreated(mContext, new WarnHandler() {
            @Override
            public void onSuccess(WarnsList commen) {
                super.onSuccess(commen);
                undoCount1.setText("[" + commen.count + "]");
            }
        });
    }

    public void getOrderUntreated() {
        client.getOrderUntreated(mContext, new WarnHandler() {
            @Override
            public void onSuccess(WarnsList commen) {
                super.onSuccess(commen);
                undoCount2.setText("[" + commen.count + "]");
            }
        });
    }

}
