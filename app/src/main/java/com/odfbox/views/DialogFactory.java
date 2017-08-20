package com.odfbox.views;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.odfbox.OdfboxApplication;
import com.odfbox.R;
import com.odfbox.entity.VersionOdfbox;
import com.odfbox.utils.StringUtils;


public class DialogFactory {

    private static DialogFactory factory = null;

    public static synchronized DialogFactory getDialogFactory() {

        if (null == factory) {
            factory = new DialogFactory();
            return factory;
        } else {
            return factory;
        }

    }

    public Dialog showCommonDialog(Context context, String msg, String leftbtnStr, String rightbtnStr, View.OnClickListener leftBtnClickListener, View.OnClickListener rightBtnClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_common_ui, null);
        final Dialog customDialog = new Dialog(context, R.style.commonDialog);
        WindowManager.LayoutParams localLayoutParams = customDialog.getWindow().getAttributes();
        customDialog.onWindowAttributesChanged(localLayoutParams);
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.setCancelable(true);
        customDialog.setContentView(view);
        TextView span_view = (TextView) view.findViewById(R.id.span_view);
        TextView msgtv = (TextView) view.findViewById(R.id.tv_msg);
        msgtv.setText(msg);
        // left btn
        TextView btnLeft = (TextView) view.findViewById(R.id.btn_left);
        if (StringUtils.isEmpty(leftbtnStr)) {
            btnLeft.setVisibility(View.GONE);
            span_view.setVisibility(View.GONE);
        } else {
            btnLeft.setText(leftbtnStr);
            if (leftBtnClickListener != null) {
                btnLeft.setOnClickListener(leftBtnClickListener);
            } else {
                btnLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });
            }
        }
        // right btn
        TextView btnRight = (TextView) view.findViewById(R.id.btn_right);
        if (StringUtils.isEmpty(rightbtnStr)) {
            btnRight.setVisibility(View.GONE);
            span_view.setVisibility(View.GONE);
        } else {
            btnRight.setText(rightbtnStr);
            if (rightBtnClickListener != null) {
                btnRight.setOnClickListener(rightBtnClickListener);
            } else {
                btnRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });
            }
        }

        customDialog.show();
        return customDialog;
    }


    public Dialog showUpdateVersionDialog(Context context, VersionOdfbox check, View.OnClickListener leftBtnClickListener, View.OnClickListener rightBtnClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.update_version, null);
        final Dialog customDialog = new Dialog(context, R.style.commonDialog);
        WindowManager.LayoutParams localLayoutParams = customDialog.getWindow().getAttributes();
        customDialog.onWindowAttributesChanged(localLayoutParams);
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.setCancelable(true);
        customDialog.setContentView(view);
        TextView content = (TextView) view.findViewById(R.id.update_content);
        if (check != null && check.results.get(0).comment != "") {
            content.setText("更新说明：" + check.results.get(0).comment);
        }
        TextView btnLeft = (TextView) view.findViewById(R.id.update_later);
        TextView btnRight = (TextView) view.findViewById(R.id.update_now);
        TextView current_version = (TextView) view.findViewById(R.id.current_version);
        TextView recent_version = (TextView) view.findViewById(R.id.recent_version);
        if (check.results.get(0).revision.contains("v") || check.results.get(0).revision.contains("V")) {
            recent_version.setText("最新版本" + check.results.get(0).revision);
        } else {
            recent_version.setText("最新版本v" + check.results.get(0).revision);
        }
        current_version.setText("当前版本v" + OdfboxApplication.getAppVersions());
        btnLeft.setOnClickListener(leftBtnClickListener);
        btnRight.setOnClickListener(rightBtnClickListener);
        customDialog.show();
        return customDialog;
    }

}
