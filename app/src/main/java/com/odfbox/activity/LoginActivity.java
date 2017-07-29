package com.odfbox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.odfbox.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tianyue on 2017/6/29.
 * 登陆页面
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.login)
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login)
    public void onViewClicked() {
        startActivity(new Intent(mContext, PhoneValidateActivity.class));
        finish();
    }
}
