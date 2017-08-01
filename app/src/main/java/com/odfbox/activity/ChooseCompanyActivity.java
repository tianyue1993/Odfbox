package com.odfbox.activity;

import android.content.Intent;
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
                startActivity(new Intent(mContext, MainActivity.class));
                ApiClient.getInstance().asyncHttpClient.addHeader("Authorization", prefs.getToken());
                getOrg();
                finish();
            }


            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                cancelmDialog();
            }
        });

    }


    public void getOrg() {
        client.getOrg(mContext, accessKey.orgs.get(selectPosition).org_id + "", new OrgHandler() {
            @Override
            public void onSuccess(Org org) {
                super.onSuccess(org);
                double percent = org.odf_box_monitored / org.total_odf_box;
                prefs.saveBoxData(percent * 100 + "", org.total_odf_box + "");
                prefs.saveOrgName(org.name);
                Log.d("getOrg", "getOrg: " + org.toString());
            }
        });
    }
}
