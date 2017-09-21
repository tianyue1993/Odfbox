package com.odfbox.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.odfbox.R;
import com.odfbox.views.CircleProgressBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DataStatisticsActivity extends BaseActivity {

    @Bind(R.id.previous)
    ImageView previous;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.next)
    ImageView next;
    @Bind(R.id.total)
    TextView total;
    @Bind(R.id.tv_smart)
    TextView tvSmart;
    @Bind(R.id.smart_count)
    TextView smartCount;
    @Bind(R.id.activity_data_statistics)
    LinearLayout activityDataStatistics;
    @Bind(R.id.progressBar1)
    CircleProgressBar progressBar1;
    @Bind(R.id.progressBar2)
    CircleProgressBar progressBar2;
    @Bind(R.id.tv_warn)
    TextView tvWarn;
    @Bind(R.id.warn_count)
    TextView warnCount;
    @Bind(R.id.order_count)
    TextView orderCount;
    @Bind(R.id.progressBar3)
    CircleProgressBar progressBar3;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月");
    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
    String curTime = "";
    @Bind(R.id.count)
    TextView count;
    @Bind(R.id.tv_order)
    TextView tvOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_statistics);
        ButterKnife.bind(this);
        setTitleTextView("数据统计", null);
        progressBar1.setTargetPercent(80);
        progressBar2.setTargetPercent(40);
        progressBar3.setTargetPercent(90);
        curTime = formatter.format(curDate);
        time.setText(curTime);

    }

    @OnClick({R.id.previous, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.previous:
                Calendar c = Calendar.getInstance();
                c.add(Calendar.MONTH, -1);
                if (time.getText().toString().equals(curTime)) {
                    next.setImageResource(R.mipmap.ic_un_next);
                } else {
                    next.setImageResource(R.mipmap.ic_next);
                }

                break;
            case R.id.next:
                if (time.getText().toString().equals(curTime)) {
                    next.setImageResource(R.mipmap.ic_un_next);
                } else {
                    next.setImageResource(R.mipmap.ic_next);
                }
                break;
        }
    }
}
