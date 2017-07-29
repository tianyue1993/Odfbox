package com.odfbox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.odfbox.R;
import com.odfbox.entity.WorkOrder;

import java.util.ArrayList;


/**
 * Created by admin on 2017/4/14.
 */

public class WorkOrderAdapter extends BoxBaseAdapter<WorkOrder> {

    public WorkOrderAdapter(Context context, ArrayList<WorkOrder> List) {
        super(context);
        this.mList = List;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final WorkOrder mInfo = mList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_work_order, null);
            holder.code = (TextView) convertView.findViewById(R.id.code);
            holder.state = (TextView) convertView.findViewById(R.id.state);
            holder.manager = (TextView) convertView.findViewById(R.id.manager);
            holder.type = (TextView) convertView.findViewById(R.id.type);
            holder.starttime = (TextView) convertView.findViewById(R.id.starttime);
            holder.endtime = (TextView) convertView.findViewById(R.id.endtime);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (mInfo != null) {
            if (mInfo.serial != null)
                holder.code.setText("工单号：" + mInfo.serial);
            if (mInfo.comment != null)
                holder.content.setText("工单内容：" + mInfo.comment);
            if (mInfo.state != null)
                holder.state.setText("状态：" + mInfo.state);
            if (mInfo.create_time != null)
                holder.starttime.setText("创建时间：" + mInfo.create_time);
            if (mInfo.accomplish_time != null)
                holder.endtime.setText("要求完成时间：" + mInfo.accomplish_time);
            if (mInfo.appoint_from_name != null)
                holder.manager.setText("发起人：" + mInfo.appoint_from_name);

        }

        return convertView;
    }


    static class ViewHolder {
        TextView code;
        TextView state;
        TextView manager;
        TextView type;
        TextView starttime;
        TextView endtime;
        TextView content;
    }
}
