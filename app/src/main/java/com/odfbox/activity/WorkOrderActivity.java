package com.odfbox.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.odfbox.OdfboxApplication;
import com.odfbox.R;
import com.odfbox.adapter.WorkOrderAdapter;
import com.odfbox.entity.WorkOrder;
import com.odfbox.entity.WorkOrderList;
import com.odfbox.handle.WorkOrderHandler;
import com.odfbox.views.DownPullRefreshListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;


public class WorkOrderActivity extends BaseActivity {

    @Bind(R.id.checkbox_undo)
    TextView checkboxUndo;
    @Bind(R.id.checkbox_search)
    TextView checkboxSearch;
    @Bind(R.id.listview)
    DownPullRefreshListView listview;
    @Bind(R.id.code)
    EditText code;
    @Bind(R.id.spinner1)
    Spinner spinner1;
    @Bind(R.id.spinner2)
    Spinner spinner2;
    @Bind(R.id.starttime)
    TextView starttime;
    @Bind(R.id.endtime)
    TextView endtime;
    @Bind(R.id.search)
    Button search;
    @Bind(R.id.ll_search)
    RelativeLayout llSearch;
    public boolean isVisible = false;
    @Bind(R.id.text_title)
    TextView textTitle;

    private ArrayList<WorkOrder> adaptList = new ArrayList<>();
    protected ArrayList<WorkOrder> list = new ArrayList<>();
    WorkOrderAdapter adapter;
    private int curTab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_order);
        ButterKnife.bind(this);
        OdfboxApplication.addActivity(this);
        setTitleTextView("我的工单", null);
        final String[] workState = getResources().getStringArray(R.array.state);
        final String[] workType = getResources().getStringArray(R.array.workType);
        ArrayAdapter<String> state = new ArrayAdapter<>(this, R.layout.lib_tv_spinner, workState);
        ArrayAdapter<String> type = new ArrayAdapter<>(this, R.layout.lib_tv_spinner, workType);
        state.setDropDownViewResource(android.R.layout.simple_list_item_1);
        type.setDropDownViewResource(android.R.layout.simple_list_item_1);
        //绑定 Adapter到控件
        spinner1.setAdapter(state);
        spinner2.setAdapter(type);
        spinner1.setSelection(0, true);
        spinner2.setSelection(0, true);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                tv.setTextColor(getResources().getColor(R.color.text_grey));    //设置颜色
                tv.setTextSize(12.0f);    //设置大小
                tv.setGravity(Gravity.CENTER_HORIZONTAL);   //设置居中
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                tv.setTextColor(getResources().getColor(R.color.text_grey));    //设置颜色
                tv.setTextSize(12.0f);    //设置大小
                tv.setGravity(Gravity.CENTER_HORIZONTAL);   //设置居中
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        adapter = new WorkOrderAdapter(mContext, adaptList);
        listview.setAdapter(adapter);
        checkboxUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curTab == 1) {
                    checkboxUndo.setBackgroundResource(R.mipmap.ic_check);
                    checkboxSearch.setBackgroundResource(R.mipmap.ic_uncheck);
                    llSearch.setVisibility(View.GONE);
                    isVisible = false;
                    page_number = 0;
                    adaptList.clear();
                    adapter.notifyDataSetChanged();
                    textTitle.setText("工单,共计" + 0 + "条");
                    getList("");
                    if (listview.getFooterViewsCount() > 0) {
                        listview.removeFooterView(footer);
                    }
                    curTab = 0;
                }
            }
        });
        checkboxSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curTab == 0) {
                    checkboxSearch.setBackgroundResource(R.mipmap.ic_check);
                    checkboxUndo.setBackgroundResource(R.mipmap.ic_uncheck);
                    llSearch.setVisibility(View.VISIBLE);
                    isVisible = true;
                    adaptList.clear();
                    adapter.notifyDataSetChanged();
                    textTitle.setText("工单,共计" + 0 + "条");
                    if (listview.getFooterViewsCount() > 0) {
                        listview.removeFooterView(footer);
                    }
                    curTab = 1;
                }
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adaptList.clear();
                page_number = 0;
                getList("&serial=" + code.getText().toString() + "&min_appoint_time=" + starttime.getText().toString() + "&max_appoint_time=" + endtime.getText().toString() + "&state=" + spinner1.getSelectedItem().toString() + "&task_sheet_type=" + spinner2.getSelectedItem().toString());
            }
        });

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        starttime.setText(formatter.format(c.getTime()));
        String str = formatter.format(Calendar.getInstance().getTime());
        endtime.setText(str);
        starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                View dataview = View.inflate(mContext, R.layout.time_layout, null);
                final DatePicker timePicker = (DatePicker) dataview.findViewById(R.id.time_picker);
                final Button commit = (Button) dataview.findViewById(R.id.commit);
                final Button cancle = (Button) dataview.findViewById(R.id.cancel);
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
                        starttime.setText(timePicker.getYear() + "-" + month + "-" + timePicker.getDayOfMonth());
                    }
                });
                cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        endtime.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                           View dataview = View.inflate(mContext, R.layout.time_layout, null);
                                           final DatePicker timePicker = (DatePicker) dataview.findViewById(R.id.time_picker);
                                           final Button commit = (Button) dataview.findViewById(R.id.commit);
                                           final Button cancle = (Button) dataview.findViewById(R.id.cancel);
                                           builder.setView(dataview);
                                           builder.setTitle("选择结束时间");
                                           final Dialog dialog = builder.create();
                                           Calendar cal = Calendar.getInstance();
                                           cal.setTimeInMillis(System.currentTimeMillis());
                                           timePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
                                           commit.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   dialog.dismiss();
                                                   int month = timePicker.getMonth() + 1;
                                                   endtime.setText(timePicker.getYear() + "-" + month + "-" + timePicker.getDayOfMonth());
                                               }
                                           });
                                           cancle.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   dialog.dismiss();
                                               }
                                           });
                                           dialog.show();
                                       }

                                   }

        );


        if (getIntent() != null) {
            String serail = getIntent().getStringExtra("serial");
            if (serail != null) {
                code.setText(serail);
                checkboxUndo.setBackgroundResource(R.mipmap.ic_check);
                checkboxSearch.setBackgroundResource(R.mipmap.ic_uncheck);
                llSearch.setVisibility(View.VISIBLE);
                isVisible = true;
                adaptList.clear();
                adapter.notifyDataSetChanged();
                getList("&serial=" + code.getText().toString());
            } else {
                initData();
            }
        }

        //条目点击进入webview详情
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WorkOrder workOrder = adapter.getItem(position - 1);
                Intent intent = new Intent(mContext, ControlOrderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("workOrder", workOrder);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

    }


    public void getList(String string) {
        cancelmDialog();
        showProgress(0, true);
        client.getWorkOrderList(mContext, (page_number++) * 10 + "", string, new WorkOrderHandler() {
            @Override
            public void onCancel() {
                super.onCancel();
                cancelmDialog();
            }

            @Override
            public void onSuccess(WorkOrderList commen) {
                super.onSuccess(commen);
                cancelmDialog();
                list = commen.results;
                if (commen.count > 10) {
                    textTitle.setText("工单,共计" + (page_number) * 10 + "条");
                } else {
                    textTitle.setText("工单,共计" + commen.count + "条");
                }
                if (list.size() > 0) {
                    if (listview.getFooterViewsCount() == 0) {
                        listview.addFooterView(footer);
                        listview.setAdapter(adapter);
                    }
                    for (Iterator<WorkOrder> iterator = list.iterator(); iterator.hasNext(); ) {
                        WorkOrder disscussCommentItems = iterator.next();
                        adaptList.add(disscussCommentItems);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    showToast("已无更多内容");
                    if (listview.getFooterViewsCount() > 0) {
                        listview.removeFooterView(footer);
                    }

                }
                if (commen.count < 10) {
                    if (listview.getFooterViewsCount() > 0) {
                        listview.removeFooterView(footer);
                    }
                }

                loadStatus = false;
                listview.onRefreshComplete();
                loadingProgressBar.setVisibility(View.GONE);
                loadingText.setText(getResources().getString(R.string.loadmore));
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                cancelmDialog();
                listview.onRefreshComplete();
                loadStatus = false;
                loadingProgressBar.setVisibility(View.GONE);
            }

        });
    }

    public void initData() {
        getList("");
        //点击角布局加载更多
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loadStatus && list.size() != 0) {
                    if (isVisible) {
                        getList("&serial=" + code.getText().toString() + "&min_appoint_time=" + starttime.getText().toString() + "&max_appoint_time=" + endtime.getText().toString() + "&state_category=" + spinner1.getSelectedItem().toString() + "&task_sheet_type=" + spinner2.getSelectedItem().toString());
                    } else {
                        getList("");
                    }

                }
            }
        });

        //上拉listview加载更多监听
        loadMoreListener = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (loadMore && !loadStatus) {
                        if (isVisible) {
                            getList("&serial=" + code.getText().toString() + "&min_appoint_time=" + starttime.getText().toString() + "&max_appoint_time=" + endtime.getText().toString() + "&state_category=" + spinner1.getSelectedItem().toString() + "&task_sheet_type=" + spinner2.getSelectedItem().toString());
                        } else {
                            getList("");
                        }
                    }
                    if (list.size() == 0) {
                        listview.removeFooterView(footer);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                listview.setFirstItemIndex(firstVisibleItem);
                if (firstVisibleItem != 1 && list.size() != 0) {
                    loadMore = firstVisibleItem + visibleItemCount == totalItemCount;
                } else {
                    loadMore = false;
                }
            }
        };
        listview.setOnScrollListener(loadMoreListener);
        listview.setOnRefreshListener(new DownPullRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (listview.getFooterViewsCount() > 0) {
                    listview.removeFooterView(footer);
                }
                page_number = 0;
                adaptList.clear();
                if (isVisible) {
                    getList("&serial=" + code.getText().toString() + "&min_appoint_time=" + starttime.getText().toString() + "&max_appoint_time=" + endtime.getText().toString() + "&state_category=" + spinner1.getSelectedItem().toString() + "&task_sheet_type=" + spinner2.getSelectedItem().toString());
                } else {
                    getList("");
                }
            }
        });


    }
}
