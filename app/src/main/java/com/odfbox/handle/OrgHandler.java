package com.odfbox.handle;

import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.odfbox.OdfboxApplication;
import com.odfbox.entity.Org;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class OrgHandler extends BaseResponseHandler {

    public OrgHandler() {
        this(DEFAULT_CHARSET);
    }

    public OrgHandler(String encoding) {
        super(encoding);
    }

    public void onSuccess(Org org) {

    }


    public void onFailure(String msg) {
        Toast.makeText(OdfboxApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(final int statusCode, Header[] headers, final JSONObject response) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                postRunnable(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            if (statusCode == 200 || statusCode == 201) {
                                onSuccess(JSON.parseObject(response.toString(), Org.class));
                            } else {
                                onFailure(response.getString("detail"));
                            }
                        } catch (JSONException e) {
                            onFailure(e.getMessage());
                        }
                    }
                });
            }
        }).start();
    }


    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);
        onFailure(responseString);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        onFailure(throwable.getMessage());
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        onFailure(throwable.getMessage());
    }
}
