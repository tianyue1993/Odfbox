package com.odfbox.activity;

import android.os.Bundle;
import android.os.CountDownTimer;

import com.odfbox.OdfboxApplication;
import com.odfbox.R;

public class GuideThirdActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_third);
        OdfboxApplication.addActivity(this);
        setTitleTextView("开锁引导", null);
        timer.start();

    }

    // 注册 刷新验证码 倒计时
    CountDownTimer timer = new CountDownTimer(
            30 * 1000, 30 * 1000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.onFinish();
    }
}
