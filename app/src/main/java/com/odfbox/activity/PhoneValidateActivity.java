package com.odfbox.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.odfbox.ApiClient;
import com.odfbox.MainActivity;
import com.odfbox.OdfboxApplication;
import com.odfbox.R;
import com.odfbox.entity.AccessKey;
import com.odfbox.entity.Org;
import com.odfbox.handle.AccessKeyHandler;
import com.odfbox.handle.CommentHandler;
import com.odfbox.handle.OrgHandler;
import com.odfbox.utils.StringUtils;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhoneValidateActivity extends BaseActivity implements TextWatcher {

    @Bind(R.id.phone_number)
    EditText phoneNumber;
    @Bind(R.id.validate_code)
    EditText validateCode;
    @Bind(R.id.send_code)
    Button sendCode;
    @Bind(R.id.sure)
    Button sure;
    @Bind(R.id.delete)
    ImageView delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_validate);
        OdfboxApplication.addActivity(this);
        ButterKnife.bind(this);
        setTitleTextView("手机验证", null);
        // 获取编辑框焦点
        phoneNumber.setFocusable(true);
        //打开软键盘
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        phoneNumber.addTextChangedListener(this);

    }


    @OnClick({R.id.send_code, R.id.sure, R.id.delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.send_code:
                if (phoneNumber.getText().toString().trim().length() == 11) {
                    toGetPhoneCode();
                } else {
                    showToast("您输入的号码格式不对");
                }

                break;
            case R.id.sure:
                if (phoneNumber.getText().toString().trim().toString().length() == 11 && validateCode.getText().toString().trim().length() > 0) {
                    toGetAccessKey();
                } else {
                    showToast("请检查手机号或者验证码");
                }
                break;
            case R.id.delete:
                phoneNumber.setText("");
                break;
        }
    }


    //获取短信
    private void toGetPhoneCode() {
        showProgress(0, true);
        client.getPhoneCode(mContext, phoneNumber.getText().toString().trim(), new CommentHandler() {
            @Override
            public void onCancel() {
                super.onCancel();
                cancelmDialog();
            }

            @Override
            public void onSuccess(String getCode) {
                super.onSuccess(getCode);
                cancelmDialog();
                showToast("发送成功");
                timer.start();

            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                cancelmDialog();
            }
        });
    }

    // 注册 刷新验证码 倒计时
    CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            sendCode.setText(millisUntilFinished / 1000 + "秒可重发");
            sendCode.setClickable(false);
            sendCode.setBackgroundColor(getResources().getColor(R.color.button_grey));
            sendCode.setTextColor(getResources().getColor(R.color.text_grey));

        }

        @Override
        public void onFinish() {
            sendCode.setClickable(true);
            sendCode.setText(R.string.reget_check_code);
            sendCode.setTextColor(getResources().getColor(R.color.white));
            sendCode.setBackgroundColor(getResources().getColor(R.color.button_bg));
        }
    };

    public void toGetAccessKey() {
        showProgress(0, true);
        client.getAccess(mContext, phoneNumber.getText().toString().trim(), validateCode.getText().toString().trim(), new AccessKeyHandler() {
            @Override
            public void onSuccess(AccessKey commen) {
                super.onSuccess(commen);
                cancelmDialog();
                prefs.saveUserId(commen.orgs.get(0).user_id);
                prefs.saveUserPhone(phoneNumber.getText().toString().trim());
                if (commen.orgs.size() == 1) {
                    prefs.saveOrgId(commen.orgs.get(0).org_id + "");
                    prefs.saveUserId(commen.orgs.get(0).user_id);
                    prefs.saveOrgName(commen.orgs.get(0).name);
                    prefs.saveBannerImage(commen.orgs.get(0).app_banner_image);
                    getToken(commen.access_key);
                } else {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("access", commen);
                    intent.putExtras(bundle);
                    intent.setClass(mContext, ChooseCompanyActivity.class);
                    startActivity(intent);
                    finish();
                }
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

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * 根据输入框，显示删除图标
     * 下一步按钮是否可点击
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!StringUtils.isEmpty(phoneNumber.getText().toString().trim())) {
            delete.setVisibility(View.VISIBLE);
        } else {
            delete.setVisibility(View.GONE);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void getToken(String access_key) {
        showProgress(0, true);
        client.getToken(mContext, access_key, prefs.getOrgId(), new CommentHandler() {
            @Override
            public void onCancel() {
                super.onCancel();
                cancelmDialog();
            }

            @Override
            public void onSuccess(String commen) {
                super.onSuccess(commen);
                cancelmDialog();
                JSONObject object = JSON.parseObject(commen);
                prefs.saveToken(object.getString("token"));
                ApiClient.getInstance().asyncHttpClient.addHeader("Authorization", prefs.getToken());
                getOrg(prefs.getOrgId());
            }


            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                cancelmDialog();
            }
        });

    }


    public void getOrg(String org_id) {
        client.getOrg(mContext, org_id, new OrgHandler() {
            @Override
            public void onSuccess(Org org) {
                super.onSuccess(org);
                if (org.odf_box_monitored != 0f && org.total_odf_box != 0f) {
                    float percent = (org.odf_box_monitored / (float) org.total_odf_box);
                    NumberFormat nt = NumberFormat.getPercentInstance();
                    //设置百分数精确度2即保留两位小数
                    nt.setMinimumFractionDigits(2);
                    //最后格式化并输出
                    prefs.saveBoxData(nt.format(percent), org.total_odf_box + "");
                }
                prefs.saveOrgName(org.name);
                login();

            }
        });
    }

    public void login() {
        SharedPreferences preferences = mContext.getSharedPreferences("CHANNELID", Context.MODE_PRIVATE);
        JSONObject object = new JSONObject();
        object.put("action", "login");
        object.put("platform", "Android");
        object.put("channel_id_baidu", preferences.getString("channelid", ""));
        try {
            showProgress(0, true);
            StringEntity entity = new StringEntity(object.toString(), "UTF-8");
            client.getLoginState(mContext, entity, new CommentHandler() {
                @Override
                public void onSuccess(String commen) {
                    super.onSuccess(commen);
                    showToast("登陆成功");
                    startActivity(new Intent(mContext, MainActivity.class));
                    finish();
                }

                @Override
                public void onFailure(String msg) {
                    super.onFailure(msg);
                    showToast("登陆失败");
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
