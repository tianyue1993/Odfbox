package com.odfbox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.odfbox.R;
import com.odfbox.entity.Address;

import java.util.ArrayList;


/**
 * 列表的Adapter封装
 */
public class ComAddressAdapter extends BoxBaseAdapter<Address> {

    public ComAddressAdapter(Context context, ArrayList<Address> List) {
        super(context);
        this.mList = List;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final Address mInfo = mList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_com_address, null);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.area = (TextView) convertView.findViewById(R.id.area);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (mInfo != null) {
            holder.address.setText(mInfo.name);
            holder.area.setText(mInfo.area);
        }

        return convertView;
    }


    public static class ViewHolder {

        TextView address, area;
    }

}
