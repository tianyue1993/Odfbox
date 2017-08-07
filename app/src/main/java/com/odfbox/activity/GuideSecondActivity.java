package com.odfbox.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import com.cunoraz.gifview.library.GifView;
import com.odfbox.OdfboxApplication;
import com.odfbox.R;
import com.odfbox.entity.Odfbox;
import com.odfbox.handle.BoxDetailHandler;
import com.odfbox.views.DialogFactory;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GuideSecondActivity extends BaseActivity {

    String id;
    Intent intent;
    @Bind(R.id.guide)
    GifView guide;
    Dialog dialog;
    Dialog exist;
    Vibrator vibrator;
    private MediaPlayer mPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_second);
        ButterKnife.bind(this);
        OdfboxApplication.addActivity(this);
        setTitleTextView("开锁引导", null);
        intent = getIntent();
        id = intent.getStringExtra("id");
        guide.setVisibility(View.VISIBLE);
        guide.setGifResource(R.mipmap.ic_secondx);
        guide.getGifResource();
        guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guide.play();
            }
        });
        setLeftTextView(R.mipmap.ic_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = DialogFactory.getDialogFactory().showCommonDialog(mContext, "确定退出？", "取消", "确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        finish();
                    }
                });
            }
        });
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 400, 100, 400}; // 停止 开启 停止 开启
        vibrator.vibrate(3000); //重复两次上面的pattern 如果只想震动一次，index设为-1
        mPlayer = MediaPlayer.create(this, R.raw.music);
        mPlayer.start();
        getProgress();
    }


    @Override
    protected void onPause() {
        super.onPause();
        vibrator.cancel();
        mPlayer.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        vibrator.cancel();
        mPlayer.stop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        vibrator.cancel();
    }

    @Override
    public void onBackPressed() {
        dialog = DialogFactory.getDialogFactory().showCommonDialog(mContext, "确定退出？", "取消", "确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

    }


    public void getProgress() {
        client.getBoxDetail(mContext, id, new BoxDetailHandler() {
            @Override
            public void onSuccess(Odfbox odfbox) {
                super.onSuccess(odfbox);
//                long[] pattern = {100, 400, 100, 400}; // 停止 开启 停止 开启
//                vibrator.vibrate(100); //重复两次上面的pattern 如果只想震动一次，index设为-1
                Log.d("Odfboxdata", "onSuccess: " + odfbox.smart_lock.business_state);
                if (odfbox.smart_lock.business_state.equals("已打开")) {
                    intent.setClass(mContext, GuideThirdActivity.class);
                    startActivity(intent);
                    finish();
                } else if (odfbox.smart_lock.business_state.equals("上锁")) {
                    exist = DialogFactory.getDialogFactory().showCommonDialog(mContext, "操作超时", "取消", "确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            exist.dismiss();
                            finish();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            exist.dismiss();
                            finish();
                        }
                    });
                } else {
                    getProgress();
                }
            }
        });

    }

}
