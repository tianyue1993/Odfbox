package com.odfbox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.odfbox.OdfboxApplication;
import com.odfbox.R;
import com.odfbox.adapter.MyOdfboxAdapter;
import com.odfbox.entity.BoxList;
import com.odfbox.entity.Odfbox;
import com.odfbox.handle.BoxListHandler;
import com.odfbox.handle.CommentHandler;
import com.odfbox.utils.StringUtils;
import com.odfbox.views.DownPullRefreshListView;
import com.odfbox.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyBoxActivity extends BaseActivity {

    @Bind(R.id.total)
    TextView total;
    @Bind(R.id.warses)
    TextView warses;
    @Bind(R.id.offlines)
    TextView offlines;
    @Bind(R.id.openes)
    TextView openes;
    @Bind(R.id.count)
    TextView count;
    @Bind(R.id.listview)
    DownPullRefreshListView listview;
    @Bind(R.id.line1)
    ImageView line1;
    @Bind(R.id.tv_code)
    TextView tvCode;
    @Bind(R.id.ed_code)
    EditText edCode;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.ed_name)
    EditText edName;
    @Bind(R.id.tv_describe)
    TextView tvDescribe;
    @Bind(R.id.ed_describe)
    EditText edDescribe;
    @Bind(R.id.search)
    Button search;
    @Bind(R.id.ll_search)
    RelativeLayout llSearch;
    @Bind(R.id.line2)
    ImageView line2;
    @Bind(R.id.title)
    LinearLayout title;
    @Bind(R.id.ed_serail)
    EditText edSerail;
    @Bind(R.id.sacn)
    ImageView sacn;
    private ArrayList<Odfbox> adaptList = new ArrayList<>();
    protected ArrayList<Odfbox> list = new ArrayList<Odfbox>();
    public boolean isVisible = true;//
    MyOdfboxAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_box);
        OdfboxApplication.addActivity(this);
        ButterKnife.bind(this);
        setTitleTextView("我的光交箱", null);
        setRightImage(R.mipmap.ic_white_search, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listview.getFooterViewsCount() > 0) {
                    listview.removeFooterView(footer);
                }
                if (isVisible) {
                    isVisible = false;
                    llSearch.setVisibility(View.VISIBLE);
                    count.setText("共有0条记录");
                    adaptList.clear();
                    page_number = 0;
                    adapter.notifyDataSetChanged();
                } else {
                    isVisible = true;
                    llSearch.setVisibility(View.GONE);
                    adaptList.clear();
                    page_number = 0;
                    getList("");
                    adapter.notifyDataSetChanged();
                }

            }
        });

        initData();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adaptList.clear();
                page_number = 0;
                if (isVisible) {
                    getList("");
                } else {
                    String string = "";
                    if (StringUtils.isNotEmptyOrNull(edSerail.getText().toString().trim())) {
                        string = string + "&lock_serial=" + edSerail.getText().toString().trim();
                    }
                    if (StringUtils.isNotEmptyOrNull(edCode.getText().toString().trim())) {
                        string = string + "&serial_text_like=" + edCode.getText().toString().trim();
                    }
                    if (StringUtils.isNotEmptyOrNull(edCode.getText().toString().trim())) {
                        string = string + "&name_like=" + edName.getText().toString().trim();
                    }
                    if (StringUtils.isNotEmptyOrNull(edDescribe.getText().toString().trim())) {
                        string = string + "&address_like=" + edDescribe.getText().toString().trim();
                    }
                    getList(string);
                }
            }
        });

        sacn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, CaptureActivity.class), 5);
            }
        });


    }


    public void initData() {
        getCount();
        adapter = new MyOdfboxAdapter(mContext, adaptList);
        listview.setAdapter(adapter);
        //点击角布局加载更多
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loadStatus && list.size() != 0) {
                    if (isVisible) {
                        getList("");
                    } else {
                        String string = "";
                        if (StringUtils.isNotEmptyOrNull(edSerail.getText().toString().trim())) {
                            string = string + "&lock_serial=" + edSerail.getText().toString().trim();
                        }
                        if (StringUtils.isNotEmptyOrNull(edCode.getText().toString().trim())) {
                            string = string + "&serial_text_like=" + edCode.getText().toString().trim();
                        }
                        if (StringUtils.isNotEmptyOrNull(edCode.getText().toString().trim())) {
                            string = string + "&name_like=" + edName.getText().toString().trim();
                        }
                        if (StringUtils.isNotEmptyOrNull(edDescribe.getText().toString().trim())) {
                            string = string + "&address_like=" + edDescribe.getText().toString().trim();
                        }
                        getList(string);
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
                            String string = "";
                            if (StringUtils.isNotEmptyOrNull(edSerail.getText().toString().trim())) {
                                string = string + "&lock_serial=" + edSerail.getText().toString().trim();
                            }
                            if (StringUtils.isNotEmptyOrNull(edCode.getText().toString().trim())) {
                                string = string + "&serial_text_like=" + edCode.getText().toString().trim();
                            }
                            if (StringUtils.isNotEmptyOrNull(edCode.getText().toString().trim())) {
                                string = string + "&name_like=" + edName.getText().toString().trim();
                            }
                            if (StringUtils.isNotEmptyOrNull(edDescribe.getText().toString().trim())) {
                                string = string + "&address_like=" + edDescribe.getText().toString().trim();
                            }
                            getList(string);
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
                    String string = "";
                    if (StringUtils.isNotEmptyOrNull(edSerail.getText().toString().trim())) {
                        string = string + "&lock_serial=" + edSerail.getText().toString().trim();
                    }
                    if (StringUtils.isNotEmptyOrNull(edCode.getText().toString().trim())) {
                        string = string + "&serial_text_like=" + edCode.getText().toString().trim();
                    }
                    if (StringUtils.isNotEmptyOrNull(edCode.getText().toString().trim())) {
                        string = string + "&name_like=" + edName.getText().toString().trim();
                    }
                    if (StringUtils.isNotEmptyOrNull(edDescribe.getText().toString().trim())) {
                        string = string + "&address_like=" + edDescribe.getText().toString().trim();
                    }
                    getList(string);
                }
            }
        });

        //条目点击进入webview详情
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Odfbox recommendItems = adapter.getItem(position - 1);
                Intent intent = new Intent(mContext, BoxDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("info", recommendItems);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

    }


    public void getCount() {
        client.getBoxCount(mContext, new CommentHandler() {
            @Override
            public void onSuccess(String commen) {
                super.onSuccess(commen);
                JSONObject object = JSON.parseObject(commen);
                total.setText("总数：" + object.get("total"));
                warses.setText("报警数：" + object.get("alarming"));
                offlines.setText("离线数：" + object.get("offline"));
                openes.setText("打开数：" + object.get("opening"));
            }
        });
    }

    public void getList(String string) {
        showProgress(0, true);
        client.getBoxList(mContext, (page_number++) * 10 + "", string, new BoxListHandler() {
            @Override
            public void onCancel() {
                super.onCancel();
                cancelmDialog();
            }

            @Override
            public void onSuccess(BoxList boxList) {
                super.onSuccess(boxList);
                cancelmDialog();
                list = boxList.results;
                count.setText("共有" + boxList.count + "条记录");
                if (list.size() > 0) {
                    if (listview.getFooterViewsCount() == 0) {
                        listview.addFooterView(footer);
                        listview.setAdapter(adapter);
                    }
                    for (Iterator<Odfbox> iterator = list.iterator(); iterator.hasNext(); ) {
                        Odfbox disscussCommentItems = iterator.next();
                        adaptList.add(disscussCommentItems);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    showToast("已无更多内容");
                    if (listview.getFooterViewsCount() > 0) {
                        listview.removeFooterView(footer);
                    }

                }
                if (boxList.count < 10) {
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
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isVisible) {
            getList("");
        } else {
            String string = "";
            if (StringUtils.isNotEmptyOrNull(edSerail.getText().toString().trim())) {
                string = string + "&lock_serial=" + edSerail.getText().toString().trim();
            }
            if (StringUtils.isNotEmptyOrNull(edCode.getText().toString().trim())) {
                string = string + "&serial_text_like=" + edCode.getText().toString().trim();
            }
            if (StringUtils.isNotEmptyOrNull(edCode.getText().toString().trim())) {
                string = string + "&name_like=" + edName.getText().toString().trim();
            }
            if (StringUtils.isNotEmptyOrNull(edDescribe.getText().toString().trim())) {
                string = string + "&address_like=" + edDescribe.getText().toString().trim();
            }
            getList(string);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == 5) {
                edSerail.setText(data.getStringExtra("code"));
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }

    }
}
