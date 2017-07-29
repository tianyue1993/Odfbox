package com.odfbox.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.odfbox.ApiClient;
import com.odfbox.R;
import com.odfbox.utils.GlobalSetting;
import com.odfbox.views.ProgressDialog;


/**
 * Created by tianyue on 2017/3/23.
 */
public class BaseActivity extends Activity {

    //标题
    private TextView title;
    //左边按钮
    private ImageView leftTextView;
    //右边按钮
    private ImageView rightImageView;
    private TextView rightTextView, rightText;
    //外部布局
    private RelativeLayout allRelativeLayout;
    //标题布局
    private RelativeLayout titleRelativeLayout;
    String tag = getClass().getSimpleName();
    public Context mContext;
    public GlobalSetting prefs;
    public ApiClient client;

    public int page_number = 0;
    public AbsListView.OnScrollListener loadMoreListener;
    public boolean loadMore;
    public boolean loadStatus = false;
    public View footer;
    public ProgressBar loadingProgressBar;
    public TextView loadingText;

    public ProgressDialog mProgress;
    public static final int MSG_CLOSE_PROGRESS = 1;
    public static final int MSG_SHOW_TOAST = 2;
    public Handler baseHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_CLOSE_PROGRESS:
                    // cancelProgress(); //加载进度条
                    break;
                case MSG_SHOW_TOAST:
                    int resid = msg.arg1;
                    if (resid > 0) {
                        showToast(resid);
                    } else if (msg.obj != null) {
                        String mes = (String) msg.obj;
                        if (!TextUtils.isEmpty(mes)) {
                            showToast(mes);
                        }
                    }
                    break;
            }
        }

        ;
    };
    Toast toast = null;


    /**
     * 吐司提示，防止多个提示同时弹出时的崩溃
     *
     * @param message
     */
    protected void showToast(String message) {
        if (toast != null) {
            return;
        }
        toast = Toast.makeText(mContext, (!TextUtils.isEmpty(message)) ? message
                : this.getString(R.string.network_error), Toast.LENGTH_SHORT);
        toast.show();
        baseHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (toast != null) {
                    toast.cancel();
                    toast = null;
                }
            }
        }, 2000);
    }

    protected void showToast(int resid) {
        if (toast != null) {
            return;
        }
        toast = Toast.makeText(mContext, resid, Toast.LENGTH_SHORT);
        toast.show();
        baseHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (toast != null) {
                    toast.cancel();
                    toast = null;
                }
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        mContext = this;
        prefs = GlobalSetting.getInstance(mContext);
        client = ApiClient.getInstance();
        title = (TextView) findViewById(R.id.base_title);
        leftTextView = (ImageView) findViewById(R.id.base_left);
        rightImageView = (ImageView) findViewById(R.id.base_right);
        rightTextView = (TextView) findViewById(R.id.text_base_right);
        rightText = (TextView) findViewById(R.id.text_right);
        allRelativeLayout = (RelativeLayout) findViewById(R.id.all_base_layout);
        titleRelativeLayout = (RelativeLayout) findViewById(R.id.base_layout);
        leftTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        /**
         * 下拉列表相关布局变量
         */
        footer = View.inflate(mContext, R.layout.loadmore, null);
        loadingProgressBar = (ProgressBar) footer.findViewById(R.id.progressBar);
        loadingText = (TextView) footer.findViewById(R.id.title);

    }


    /**
     * 设置子布局
     */
    @Override
    public void setContentView(int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, null);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.BELOW, R.id.base_layout);
        if (null != allRelativeLayout)
            allRelativeLayout.addView(view, lp);
    }

    /**
     * 设置右边按钮图片
     */
    public void setRightImage(int id, View.OnClickListener onClickListener) {
        if (rightImageView != null) {
            if (rightImageView != null) {
                rightImageView.setVisibility(View.VISIBLE);
                if (id != 0) {
                    rightImageView.setImageResource(id);
                }
            }
            if (onClickListener != null) {
                rightImageView.setOnClickListener(onClickListener);
            }
        }
    }

    /**
     * 设置右边文字
     */
    public void setRightTextView(String string, View.OnClickListener onClickListener) {
        if (rightTextView != null) {
            if (rightTextView != null) {
                rightTextView.setVisibility(View.VISIBLE);
                if (!string.isEmpty()) {
                    rightTextView.setText(string);
                }
            }
            if (onClickListener != null) {
                rightTextView.setOnClickListener(onClickListener);
            }
        }
    }


    /**
     * 设置右边文字
     */
    public void setRightText(String string, View.OnClickListener onClickListener) {
        if (rightText != null) {
            if (rightText != null) {
                rightText.setVisibility(View.VISIBLE);
                if (!string.isEmpty()) {
                    rightText.setText(string);
                } else {
                    rightText.setText("");
                }
            } else {
                rightText.setVisibility(View.GONE);
            }
            if (onClickListener != null) {
                rightText.setOnClickListener(onClickListener);
            }
        }
    }

    private int type = 1;

    /**
     * 设置右边文字
     */
    public void setRightTextOrder(View.OnClickListener onClickListener) {

        if (rightText != null) {
            if (rightText != null) {
                rightText.setVisibility(View.VISIBLE);
                rightText.setText("提交");
            }
            if (onClickListener != null) {
                rightText.setOnClickListener(onClickListener);
                if (type == 1) {
                    rightText.setText("取消");
                    type = 2;
                } else if (type == 2) {
                    rightText.setText("提交");
                }

            }
        }
    }

    /**
     * 设置左边按钮
     */
    protected void setLeftTextView(int id, View.OnClickListener onClickListener) {
        if (leftTextView != null) {
            leftTextView.setVisibility(View.VISIBLE);
            if (id != 0) {
                leftTextView.setImageResource(id);
            }
            if (onClickListener != null) {
                leftTextView.setOnClickListener(onClickListener);
            }
        }
    }

    /**
     * 设置标题
     */
    public void setTitleTextView(String string, View.OnClickListener onClickListener) {
        if (title != null) {
            titleRelativeLayout.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            if (string != null) {
                title.setText(string);
            }
            if (onClickListener != null) {
                title.setOnClickListener(onClickListener);
            }
        }
    }

    //获取右边
    protected TextView getRightTextView() {
        return rightTextView;
    }


    public void showProgress(int resId, boolean cancel) {
        mProgress = new ProgressDialog(this);
        if (resId <= 0) {
            mProgress.setMessage(R.string.loading_data, cancel);
        } else {
            mProgress.setMessage(resId, cancel);
        }
        mProgress.show();
    }

    public void cancelmDialog() {
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }



}
