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

/**
 * 网络请求
 */
public class ApiClient {
    private static final String PROTOCOL_CHARSET = "utf-8";
    private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    public static AsyncHttpClient asyncHttpClient;

    private GlobalSetting pres;

    private volatile static ApiClient instance;

    public static ApiClient getInstance() {
        if (instance == null) {
            synchronized (ApiClient.class) {
                if (instance == null) {
                    instance = new ApiClient();
                    if (GlobalSetting.getInstance(OdfboxApplication.getContext()).getToken() != null && !GlobalSetting.getInstance(OdfboxApplication.getContext()).getToken().equals("")) {
                        instance.asyncHttpClient.addHeader("Authorization", GlobalSetting.getInstance(OdfboxApplication.getContext()).getToken());
                    }
                }
            }
        } else {
            if (GlobalSetting.getInstance(OdfboxApplication.getContext()).getToken() != null && !GlobalSetting.getInstance(OdfboxApplication.getContext()).getToken().equals("")) {
                instance.asyncHttpClient.addHeader("Authorization", GlobalSetting.getInstance(OdfboxApplication.getContext()).getToken());
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
        asyncHttpClient.addHeader("Authorization", GlobalSetting.getInstance(OdfboxApplication.getContext()).getToken());
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
        asyncHttpClient.get(context, OdfboxApplication.GETCODE(url), handler);
    }

    /**
     * 获取TOKEN
     */
    public void getToken(Context context, String url, String code, BaseResponseHandler handler) {
        asyncHttpClient.get(context, OdfboxApplication.GETTOKEN(url, code), handler);
    }

    /**
     * 获取Access
     */
    public void getAccess(Context context, String url, String code, BaseResponseHandler handler) {
        asyncHttpClient.get(context, OdfboxApplication.ACCESSKEY(url, code), handler);
    }

    public void getOrg(Context context, String code, BaseResponseHandler handler) {
        asyncHttpClient.get(context, OdfboxApplication.ORG(code), handler);
    }

    public void getWarnList(Context context, String offset, String string, boolean warn, BaseResponseHandler handler) {
        asyncHttpClient.get(context, OdfboxApplication.WARNLIST(offset, string, warn), handler);
        Log.d("WARNLIST", "getWarnList: WARNLIST" + OdfboxApplication.WARNLIST(offset, string, warn));
    }

    public void getMainWarnList(Context context, BaseResponseHandler handler) {
        asyncHttpClient.get(context, OdfboxApplication.MAINEVENT(), handler);
        Log.d("WARNLIST", "getWarnList: WARNLIST" + OdfboxApplication.MAINEVENT());
    }

    public void getBoxList(Context context, String offset, String parms, BaseResponseHandler handler) {
        asyncHttpClient.get(context, OdfboxApplication.BOXLIST(offset, parms).trim(), handler);
        Log.d("BOXLIST", " BOXLIST" + OdfboxApplication.BOXLIST(offset, parms));
    }

    public void getWorkOrderList(Context context, String offset, String parms, BaseResponseHandler handler) {
        asyncHttpClient.get(context, OdfboxApplication.WORKORDERLIST(offset, parms), handler);
        Log.d("WORKORDERLIST", " WORKORDERLIST" + OdfboxApplication.WORKORDERLIST(offset, parms));
    }

    public void getNearBox(Context context, String lat, String lon, String distance, BaseResponseHandler handler) {
        asyncHttpClient.get(context, OdfboxApplication.NEARBOX(lat, lon, distance), handler);
        Log.d("getNearBox", "getNearBox: " + OdfboxApplication.NEARBOX(lat, lon, distance));
    }

    public void getOpenBox(Context context, String lock_pk, StringEntity params, BaseResponseHandler handler) {
        asyncHttpClient.post(context, OdfboxApplication.OPENBOX(lock_pk), params, "application/json", handler);
        Log.d("getOpenBox", "getOpenBox: " + OdfboxApplication.OPENBOX(lock_pk));
    }

    public void getAddBox(Context context, StringEntity params, BaseResponseHandler handler) {
        asyncHttpClient.post(context, OdfboxApplication.ADDBOX(), params, "application/json", handler);
    }

    public void getEditBox(Context context, StringEntity params, String id, BaseResponseHandler handler) {
        asyncHttpClient.patch(context, OdfboxApplication.EDITBOX(id), params, "application/json", handler);
    }


    public void getPutOrder(Context context, String id, StringEntity params, BaseResponseHandler handler) {
        asyncHttpClient.post(context, OdfboxApplication.PUTORDER(id), params, "application/json", handler);
    }

    public void getPutTask(Context context, String id, String task_id, StringEntity params, BaseResponseHandler handler) {
        asyncHttpClient.post(context, OdfboxApplication.PUTTASKS(id, task_id), params, "application/json", handler);
    }


    public void getEventList(Context context, String id, boolean isWarns, BaseResponseHandler handler) {
        asyncHttpClient.get(context, OdfboxApplication.ENENTLIST(id, isWarns), handler);
    }

    public void getBoxDetail(Context context, String id, BaseResponseHandler handler) {
        asyncHttpClient.get(context, OdfboxApplication.BOXDETAIL(id), handler);
        Log.d("BOXDETAIL", "getBoxDetail: " + OdfboxApplication.BOXDETAIL(id));
    }

    public void getBoxCount(Context context, BaseResponseHandler handler) {
        asyncHttpClient.get(context, OdfboxApplication.BOXCOUNT(), handler);
    }

    public void getUserGuide(Context context, BaseResponseHandler handler) {
        asyncHttpClient.get(context, OdfboxApplication.USERGUIDE(), handler);
    }

    public void getTaskList(Context context, String id, BaseResponseHandler handler) {
        asyncHttpClient.get(context, OdfboxApplication.TASKLIST(id), handler);
    }

    public void getUserList(Context context, BaseResponseHandler handler) {
        asyncHttpClient.get(context, OdfboxApplication.GETUSER(), handler);
    }


    public void getPutEventOrder(Context context, StringEntity params, BaseResponseHandler handler) {
        asyncHttpClient.post(context, OdfboxApplication.EVENTORDER(), params, "application/json", handler);
    }

    public void getPutEventHandle(Context context, String id, StringEntity params, BaseResponseHandler handler) {
        asyncHttpClient.post(context, OdfboxApplication.HANDLE(id), params, "application/json", handler);
    }

    public void getConstantDefine(Context context, String type, BaseResponseHandler handler) {
        asyncHttpClient.get(context, OdfboxApplication.CONSTANTDEFINE(type), handler);
    }

    public void getUntreated(Context context, BaseResponseHandler handler) {
        asyncHttpClient.get(context, OdfboxApplication.UNTREATED(), handler);
    }

    public void getOrderUntreated(Context context, BaseResponseHandler handler) {
        asyncHttpClient.get(context, OdfboxApplication.ORDERUNTREATED(), handler);
    }


}
