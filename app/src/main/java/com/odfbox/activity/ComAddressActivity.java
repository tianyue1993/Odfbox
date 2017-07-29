package com.odfbox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.odfbox.OdfboxApplication;
import com.odfbox.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ComAddressActivity extends BaseActivity {

    @Bind(R.id.ll_address1)
    RelativeLayout llAddress1;
    @Bind(R.id.ll_address2)
    RelativeLayout llAddress2;
    @Bind(R.id.image1)
    ImageView image1;
    @Bind(R.id.address1)
    TextView address1;
    @Bind(R.id.set_address1)
    TextView setAddress1;
    @Bind(R.id.image2)
    ImageView image2;
    @Bind(R.id.address2)
    TextView address2;
    @Bind(R.id.set_address2)
    TextView setAddress2;
    @Bind(R.id.activity_com_address)
    RelativeLayout activityComAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_address);
        ButterKnife.bind(this);
        OdfboxApplication.addActivity(this);
        setTitleTextView("常用地址", null);

    }


    @OnClick({R.id.ll_address1, R.id.ll_address2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_address1:
                Intent intent = new Intent();
                intent.putExtra("type", 1);
                intent.setClass(mContext, IuputAddressActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_address2:
                Intent intent1 = new Intent();
                intent1.putExtra("type", 2);
                intent1.setClass(mContext, IuputAddressActivity.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs.getAddress1()[0].equals("")) {
            setAddress1.setTextColor(getResources().getColor(R.color.button_bg));
            image1.setImageResource(R.mipmap.ic_star2);
        } else {
            setAddress1.setTextColor(getResources().getColor(R.color.grey));
            setAddress1.setText(prefs.getAddress1()[1]);
            address1.setText(prefs.getAddress1()[0]);
            image1.setImageResource(R.mipmap.ic_star1);
        }
        if (prefs.getAddress2()[0].equals("")) {
            setAddress2.setTextColor(getResources().getColor(R.color.button_bg));
            image2.setImageResource(R.mipmap.ic_star2);
        } else {
            setAddress2.setTextColor(getResources().getColor(R.color.grey));
            setAddress2.setText(prefs.getAddress2()[1]);
            address2.setText(prefs.getAddress2()[0]);
            image2.setImageResource(R.mipmap.ic_star1);
        }
    }
}
