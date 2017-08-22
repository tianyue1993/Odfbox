package com.odfbox.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.odfbox.ApiClient;
import com.odfbox.MainActivity;
import com.odfbox.OdfboxApplication;
import com.odfbox.R;
import com.odfbox.adapter.CompanyAdapter;
import com.odfbox.entity.AccessKey;
import com.odfbox.entity.Org;
import com.odfbox.handle.CommentHandler;
import com.odfbox.handle.OrgHandler;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseCompanyActivity extends BaseActivity {


    @Bind(R.id.listview)
    GridView listview;
    @Bind(R.id.sure)
    Button sure;
    AccessKey accessKey;
    public int selectPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_company);
        ButterKnife.bind(this);
        OdfboxApplication.addActivity(this);
        setTitleTextView("手机号验证", null);
        Bundle bundle = getIntent().getExtras();
        accessKey = (AccessKey) bundle.getSerializable("access");
        final CompanyAdapter adapter = new CompanyAdapter(mContext, accessKey.orgs);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetInvalidated();
                selectPosition = position;
            }
        });
    }

    @OnClick({R.id.sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sure:
                getToken();
                break;
        }
    }

    public void getToken() {
        showProgress(0, true);
        client.getToken(mContext, accessKey.access_key, accessKey.orgs.get(selectPosition).org_id + "", new CommentHandler() {
            @Override
            public void onCancel() {
                super.onCancel();
                cancelmDialog();
            }

            @Override
            public void onSuccess(String commen) {
                super.onSuccess(commen);
                cancelmDialog();
                JSONObject object = JSON.parseObject(commen);
                prefs.saveToken(object.getString("token"));
                prefs.saveOrgId(accessKey.orgs.get(selectPosition).org_id + "");
                prefs.saveUserId(accessKey.orgs.get(selectPosition).user_id);
                prefs.saveOrgName(accessKey.orgs.get(selectPosition).name);
                ApiClient.getInstance().asyncHttpClient.addHeader("Authorization", prefs.getToken());
                getOrg();
                login();

            }


            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                cancelmDialog();
            }
        });

    }

    public void login() {
        SharedPreferences preferences = mContext.getSharedPreferences("CHANNELID", Context.MODE_PRIVATE);
        JSONObject object = new JSONObject();
        object.put("action", "login");
        object.put("platform", "Android");
        object.put("channel_id_baidu", preferences.getString("channelid", ""));
        try {
            showProgress(0, true);
            StringEntity entity = new StringEntity(object.toString(), "UTF-8");
            client.getLoginState(mContext, entity, new CommentHandler() {
                @Override
                public void onSuccess(String commen) {
                    super.onSuccess(commen);
                    showToast("登陆成功");
                    startActivity(new Intent(mContext, MainActivity.class));
                    finish();
                }

                @Override
                public void onFailure(String msg) {
                    super.onFailure(msg);
                    showToast("登陆失败");
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void getOrg() {
        client.getOrg(mContext, accessKey.orgs.get(selectPosition).org_id + "", new OrgHandler() {
            @Override
            public void onSuccess(Org org) {
                super.onSuccess(org);
                if (org.odf_box_monitored != 0f && org.total_odf_box != null) {
                    float percent = (org.odf_box_monitored / (float) org.total_odf_box);
                    NumberFormat nt = NumberFormat.getPercentInstance();
                    //设置百分数精确度2即保留两位小数
                    nt.setMinimumFractionDigits(2);
                    //最后格式化并输出
                    prefs.saveBoxData(nt.format(percent), org.total_odf_box + "");
                }
                prefs.saveOrgName(org.name);
                Log.d("getOrg", "getOrg: " + org.toString());
            }
        });
    }
}
