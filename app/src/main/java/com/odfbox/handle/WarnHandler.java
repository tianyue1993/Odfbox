package com.odfbox.handle;

import com.alibaba.fastjson.JSON;
import com.odfbox.entity.WarnsList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;


public class WarnHandler extends BaseResponseHandler {

    public WarnHandler() {
        this(DEFAULT_CHARSET);
    }

    public WarnHandler(String encoding) {
        super(encoding);
    }

    public void onSuccess(WarnsList commen) {

    }

    public void onFailure() {
//        Toast.makeText(OdfboxApplication.getContext(), "操作失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(final int statusCode, Header[] headers, final JSONObject response) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        if (statusCode == 200) {
                            onSuccess(JSON.parseObject(response.toString(), WarnsList.class));
                        }
                    }
                });
            }
        }).start();
    }


    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);
        onFailure();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        onFailure();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        onFailure();
    }
}
