package com.odfbox.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.odfbox.R;
import com.odfbox.entity.Statistics;
import com.odfbox.handle.StatisticsHandler;
import com.odfbox.views.CircleProgressBar;

import java.text.ParseException;
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
    String curTime = "";
    @Bind(R.id.count)
    TextView count;
    @Bind(R.id.tv_order)
    TextView tvOrder;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月");
    SimpleDateFormat formatData = new SimpleDateFormat("yyyy-MM");
    Date curDate = new Date(System.currentTimeMillis());//获取当前时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_statistics);
        ButterKnife.bind(this);
        setTitleTextView("数据统计", null);
        curTime = formatter.format(curDate);
        time.setText(curTime);
        getData();
        getTaskData();
    }

    @OnClick({R.id.previous, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.previous:
                String str = time.getText().toString();
                Date date = null;
                try {
                    date = formatter.parse(str);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.MONTH, -1);
                time.setText(formatter.format(calendar.getTime()));
                if (time.getText().toString().equals(curTime)) {
                    next.setImageResource(R.mipmap.ic_un_next);
                } else {
                    next.setImageResource(R.mipmap.ic_next);
                }
                getData();
                getTaskData();
                break;
            case R.id.next:
                if (time.getText().toString().equals(curTime)) {
                    next.setImageResource(R.mipmap.ic_un_next);
                } else {
                    next.setImageResource(R.mipmap.ic_next);
                    String strnext = time.getText().toString();
                    Date datenext = null;
                    try {
                        datenext = formatter.parse(strnext);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar calendarnext = Calendar.getInstance();
                    calendarnext.setTime(datenext);
                    calendarnext.add(Calendar.MONTH, +1);
                    time.setText(formatter.format(calendarnext.getTime()));
                }
                getData();
                getTaskData();
                break;
        }
    }

    public void getData() {

        String getTime = time.getText().toString().replace("年", "-");
        client.getStatistics(mContext, getTime.substring(0, getTime.length() - 1), new StatisticsHandler() {
            @Override
            public void onSuccess(Statistics commen) {
                super.onSuccess(commen);
                float f1 = (float) commen.odf_box_monitors / (float) commen.odf_boxes;
                float f2 = (float) commen.odf_boxes_alarming / (float) commen.odf_boxes;
                progressBar1.setTargetPercent(f1);
                progressBar2.setTargetPercent(f2);
                total.setText("光交箱总数   " + commen.odf_boxes);
                smartCount.setText(commen.odf_box_monitors + "");
                warnCount.setText(commen.odf_boxes_alarming + "");
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                progressBar1.setTargetPercent(0);
                progressBar2.setTargetPercent(0);
                total.setText("光交箱总数   15555");
                smartCount.setText("0");
                warnCount.setText("0");


            }
        });


    }

    public void getTaskData() {

        String getTime = time.getText().toString().replace("年", "-");
        client.getTaskStatistics(mContext, getTime.substring(0, getTime.length() - 1), new StatisticsHandler() {
            @Override
            public void onSuccess(Statistics commen) {
                super.onSuccess(commen);
                float i = (float) commen.task_sheets_done / (float) (commen.task_sheets + commen.task_sheets_done);
                progressBar3.setTargetPercent(i);
                count.setText("工单数   " + commen.task_sheets);
                orderCount.setText(commen.task_sheets_done + "");
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                progressBar3.setTargetPercent(0);
                count.setText("工单数   0");
                orderCount.setText("0");

            }
        });


    }
}
