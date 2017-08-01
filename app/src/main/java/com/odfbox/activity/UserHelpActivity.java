package com.odfbox.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.odfbox.OdfboxApplication;
import com.odfbox.R;
import com.odfbox.adapter.GuideAdapter;
import com.odfbox.entity.GuideList;
import com.odfbox.handle.GuideListHandler;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserHelpActivity extends BaseActivity {

    @Bind(R.id.listview)
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_help);
        ButterKnife.bind(this);
        OdfboxApplication.addActivity(this);
        setTitleTextView("用户指南", null);
        getUserGuide();
    }

    public void getUserGuide() {
        showProgress(0, true);
        client.getUserGuide(mContext, new GuideListHandler() {
            @Override
            public void onSuccess(GuideList boxList) {
                super.onSuccess(boxList);
                cancelmDialog();
                ArrayList<GuideList.Guide> arrayList = boxList.results;
                GuideAdapter adapter = new GuideAdapter(mContext, arrayList);
                listview.setAdapter(adapter);
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                cancelmDialog();
            }
        });
    }

}
