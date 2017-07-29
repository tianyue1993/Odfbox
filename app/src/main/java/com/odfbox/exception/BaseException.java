package com.odfbox.exception;

import android.content.Intent;
import android.widget.Toast;

import com.odfbox.OdfboxApplication;
import com.odfbox.activity.LoginActivity;
import com.odfbox.utils.GlobalSetting;

/**
 * 异常信息处理Exception基类封装
 */
public class BaseException extends Exception {

    private static final long serialVersionUID = 1L;

    private int statusCode = -1;

    public BaseException(String msg) {
        super(msg);
    }

    public BaseException(Exception cause) {
        super(cause);
    }

    public BaseException(String msg, int statusCode) {
        super(msg);
        this.statusCode = statusCode;
        Toast.makeText(OdfboxApplication.getContext(), msg, Toast.LENGTH_LONG).show();

    }

    public BaseException(String msg, Exception cause) {
        super(msg, cause);
    }

    public BaseException(String msg, Exception cause, int statusCode) {
        super(msg, cause);
        this.statusCode = statusCode;

    }

    public BaseException(int code, String msg, Throwable throwable) {
        super(msg, throwable);
        this.statusCode = code;
    }

    public BaseException(int code, Throwable throwable) {
        super(throwable);
        this.statusCode = code;
        Toast.makeText(OdfboxApplication.getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
    }

    public int getStatusCode() {
        return this.statusCode;
    }


    /**
     * 表示当前账号被异地登录，返回登录页面
     */
    protected synchronized void exceptionCode(String string) {
        Toast.makeText(OdfboxApplication.getContext(), string, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(OdfboxApplication.getContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        OdfboxApplication.getContext().startActivity(intent);
        GlobalSetting.getInstance(OdfboxApplication.getContext()).clear();
    }

}
