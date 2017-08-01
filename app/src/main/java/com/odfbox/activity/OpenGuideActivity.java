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


public class OpenGuideActivity extends BaseActivity {

    @Bind(R.id.guide)
    GifView guide;
    Dialog dialog;
    Dialog exist;
    String id;

    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_guide);
        OdfboxApplication.addActivity(this);
        ButterKnife.bind(this);
        setTitleTextView("开锁引导", null);
        intent = getIntent();
        id = intent.getStringExtra("id");
        getProgress();
        guide.setVisibility(View.VISIBLE);
        guide.setGifResource(R.mipmap.ic_firstx);
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
                if (odfbox.smart_lock.business_state.equals("已授权")) {

                    intent.setClass(mContext, GuideSecondActivity.class);
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


            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                exist = DialogFactory.getDialogFactory().showCommonDialog(mContext, "服务器处理异常", "取消", "确定", new View.OnClickListener() {
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
            }

        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
