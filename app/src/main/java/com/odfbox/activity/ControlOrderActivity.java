package com.odfbox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.odfbox.OdfboxApplication;
import com.odfbox.R;
import com.odfbox.adapter.TaskAdapter;
import com.odfbox.entity.TaskList;
import com.odfbox.entity.Tasks;
import com.odfbox.entity.WorkOrder;
import com.odfbox.handle.CommentHandler;
import com.odfbox.handle.TaskHandler;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ControlOrderActivity extends BaseActivity {

    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.line1)
    ImageView line1;
    @Bind(R.id.code)
    TextView code;
    @Bind(R.id.state)
    TextView state;
    @Bind(R.id.manager)
    TextView manager;
    @Bind(R.id.type)
    TextView type;
    @Bind(R.id.starttime)
    TextView starttime;
    @Bind(R.id.endtime)
    TextView endtime;
    @Bind(R.id.content)
    TextView content;
    @Bind(R.id.work_news)
    RelativeLayout workNews;
    @Bind(R.id.line2)
    ImageView line2;
    @Bind(R.id.text1)
    TextView text1;
    @Bind(R.id.count)
    TextView count;
    @Bind(R.id.rl_count)
    RelativeLayout rlCount;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.activity_control_order)
    RelativeLayout activityControlOrder;

    TaskAdapter adapter;

    WorkOrder workOrder;
    @Bind(R.id.ed_work_control)
    EditText edWorkControl;
    @Bind(R.id.commit)
    Button commit;
    @Bind(R.id.rl_control)
    RelativeLayout rlControl;

    boolean isVisible = true;
    boolean isComplete = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_order);
        ButterKnife.bind(this);
        OdfboxApplication.addActivity(this);
        setTitleTextView("工单处理", null);
        setRightText("提交", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible) {
                    rlControl.setVisibility(View.VISIBLE);
                    isVisible = false;
                } else {
                    rlControl.setVisibility(View.GONE);
                    isVisible = true;
                }
            }
        });
        Bundle bundle = getIntent().getExtras();
        workOrder = (WorkOrder) bundle.getSerializable("workOrder");
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tasks tasks = adapter.getItem(position);
                Intent intent = new Intent(mContext, TaskControlActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("tasks", tasks);
                bundle.putSerializable("order", workOrder);
                intent.putExtras(bundle);
                mContext.startActivity(intent);

            }
        });
        initData();

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < workOrder.tasks.size(); i++) {
                    if (!workOrder.tasks.get(i).state.equals("完成")) {
                        isComplete = false;
                        break;
                    }
                }
                if (isComplete) {
                    if (edWorkControl.getText().toString().length() > 0) {
                        putWorkControl();
                    } else {
                        showToast("请输入工单处理情况");
                    }
                } else {
                    showToast("请先处理完任务后再提交");
                }

            }
        });
    }


    public void putWorkControl() {
        showProgress(0, true);
        JSONObject params = new JSONObject();
        params.put("worker_comment", edWorkControl.getText().toString().trim());
        try {
            StringEntity entity = new StringEntity(params.toString(), "UTF-8");
            client.getPutOrder(mContext, workOrder.id + "", entity, new CommentHandler() {
                @Override
                public void onSuccess(String commen) {
                    super.onSuccess(commen);
                    cancelmDialog();
                    rlControl.setVisibility(View.GONE);
                    isVisible = true;
                    showToast("提交成功");
                }

                @Override
                public void onFailure(String msg) {
                    super.onFailure(msg);
                    cancelmDialog();
                }

                @Override
                public void onCancel() {
                    super.onCancel();
                    cancelmDialog();
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public void initData() {
        if (workOrder != null) {
            if (workOrder.serial != null) {
                code.setText("工单号：" + workOrder.serial);
            } else {
                code.setText("工单号：----");
            }


            if (workOrder.state != null) {
                state.setText("状态：" + workOrder.state);
            } else {
                state.setText("状态：----");
            }


            if (workOrder.appoint_to_name != null) {
                manager.setText("发起人：" + workOrder.appoint_from_name);
            } else {
                manager.setText("发起人：----");
            }

            if (workOrder.task_sheet_type != null) {
                type.setText("工单类型：" + workOrder.task_sheet_type);
            } else {
                type.setText("工单类型：----");
            }

            if (workOrder.create_time != null) {
                starttime.setText("发起时间：" + workOrder.create_time);
            } else {
                starttime.setText("发起时间：----");
            }

            if (workOrder.accomplish_time != null) {
                endtime.setText("完成时间：" + workOrder.accomplish_time);
            } else {
                endtime.setText("要求完成时间：----");
            }

            if (workOrder.comment != null) {
                content.setText("工单内容：" + workOrder.comment);
            } else {
                content.setText("工单内容：----");
            }
            if (workOrder.tasks != null) {
                count.setText("任务数:" + workOrder.tasks.size());
            }


        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        getTaskList();

    }

    public void getTaskList() {
        showProgress(0, true);
        client.getTaskList(mContext, workOrder.id + "", new TaskHandler() {
            @Override
            public void onSuccess(TaskList commen) {
                super.onSuccess(commen);
                count.setText("任务数:" + commen.count);
                cancelmDialog();
                adapter = new TaskAdapter(mContext, commen.results);
                listview.setAdapter(adapter);
                workOrder.tasks = commen.results;
            }

            @Override
            public void onCancel() {
                super.onCancel();
                cancelmDialog();
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                cancelmDialog();
            }
        });
    }
}
