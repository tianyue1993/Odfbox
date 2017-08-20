package com.odfbox.push;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.odfbox.OdfboxApplication;
import com.odfbox.activity.WarnListActivity;
import com.odfbox.utils.GlobalSetting;

import java.util.List;

/**
 * Created by admin on 2017/7/7.
 */

public class PushReceiver extends PushMessageReceiver {

    @Override
    public void onUnbind(Context context, int i, String s) {

    }

    @Override
    public void onListTags(Context context, int i, List<String> list, String s) {

    }

    @Override
    public void onDelTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onBind(Context context, int i, String s, String s1, String s2, String s3) {
        Log.d(TAG, "onBind: channelid" + s2);
        if (i == 0) {
            GlobalSetting.getInstance(context).saveChannelId(s2);
        } else {
            Toast.makeText(OdfboxApplication.getContext(), "推送绑定失败", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onMessage(Context context, String s, String s1) {

    }

    @Override
    public void onNotificationArrived(Context context, String s, String s1, String s2) {

    }

    @Override
    public void onNotificationClicked(Context context, String s, String s1, String s2) {
        //点击通知栏跳转
        OdfboxApplication.getContext().startActivity(new Intent(OdfboxApplication.getContext(), WarnListActivity.class));

    }

    @Override
    public void onSetTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }
}
