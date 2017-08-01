package com.odfbox.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.odfbox.OdfboxApplication;
import com.odfbox.R;
import com.odfbox.adapter.WarnsAdapter;
import com.odfbox.entity.Warns;
import com.odfbox.entity.WarnsList;
import com.odfbox.handle.WarnHandler;
import com.odfbox.views.DownPullRefreshListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;


public class WarnListActivity extends BaseActivity {


    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.checkbox_order)
    CheckBox checkboxOrder;
    @Bind(R.id.checkbox_warns)
    CheckBox checkboxWarns;
    @Bind(R.id.ll_type)
    LinearLayout llType;
    @Bind(R.id.listview)
    DownPullRefreshListView listview;
    @Bind(R.id.tv_code)
    TextView tvCode;
    @Bind(R.id.start_time)
    TextView start_time;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.end_time)
    TextView end_time;
    @Bind(R.id.jwd)
    TextView jwd;
    @Bind(R.id.tvname)
    TextView tvname;
    @Bind(R.id.ed_jd)
    EditText edJd;
    @Bind(R.id.ed_wd)
    EditText edWd;
    @Bind(R.id.search)
    Button search;
    @Bind(R.id.ll_search)
    RelativeLayout llSearch;
    private ArrayList<Warns> adaptList = new ArrayList<>();
    protected ArrayList<Warns> list = new ArrayList<Warns>();
    private WarnsAdapter mAdapter;
    public boolean isVisible = true;
    public boolean alrming = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warn);
        ButterKnife.bind(this);
        OdfboxApplication.addActivity(this);
        setTitleTextView("事件/告警", null);
        setRightImage(R.mipmap.ic_white_search, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible) {
                    isVisible = false;
                    llSearch.setVisibility(View.VISIBLE);
                    adaptList.clear();
                    mAdapter.notifyDataSetChanged();
                } else {
                    isVisible = true;
                    llSearch.setVisibility(View.GONE);
                }
            }
        });

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        start_time.setText(formatter.format(c.getTime()));
        String str = formatter.format(Calendar.getInstance().getTime());
        end_time.setText(str);
        start_time.setOnClickListener(new View.OnClickListener() {
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
                        start_time.setText(timePicker.getYear() + "-" + month + "-" + timePicker.getDayOfMonth());
                    }
                });
                dialog.show();
            }
        });

        end_time.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                            View dataview = View.inflate(mContext, R.layout.time_layout, null);
                                            final DatePicker timePicker = (DatePicker) dataview.findViewById(R.id.time_picker);
                                            final Button commit = (Button) dataview.findViewById(R.id.commit);
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
                                                    end_time.setText(timePicker.getYear() + "-" + month + "-" + timePicker.getDayOfMonth());
                                                }
                                            });
                                            dialog.show();
                                        }

                                    }

        );


        checkboxWarns.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

                                                 {
                                                     @Override
                                                     public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                         if (isChecked) {
                                                             checkboxOrder.setChecked(false);
                                                             adaptList.clear();
                                                             page_number = 0;
                                                             alrming = true;
                                                             if (isVisible) {
                                                                 getList("");
                                                             } else {
                                                                 getList("&odf_box_serial_like" + edJd.getText().toString() + "&odf_box_name_like" + edWd.getText().toString() + "&min_date=" + start_time.getText().toString() + "&max_date=" + end_time.getText().toString());
                                                             }
                                                         }
                                                     }
                                                 }

        );
        checkboxOrder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

                                                 {
                                                     @Override
                                                     public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                         if (isChecked) {
                                                             checkboxWarns.setChecked(false);
                                                             adaptList.clear();
                                                             page_number = 0;
                                                             alrming = false;
                                                             if (isVisible) {
                                                                 getList("");
                                                             } else {
                                                                 getList("&odf_box_serial_like" + edJd.getText().toString() + "&odf_box_name_like" + edWd.getText().toString() + "&min_date=" + start_time.getText().toString() + "&max_date=" + end_time.getText().toString());
                                                             }
                                                         }
                                                     }
                                                 }

        );

        initData();

        search.setOnClickListener(new View.OnClickListener()

                                  {
                                      @Override
                                      public void onClick(View v) {
                                          adaptList.clear();
                                          page_number = 0;
                                          if (isVisible) {
                                              getList("");
                                          } else {
                                              getList("&odf_box_serial_like" + edJd.getText().toString() + "&odf_box_name_like" + edWd.getText().toString() + "&min_date=" + start_time.getText().toString() + "&max_date=" + end_time.getText().toString());
                                          }
                                      }
                                  }

        );
    }


    public void initData() {
        getList("");
        mAdapter = new WarnsAdapter(mContext, adaptList);
        listview.setAdapter(mAdapter);
        //点击角布局加载更多
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loadStatus && list.size() != 0) {
                    if (isVisible) {
                        getList("");
                    } else {
                        getList("&odf_box_serial_like" + edJd.getText().toString() + "&odf_box_name_like" + edWd.getText().toString() + "&min_date=" + start_time.getText().toString() + "&max_date=" + end_time.getText().toString());
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
                            getList("");
                        } else {
                            getList("&odf_box_serial_like" + edJd.getText().toString() + "&odf_box_name_like" + edWd.getText().toString() + "&min_date=" + start_time.getText().toString() + "&max_date=" + end_time.getText().toString());
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
                    getList("");
                } else {
                    getList("&odf_box_serial_like" + edJd.getText().toString() + "&odf_box_name_like" + edWd.getText().toString() + "&min_date=" + start_time.getText().toString() + "&max_date=" + end_time.getText().toString());
                }
            }
        });
        //条目点击进入webview详情
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Warns warns = mAdapter.getItem(position - 1);
                Intent intent = new Intent(mContext, WarnDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("info", warns);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

    }

    public void getList(String string) {
        showProgress(0, true);
        client.getWarnList(mContext, (page_number++) * 10 + "", string, alrming, new WarnHandler() {
            @Override
            public void onCancel() {
                super.onCancel();
                cancelmDialog();
            }

            @Override
            public void onSuccess(WarnsList commen) {
                super.onSuccess(commen);
                cancelmDialog();
                list = commen.results;
                textTitle.setText("事件和告警记录" + commen.count + "条");
                if (list.size() > 0) {
                    if (listview.getFooterViewsCount() == 0) {
                        listview.addFooterView(footer);
                        listview.setAdapter(mAdapter);
                    }
                    for (Iterator<Warns> iterator = list.iterator(); iterator.hasNext(); ) {
                        Warns disscussCommentItems = iterator.next();
                        adaptList.add(disscussCommentItems);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    showToast("已无更多内容");
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

}
