package com.odfbox.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.odfbox.OdfboxApplication;
import com.odfbox.R;
import com.odfbox.entity.VersionOdfbox;
import com.odfbox.handle.CommentHandler;
import com.odfbox.handle.VersionHandler;
import com.odfbox.utils.BoxUtils;
import com.odfbox.views.DialogFactory;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

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
    @Bind(R.id.ll_news)
    RelativeLayout llNews;
    @Bind(R.id.new_image)
    ImageView newImage;
    VersionOdfbox versionOdfbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        setTitleTextView("设置", null);
        versionName.setText("版本：" + getVersion());
        getUpdate();
    }

    @OnClick({R.id.ll_address, R.id.ll_news, R.id.ll_version, R.id.exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_address:
                startActivity(new Intent(mContext, ComAddressActivity.class));
                break;
            case R.id.ll_version:
                Intent intent = new Intent(mContext, NewVersionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("version", versionOdfbox);
                intent.putExtras(bundle);
                startActivity(intent);
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
                        logout();

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


    public void getUpdate() {
        client.getVersion(mContext, new VersionHandler() {
            @Override
            public void onSuccess(VersionOdfbox commen) {
                super.onSuccess(commen);
                versionOdfbox = commen;
                if (commen.results.get(0) != null) {
                    if (commen.results.get(0).revision.contains("v") || commen.results.get(0).revision.contains("V")) {
                        if (BoxUtils.getIsUpdate(OdfboxApplication.getAppVersions(), commen.results.get(0).revision.substring(1, commen.results.get(0).revision.length()))) {
                            newImage.setVisibility(View.VISIBLE);
                        } else {
                            newImage.setVisibility(View.GONE);
                        }
                    } else {
                        if (BoxUtils.getIsUpdate(OdfboxApplication.getAppVersions(), commen.results.get(0).revision.substring(0, commen.results.get(0).revision.length()))) {
                            newImage.setVisibility(View.VISIBLE);
                        } else {
                            newImage.setVisibility(View.GONE);
                        }
                    }

                }
            }
        });
    }

    public void logout() {
        cancelmDialog();
        showProgress(0, true);
        JSONObject object = new JSONObject();
        object.put("action", "logout");
        try {
            showProgress(0, true);
            StringEntity entity = new StringEntity(object.toString(), "UTF-8");
            client.getLoginState(mContext, entity, new CommentHandler() {
                @Override
                public void onSuccess(String commen) {
                    super.onSuccess(commen);
                    cancelmDialog();
                    showToast("退出成功");
                    dialog.dismiss();
                    OdfboxApplication.finishActivity();
                    startActivity(new Intent(mContext, LoginActivity.class));
                    prefs.clear();
                    finish();
                }

                @Override
                public void onFailure(String msg) {
                    super.onFailure(msg);
                    showToast("退出失败");
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
    }

}
