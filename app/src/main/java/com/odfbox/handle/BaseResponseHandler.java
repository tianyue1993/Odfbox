package com.odfbox.handle;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONTokener;

/**
 * 网络层封装
 * 请求网络的回调类
 */
public class BaseResponseHandler extends JsonHttpResponseHandler {

    public BaseResponseHandler() {
        super();
    }

    public BaseResponseHandler(String encoding) {
        super(encoding);
    }

    @Override
    protected Object parseResponse(byte[] responseBody) throws JSONException {
        if (null == responseBody)
            return null;
        Object result = null;
        String jsonString = getResponseString(responseBody, getCharset());


        /**
         *打印请求的链接和后台返回的jsonString
         */
        if (getRequestURI() != null) {
            Log.d("Reqeust:" + getRequestURI().toString(), jsonString);
        }

        if (jsonString != null) {
            jsonString = jsonString.trim();
            if (jsonString.startsWith(UTF8_BOM)) {
                jsonString = jsonString.substring(1);
            }
            if (jsonString.startsWith("{") || jsonString.startsWith("[")) {
                result = new JSONTokener(jsonString).nextValue();
            }
        }

        if (result == null) {
            result = jsonString;
        }
        return result;
    }
}
