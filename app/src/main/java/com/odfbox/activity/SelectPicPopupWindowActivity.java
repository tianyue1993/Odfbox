package com.odfbox.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.odfbox.OdfboxApplication;
import com.odfbox.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectPicPopupWindowActivity extends Activity implements View.OnClickListener {
    @Bind(R.id.pop_layout)
    LinearLayout layout;
    @Bind(R.id.btn_pick_photo)
    Button btnPickPhoto;
    @Bind(R.id.btn_take_photo)
    Button btnTakePhoto;
    @Bind(R.id.btn_cancel)
    Button btnCancel;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OdfboxApplication.addActivity(this);
        setContentView(R.layout.activity_select_pic_popup_window);
        ButterKnife.bind(this);
        context = this;
    }


    // 实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    @OnClick({R.id.btn_take_photo, R.id.btn_pick_photo, R.id.btn_cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_photo:
                if (ContextCompat.checkSelfPermission(SelectPicPopupWindowActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(SelectPicPopupWindowActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //开启权限访问对话框
                    ActivityCompat.requestPermissions(SelectPicPopupWindowActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10003);

                } else {
                    backWithData("picmethod", "TAKEPHOTO");
                }
                break;
            case R.id.btn_pick_photo:
                if (ContextCompat.checkSelfPermission(SelectPicPopupWindowActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //开启权限访问对话框
                    ActivityCompat.requestPermissions(SelectPicPopupWindowActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10003);

                } else {
                    backWithData("picmethod", "PICKPHOTO");
                }
                break;
            case R.id.btn_cancel:
                break;
            default:
                break;
        }
        finish();
    }

    private void backWithData(String k, String v) {
        Intent data = new Intent();
        data.putExtra(k, v);
        setResult(RESULT_OK, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
