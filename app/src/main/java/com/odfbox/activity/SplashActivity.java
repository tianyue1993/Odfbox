package com.odfbox.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.odfbox.MainActivity;
import com.odfbox.R;
import com.odfbox.utils.GlobalSetting;


/**
 * Created by tianyue on 2017/6/29.
 * 启动页面
 */
public class SplashActivity extends Activity {
    GlobalSetting prefs;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        prefs = GlobalSetting.getInstance(this);
        mContext = this;
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, "OUlSjHHG7gXwvHD1uf1Yg57GdxzGwoyI");
        ImageView splash = (ImageView) findViewById(R.id.splash);
        Animation animation = new AlphaAnimation(1.0f, 1.0f);
        animation.setDuration(1000);
        splash.setAnimation(animation);
        animation.startNow();
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                SplashActivity.this.finish();

                if (TextUtils.isEmpty(prefs.getToken())) {
                    startActivity(new Intent(mContext, LoginActivity.class));
                } else {
                    startActivity(new Intent(mContext, MainActivity.class));
                }

                finish();

            }

        });

//        startActivity(new Intent(mContext, MainActivity.class));
    }
}
