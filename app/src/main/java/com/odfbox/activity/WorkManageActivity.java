package com.odfbox.activity;

import android.os.Bundle;

import com.odfbox.OdfboxApplication;
import com.odfbox.R;

public class WorkManageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_manage);
        setTitleTextView("施工管理", null);
        OdfboxApplication.addActivity(this);
    }
}
