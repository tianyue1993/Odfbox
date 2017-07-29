package com.odfbox.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.odfbox.R;
import com.odfbox.activity.SpaceImageDetailActivity;
import com.odfbox.entity.Warns;

import java.util.ArrayList;


/**
 * Created by admin on 2017/4/14.
 */

public class WarnsAdapter extends BoxBaseAdapter<Warns> {

    public WarnsAdapter(Context context, ArrayList<Warns> List) {
        super(context);
        this.mList = List;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final Warns mInfo = mList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_warns, null);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.event = (TextView) convertView.findViewById(R.id.event);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (mInfo != null) {
            if (mInfo.alarm) {
                holder.time.setTextColor(mContext.getResources().getColor(R.color.red));
            } else {
                holder.time.setTextColor(mContext.getResources().getColor(R.color.text_grey));
            }
            holder.time.setText(mInfo.time);
            holder.event.setText(mInfo.text);
            if (mInfo.position != null)
                holder.address.setText(mInfo.position.address);

            if (mInfo.picture != null) {
                ImageLoader.getInstance().displayImage("http:" + mInfo.picture.thumbnail_url, holder.imageView);
            }

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SpaceImageDetailActivity.class);
                    if (mInfo.picture != null) {
                        intent.putExtra("images", "http:" + mInfo.picture.url);
                    }
                    int[] location = new int[2];
                    holder.imageView.getLocationOnScreen(location);
                    intent.putExtra("locationX", location[0]);
                    intent.putExtra("locationY", location[1]);
                    intent.putExtra("width", holder.imageView.getWidth());
                    intent.putExtra("height", holder.imageView.getHeight());
                    mContext.startActivity(intent);
                }
            });

        }

        return convertView;
    }


    private static class ViewHolder {
        TextView address, time, event;
        ImageView imageView;

    }
}
