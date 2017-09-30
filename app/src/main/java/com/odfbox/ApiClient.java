package com.odfbox;


import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.MySSLSocketFactory;
import com.odfbox.handle.BaseResponseHandler;
import com.odfbox.utils.GlobalSetting;

import org.apache.http.entity.StringEntity;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import static com.odfbox.OdfboxApplication.ACCESSKEY;
import static com.odfbox.OdfboxApplication.ADDBOX;
import static com.odfbox.OdfboxApplication.BOXCOUNT;
import static com.odfbox.OdfboxApplication.BOXDETAIL;
import static com.odfbox.OdfboxApplication.BOXLIST;
import static com.odfbox.OdfboxApplication.CONSTANTDEFINE;
import static com.odfbox.OdfboxApplication.EDITBOX;
import static com.odfbox.OdfboxApplication.ENENTLIST;
import static com.odfbox.OdfboxApplication.EVENTORDER;
import static com.odfbox.OdfboxApplication.GETCODE;
import static com.odfbox.OdfboxApplication.GETTOKEN;
import static com.odfbox.OdfboxApplication.GETUSER;
import static com.odfbox.OdfboxApplication.HANDLE;
import static com.odfbox.OdfboxApplication.LOGINSTATE;
import static com.odfbox.OdfboxApplication.MAINEVENT;
import static com.odfbox.OdfboxApplication.NEARBOX;
import static com.odfbox.OdfboxApplication.OPENBOX;
import static com.odfbox.OdfboxApplication.ORDERUNTREATED;
import static com.odfbox.OdfboxApplication.ORG;
import static com.odfbox.OdfboxApplication.PUTORDER;
import static com.odfbox.OdfboxApplication.PUTTASKS;
import static com.odfbox.OdfboxApplication.TASKLIST;
import static com.odfbox.OdfboxApplication.UNTREATED;
import static com.odfbox.OdfboxApplication.USERGUIDE;
import static com.odfbox.OdfboxApplication.VERSION;
import static com.odfbox.OdfboxApplication.WARNLIST;
import static com.odfbox.OdfboxApplication.WORKORDERLIST;
import static com.odfbox.OdfboxApplication.getContext;

/**
 * 网络请求
 */
public class ApiClient {
    public static AsyncHttpClient asyncHttpClient;
    private GlobalSetting pres;
    private volatile static ApiClient instance;

    public static ApiClient getInstance() {
        if (instance == null) {
            synchronized (ApiClient.class) {
                if (instance == null) {
                    instance = new ApiClient();
                    if (GlobalSetting.getInstance(getContext()).getToken() != null && !GlobalSetting.getInstance(getContext()).getToken().equals("")) {
                        instance.asyncHttpClient.addHeader("Authorization", GlobalSetting.getInstance(getContext()).getToken());
                    }
                }
            }
        } else {
            if (GlobalSetting.getInstance(getContext()).getToken() != null && !GlobalSetting.getInstance(getContext()).getToken().equals("")) {
                instance.asyncHttpClient.addHeader("Authorization", GlobalSetting.getInstance(getContext()).getToken());
            }
        }
        return instance;
    }


    private ApiClient() {
        asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setURLEncodingEnabled(false);
        asyncHttpClient.setTimeout(10000);
        asyncHttpClient.setMaxRetriesAndTimeout(0, 5000);
        asyncHttpClient.addHeader("Content-Type", "application/json;charset=UTF-8");
        asyncHttpClient.addHeader("Authorization", GlobalSetting.getInstance(getContext()).getToken());
        try {
            asyncHttpClient.setSSLSocketFactory(new MySSLSocketFactory(KeyStore.getInstance("BKS")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取验证码
     */
    public void getPhoneCode(Context context, String url, BaseResponseHandler handler) {
        asyncHttpClient.get(context, GETCODE(url), handler);
    }

    /**
     * 获取TOKEN
     */
    public void getToken(Context context, String url, String code, BaseResponseHandler handler) {
        asyncHttpClient.get(context, GETTOKEN(url, code), handler);
    }

    /**
     * 获取Access
     */
    public void getAccess(Context context, String url, String code, BaseResponseHandler handler) {
        asyncHttpClient.get(context, ACCESSKEY(url, code), handler);
    }

    public void getOrg(Context context, String code, BaseResponseHandler handler) {
        asyncHttpClient.get(context, ORG(code), handler);
    }

    public void getWarnList(Context context, String offset, String string, boolean warn, BaseResponseHandler handler) {
        asyncHttpClient.get(context, WARNLIST(offset, string, warn), handler);
        Log.d("WARNLIST", "getWarnList: WARNLIST" + WARNLIST(offset, string, warn));
    }

    public void getMainWarnList(Context context, BaseResponseHandler handler) {
        asyncHttpClient.get(context, MAINEVENT(), handler);
        Log.d("WARNLIST", "getWarnList: WARNLIST" + MAINEVENT());
    }

    public void getBoxList(Context context, String offset, String parms, BaseResponseHandler handler) {
        asyncHttpClient.get(context, BOXLIST(offset, parms).trim(), handler);
        Log.d("BOXLIST", " BOXLIST" + BOXLIST(offset, parms));
    }

    public void getWorkOrderList(Context context, String offset, String parms, BaseResponseHandler handler) {
        asyncHttpClient.get(context, WORKORDERLIST(offset, parms), handler);
        Log.d("WORKORDERLIST", " WORKORDERLIST" + WORKORDERLIST(offset, parms));
    }

    public void getNearBox(Context context, String lat, String lon, String distance, BaseResponseHandler handler) {
        asyncHttpClient.get(context, NEARBOX(lat, lon, distance), handler);
        Log.d("getNearBox", "getNearBox: " + NEARBOX(lat, lon, distance));
    }

    public void getOpenBox(Context context, String lock_pk, StringEntity params, BaseResponseHandler handler) {
        asyncHttpClient.post(context, OPENBOX(lock_pk), params, "application/json", handler);
        Log.d("getOpenBox", "getOpenBox: " + OPENBOX(lock_pk));
    }

    public void getAddBox(Context context, StringEntity params, BaseResponseHandler handler) {
        asyncHttpClient.post(context, ADDBOX(), params, "application/json", handler);
    }

    public void getEditBox(Context context, StringEntity params, String id, BaseResponseHandler handler) {
        asyncHttpClient.patch(context, EDITBOX(id), params, "application/json", handler);
    }


    public void getPutOrder(Context context, String id, StringEntity params, BaseResponseHandler handler) {
        asyncHttpClient.post(context, PUTORDER(id), params, "application/json", handler);
    }

    public void getPutTask(Context context, String id, String task_id, StringEntity params, BaseResponseHandler handler) {
        asyncHttpClient.post(context, PUTTASKS(id, task_id), params, "application/json", handler);
    }


    public void getEventList(Context context, String id, boolean isWarns, BaseResponseHandler handler) {
        asyncHttpClient.get(context, ENENTLIST(id, isWarns), handler);
    }

    public void getBoxDetail(Context context, String id, BaseResponseHandler handler) {
        asyncHttpClient.get(context, BOXDETAIL(id), handler);
        Log.d("BOXDETAIL", "getBoxDetail: " + BOXDETAIL(id));
    }

    public void getBoxCount(Context context, BaseResponseHandler handler) {
        asyncHttpClient.get(context, BOXCOUNT(), handler);
    }

    public void getUserGuide(Context context, BaseResponseHandler handler) {
        asyncHttpClient.get(context, USERGUIDE(), handler);
    }

    public void getTaskList(Context context, String id, BaseResponseHandler handler) {
        asyncHttpClient.get(context, TASKLIST(id), handler);
    }

    public void getUserList(Context context, BaseResponseHandler handler) {
        asyncHttpClient.get(context, GETUSER(), handler);
    }


    public void getPutEventOrder(Context context, StringEntity params, BaseResponseHandler handler) {
        asyncHttpClient.post(context, EVENTORDER(), params, "application/json", handler);
    }

    public void getPutEventHandle(Context context, String id, StringEntity params, BaseResponseHandler handler) {
        asyncHttpClient.post(context, HANDLE(id), params, "application/json", handler);
    }

    public void getConstantDefine(Context context, String type, BaseResponseHandler handler) {
        asyncHttpClient.get(context, CONSTANTDEFINE(type), handler);
    }

    public void getUntreated(Context context, BaseResponseHandler handler) {
        asyncHttpClient.get(context, UNTREATED(), handler);
    }

    public void getOrderUntreated(Context context, BaseResponseHandler handler) {
        asyncHttpClient.get(context, ORDERUNTREATED(), handler);
    }

    public void getVersion(Context context, BaseResponseHandler handler) {
        asyncHttpClient.get(context, VERSION(), handler);
    }

    public void getLoginState(Context context, StringEntity params, BaseResponseHandler handler) {
        asyncHttpClient.put(context, LOGINSTATE(), params, "application/json", handler);
    }

    public void getStatistics(Context context, String params, BaseResponseHandler handler) {
        asyncHttpClient.get(context, OdfboxApplication.STATISTICS() + params + "/", handler);
    }

    public void getTaskStatistics(Context context, String params, BaseResponseHandler handler) {
        asyncHttpClient.get(context, OdfboxApplication.TASKSTATISTICS() + params + "-01/", handler);
    }
}
