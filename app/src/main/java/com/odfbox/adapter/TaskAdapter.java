package com.odfbox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.odfbox.R;
import com.odfbox.entity.Tasks;

import java.util.ArrayList;


/**
 * Created by admin on 2017/4/14.
 */

public class TaskAdapter extends BoxBaseAdapter<Tasks> {

    public TaskAdapter(Context context, ArrayList<Tasks> List) {
        super(context);
        this.mList = List;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final Tasks mInfo = mList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_task, null);
            holder.code = (TextView) convertView.findViewById(R.id.code);
            holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            holder.describle = (TextView) convertView.findViewById(R.id.describle);
            holder.complete = (TextView) convertView.findViewById(R.id.complete);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (mInfo != null) {
            if (mInfo.serial != null) {
                holder.code.setText("任务单号   " + mInfo.serial);
            } else {
                holder.code.setText("任务单号   " + mInfo.serial);
            }

            if (mInfo.state != null) {
                holder.tv_state.setText(mInfo.state);
            } else {
                holder.tv_state.setText("   ----");
            }


            if (mInfo.task_desc != null) {
                holder.describle.setText("任务描述   " + mInfo.task_desc);
            } else {
                holder.describle.setText("任务描述   ----");
            }


            if (mInfo.accomplish_time != null) {
                holder.time.setText(mInfo.accomplish_time);
            } else {
                holder.time.setText(" ----");
            }

            if (mInfo.solve != null) {
                if (mInfo.solve.worker_comment != null) {
                    holder.complete.setText(mInfo.solve.worker_comment);
                } else {
                    holder.complete.setText("  ----");
                }
            } else {
                holder.complete.setText("  ----");
            }

        }

        return convertView;
    }


    static class ViewHolder {
        TextView code;
        TextView tv_state;
        TextView describle;
        TextView complete, time;
    }
}
