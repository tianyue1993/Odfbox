package com.odfbox.views;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.odfbox.R;
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
        customDialog.setCanceledOnTouchOutside(true);
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


//    public Dialog showUpdateVersionDialog(Context context, String updatecontent, View.OnClickListener leftBtnClickListener, View.OnClickListener rightBtnClickListener) {
//        View view = LayoutInflater.from(context).inflate(R.layout.update_version, null);
//        final Dialog customDialog = new Dialog(context, R.style.commonDialog);
//        WindowManager.LayoutParams localLayoutParams = customDialog.getWindow().getAttributes();
//        customDialog.onWindowAttributesChanged(localLayoutParams);
//        customDialog.setCanceledOnTouchOutside(false);
//        customDialog.setCancelable(true);
//        customDialog.setContentView(view);
//        TextView content = (TextView) view.findViewById(R.id.update_content);
//        if (updatecontent != null && updatecontent != "") {
//            content.setText(updatecontent);
//        }
//        TextView btnLeft = (TextView) view.findViewById(R.id.update_later);
//        TextView btnRight = (TextView) view.findViewById(R.id.update_now);
//        btnLeft.setOnClickListener(leftBtnClickListener);
//        btnRight.setOnClickListener(rightBtnClickListener);
//        customDialog.show();
//        return customDialog;
//    }

}
