package com.odfbox.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.odfbox.OdfboxApplication;
import com.odfbox.R;
import com.odfbox.views.SmoothImageView;

public class SpaceImageDetailActivity extends Activity {

    private int mLocationX;
    private int mLocationY;
    private int mWidth;
    private int mHeight;
    private String url;
    SmoothImageView imageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OdfboxApplication.addActivity(this);
        url = getIntent().getStringExtra("images");
        mLocationX = getIntent().getIntExtra("locationX", 0);
        mLocationY = getIntent().getIntExtra("locationY", 0);
        mWidth = getIntent().getIntExtra("width", 0);
        mHeight = getIntent().getIntExtra("height", 0);

        imageView = new SmoothImageView(this);
        imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
        imageView.transformIn();
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        imageView.setScaleType(ScaleType.FIT_CENTER);
        setContentView(imageView);
        if (url != null) {
            ImageLoader.getInstance().displayImage(url, imageView);
        } else {
            imageView.setImageResource(R.mipmap.ic_box_image1);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setOnTransformListener(new SmoothImageView.TransformListener() {
                    @Override
                    public void onTransformComplete(int mode) {
                        if (mode == 2) {
                            finish();
                        }
                    }
                });
                imageView.transformOut();
            }
        });


    }

    @Override
    public void onBackPressed() {
        imageView.setOnTransformListener(new SmoothImageView.TransformListener() {
            @Override
            public void onTransformComplete(int mode) {
                if (mode == 2) {
                    finish();
                }
            }
        });
        imageView.transformOut();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(0, 0);
        }
    }

}
