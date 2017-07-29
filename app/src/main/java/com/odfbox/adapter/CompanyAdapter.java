package com.odfbox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.odfbox.R;
import com.odfbox.entity.AccessKey;

import java.util.ArrayList;


/**
 * Created by admin on 2017/4/14.
 */

public class CompanyAdapter extends BoxBaseAdapter<AccessKey.Org> {

    public CompanyAdapter(Context context, ArrayList<AccessKey.Org> List) {
        super(context);
        this.mList = List;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final AccessKey.Org mInfo = mList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_company, null);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (mInfo != null) {
            holder.name.setText(mInfo.name);
        }
        if (selectedPosition == position) {
            holder.name.setBackgroundResource(R.drawable.company_choose_bg);
        } else {
            holder.name.setBackgroundResource(R.drawable.login_edit_bg);
        }

        return convertView;
    }


    private static class ViewHolder {
        TextView name;

    }

    private int selectedPosition = 0;// 选中的位置

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

}
