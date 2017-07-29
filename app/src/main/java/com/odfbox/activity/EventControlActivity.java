package com.odfbox.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.odfbox.OdfboxApplication;
import com.odfbox.R;
import com.odfbox.entity.Event;
import com.odfbox.entity.Tasks;
import com.odfbox.entity.UserList;
import com.odfbox.handle.CommentHandler;
import com.odfbox.handle.UserListHandler;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventControlActivity extends BaseActivity {

    Event event;
    @Bind(R.id.control_person)
    TextView controlPerson;
    @Bind(R.id.state)
    TextView state;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.control_describe)
    EditText controlDescribe;
    @Bind(R.id.spinner_person)
    Spinner spinnerPerson;
    @Bind(R.id.the_time)
    TextView completeTime;
    @Bind(R.id.tv_type)
    TextView tvType;
    @Bind(R.id.tv_code)
    TextView tvCode;
    @Bind(R.id.btn_cancel)
    Button btnCancel;
    @Bind(R.id.commit)
    Button commit;
    @Bind(R.id.checkbox1)
    CheckBox checkbox1;
    @Bind(R.id.checkbox2)
    CheckBox checkbox2;


    private String appoint_to;
    ArrayList<String> userId = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_control);
        ButterKnife.bind(this);
        OdfboxApplication.addActivity(this);
        setTitleTextView("告警处理", null);
        Bundle bundle = getIntent().getExtras();
        event = (Event) bundle.getSerializable("event");
        initData();
        getUser();

    }

    public void initData() {
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String str = formatter.format(Calendar.getInstance().getTime());
        completeTime.setText(str);
        completeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                View dataview = View.inflate(mContext, R.layout.time_layout, null);
                final DatePicker timePicker = (DatePicker) dataview.findViewById(R.id.time_picker);
                final Button commit = (Button) dataview.findViewById(R.id.commit);
                builder.setView(dataview);
                builder.setTitle("选择开始时间");
                final Dialog dialog = builder.create();
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                timePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
                commit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        int month = timePicker.getMonth() + 1;
                        completeTime.setText(timePicker.getYear() + "-" + month + "-" + timePicker.getDayOfMonth());
                    }
                });
                dialog.show();
            }
        });


        if (event != null) {
            if (event.create_user != null) {
                controlPerson.setText("操 作 人    " + event.create_user);
            }

            if (event.time != null) {
                time.setText("时          间        " + event.time);
            }

            if (event.text != null) {
                tvType.setText(event.text);
            }

            if (event.task_sheet != null && event.handle != null) {
                state.setText("完成");
                state.setTextColor(mContext.getResources().getColor(R.color.text_grey));
            } else {
                state.setText("未完成");
                state.setTextColor(mContext.getResources().getColor(R.color.red));
            }

            if (event.task_sheet != null) {
                tvCode.setText(event.task_sheet.serial);

            }
        }


        spinnerPerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                tv.setTextColor(getResources().getColor(R.color.text_grey));    //设置颜色
                tv.setTextSize(12.0f);    //设置大小
                tv.setGravity(Gravity.CENTER_HORIZONTAL);   //设置居中
                appoint_to = userId.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkbox2.setChecked(false);
                } else {
                    checkbox2.setChecked(true);
                }
            }
        });

        checkbox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkbox1.setChecked(false);
                } else {
                    checkbox1.setChecked(true);
                }
            }
        });


    }

    @OnClick({R.id.btn_cancel, R.id.commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.commit:
                commit();
                break;
        }
    }

    public void commit() {
        if (checkbox2.isChecked()) {
            JSONObject object = new JSONObject();
            object.put("task_sheet_type", "告警处理");
            object.put("dead_line_time", completeTime.getText().toString() + " 00:00:00");
            object.put("event", event.id);
            object.put("appoint_to", appoint_to);
            Tasks tasks = new Tasks();
            tasks.task_desc = "处理事件" + event.text;
            tasks.target_type = "光交箱";
            tasks.target_id = event.odf_box + "";
            ArrayList<Tasks> list = new ArrayList<>();
            list.add(tasks);
            object.put("tasks", list);
            Log.d("Reqeust:", "Reqeustobject:: " + object.toString());
            try {
                cancelmDialog();
                showProgress(0, true);
                StringEntity entity = new StringEntity(object.toString(), "UTF-8");
                client.getPutEventOrder(mContext, entity, new CommentHandler() {
                    @Override
                    public void onSuccess(String commen) {
                        super.onSuccess(commen);
                        showToast("提交成功");
                        cancelmDialog();
                        finish();
                    }

                    @Override
                    public void onFailure() {
                        super.onFailure();
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
        } else {
            JSONObject object = new JSONObject();
            object.put("treat_state", "忽略");
            if (controlDescribe.getText().toString().length() < 1) {
                showToast("请输入处理内容");
                return;
            } else {
                object.put("comment", controlDescribe.getText().toString());
            }
            try {
                cancelmDialog();
                showProgress(0, true);
                StringEntity entity = new StringEntity(object.toString(), "UTF-8");
                client.getPutEventHandle(mContext, event.id + "", entity, new CommentHandler() {
                    @Override
                    public void onSuccess(String commen) {
                        super.onSuccess(commen);
                        showToast("提交成功");
                        cancelmDialog();
                        finish();
                    }

                    @Override
                    public void onFailure() {
                        super.onFailure();
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


    }

    public void getUser() {
        client.getUserList(mContext, new UserListHandler() {
            @Override
            public void onSuccess(UserList commen) {
                super.onSuccess(commen);
                ArrayList<String> userList = new ArrayList<>();
                userId = new ArrayList<>();
                for (int i = 0; i < commen.results.size(); i++) {
                    userList.add(commen.results.get(i).fullname);
                    userId.add(commen.results.get(i).id);
                }
                final String[] user = userList.toArray(new String[0]);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, user);
                //绑定 Adapter到控件
                spinnerPerson.setAdapter(adapter);
                spinnerPerson.setSelection(0, true);
            }

        });

    }
}
