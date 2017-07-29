package com.odfbox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.odfbox.R;
import com.odfbox.entity.Managers;

import java.util.ArrayList;


/**
 * Created by admin on 2017/4/14.
 */

public class ChooseManagerAdapter extends BoxBaseAdapter<Managers> {

    public ChooseManagerAdapter(Context context, ArrayList<Managers> List) {
        super(context);
        this.mList = List;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final Managers mInfo = mList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_manage, null);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (mInfo != null) {
        }

        return convertView;
    }


    private static class ViewHolder {

    }
}
