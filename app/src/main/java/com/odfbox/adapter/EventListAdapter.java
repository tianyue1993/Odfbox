package com.odfbox.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.odfbox.R;
import com.odfbox.activity.EventControlActivity;
import com.odfbox.activity.WorkOrderActivity;
import com.odfbox.entity.Event;

import java.util.ArrayList;


/**
 * Created by admin on 2017/4/14.
 */

public class EventListAdapter extends BoxBaseAdapter<Event> {

    public EventListAdapter(Context context, ArrayList<Event> List) {
        super(context);
        this.mList = List;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final Event mInfo = mList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_warns_list, null);
            holder.control_person = (TextView) convertView.findViewById(R.id.control_person);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.tv_code = (TextView) convertView.findViewById(R.id.tv_code);
            holder.state = (TextView) convertView.findViewById(R.id.state);
            holder.handle = (Button) convertView.findViewById(R.id.handle);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mInfo != null) {
            holder.time.setText(mInfo.time);
            if (mInfo.create_user != null) {
                holder.control_person.setText("操作人    " + mInfo.create_user);
            } else {
                holder.control_person.setText("操作人    ----");
            }


            if (mInfo.text != null) {
                holder.tv_type.setText(mInfo.text);
            } else {
                holder.tv_type.setText("----");

            }

            if (mInfo.alarm) {
                holder.tv_type.setTextColor(mContext.getResources().getColor(R.color.red));
            } else {
                holder.tv_type.setTextColor(mContext.getResources().getColor(R.color.text_grey));
            }

            if (mInfo.time != null) {
                holder.time.setText("时          间        " + mInfo.time);
            } else {
                holder.time.setText("时          间        ----");
            }
            if (mInfo.task_sheet != null) {
                holder.tv_code.setText(mInfo.task_sheet.serial);
                holder.tv_code.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, WorkOrderActivity.class);
                        intent.putExtra("serial", mInfo.task_sheet.serial);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                holder.tv_code.setText("----");
            }

            if (mInfo.handle == null && mInfo.task_sheet == null) {
                holder.state.setText("未处理");
                holder.state.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.handle.setVisibility(View.VISIBLE);
            } else {
                holder.state.setText("完成");
                holder.state.setTextColor(mContext.getResources().getColor(R.color.text_grey));
                holder.handle.setVisibility(View.GONE);
            }
            holder.handle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, EventControlActivity.class);
                    Bundle extras = new Bundle();
                    extras.putSerializable("event", mInfo);
                    intent.putExtras(extras);
                    mContext.startActivity(intent);
                }
            });

        }

        return convertView;
    }


    public static class ViewHolder {
        TextView control_person, state, tv_type, time, tv_code;
        Button handle;
    }
}
