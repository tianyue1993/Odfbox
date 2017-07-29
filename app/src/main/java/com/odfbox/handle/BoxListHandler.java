package com.odfbox.handle;

import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.odfbox.OdfboxApplication;
import com.odfbox.entity.BoxList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;


public class BoxListHandler extends BaseResponseHandler {

    public BoxListHandler() {
        this(DEFAULT_CHARSET);
    }

    public BoxListHandler(String encoding) {
        super(encoding);
    }

    public void onSuccess(BoxList boxList) {

    }

    public void onFailure() {
        Toast.makeText(OdfboxApplication.getContext(), "操作失败", Toast.LENGTH_SHORT).show();
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
                            onSuccess(JSON.parseObject(response.toString(), BoxList.class));
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
