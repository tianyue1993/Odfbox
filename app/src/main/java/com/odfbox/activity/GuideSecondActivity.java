package com.odfbox.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_second);
        ButterKnife.bind(this);
        OdfboxApplication.addActivity(this);
        setTitleTextView("开锁引导", null);
        intent = getIntent();
        id = intent.getStringExtra("id");
        getProgress();
        guide.setVisibility(View.VISIBLE);
        guide.setGifResource(R.mipmap.ic_secondx);
        guide.getGifResource();
        guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guide.play();
            }
        });
//        timer.start();
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
