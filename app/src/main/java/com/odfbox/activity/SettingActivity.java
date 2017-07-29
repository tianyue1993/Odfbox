package com.odfbox.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.odfbox.OdfboxApplication;
import com.odfbox.R;
import com.odfbox.views.DialogFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @Bind(R.id.ll_address)
    RelativeLayout llAddress;
    @Bind(R.id.ll_version)
    RelativeLayout llVersion;
    @Bind(R.id.exit)
    Button exit;
    @Bind(R.id.version_name)
    TextView versionName;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        setTitleTextView("设置", null);
        versionName.setText(getVersion());
    }

    @OnClick({R.id.ll_address, R.id.ll_version, R.id.exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_address:
                startActivity(new Intent(mContext, ComAddressActivity.class));
                break;
            case R.id.exit:
                dialog = DialogFactory.getDialogFactory().showCommonDialog(mContext, "确定退出？", "取消", "确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        OdfboxApplication.finishActivity();
                        startActivity(new Intent(mContext, LoginActivity.class));
                        prefs.clear();
                        finish();

                    }
                });

                break;
        }
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
