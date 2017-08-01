package com.odfbox.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.odfbox.MainActivity;
import com.odfbox.R;
import com.odfbox.activity.OpenGuideActivity;
import com.odfbox.activity.SpaceImageDetailActivity;
import com.odfbox.entity.Odfbox;
import com.odfbox.handle.CommentHandler;
import com.odfbox.views.DialogFactory;
import com.odfbox.views.SmoothImageView;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static com.odfbox.R.id.lock;


/**
 * Created by admin on 2017/4/14.
 */

public class MyOdfboxAdapter extends BoxBaseAdapter<Odfbox> {

    private Dialog use;

    public MyOdfboxAdapter(Context context, ArrayList<Odfbox> List) {
        super(context);
        this.mList = List;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final Odfbox mInfo = mList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_my_box, null);
            holder.cder = (TextView) convertView.findViewById(R.id.cder);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.state = (TextView) convertView.findViewById(R.id.state);
            holder.detail = (TextView) convertView.findViewById(R.id.detail);
            holder.code = (TextView) convertView.findViewById(R.id.code);
            holder.image = (SmoothImageView) convertView.findViewById(R.id.image);
            holder.map = (ImageView) convertView.findViewById(R.id.map);
            holder.lock = (ImageView) convertView.findViewById(lock);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (mInfo != null) {
            if (mInfo.serial_text != null) {
                holder.code.setText("光交箱编号：" + mInfo.serial_text);
            } else {
                holder.code.setText("光交箱编号：----");
            }

            if (mInfo.name != null) {
                holder.name.setText("光交箱名字：" + mInfo.name);
            } else {
                holder.name.setText("光交箱名字：----");
            }

            if (mInfo.address != null) {
                holder.address.setText("位置描述：" + mInfo.address);
            } else {
                holder.address.setText("位置描述：----");
            }


            if (mInfo.cable_serial != null) {
                holder.cder.setText("光缆编号:" + mInfo.cable_serial);
            } else {
                holder.cder.setText("光缆编号:" + "----");
            }


            if (mInfo.status != null) {
                holder.state.setText("状态:" + mInfo.status);
            } else {
                holder.state.setText("状态:----");
            }
            if (mInfo.alarming) {
                holder.state.setTextColor(mContext.getResources().getColor(R.color.red));

            } else {
                holder.state.setTextColor(mContext.getResources().getColor(R.color.text_grey));
            }

            if (mInfo.picture != null) {
                ImageLoader.getInstance().displayImage("http:" + mInfo.picture.thumbnail_url, holder.image);
            }

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SpaceImageDetailActivity.class);
                    if (mInfo.picture != null) {
                        intent.putExtra("images", "http:" + mInfo.picture.url);
                    }
                    int[] location = new int[2];
                    holder.image.getLocationOnScreen(location);
                    intent.putExtra("locationX", location[0]);
                    intent.putExtra("locationY", location[1]);
                    intent.putExtra("width", holder.image.getWidth());
                    intent.putExtra("height", holder.image.getHeight());
                    mContext.startActivity(intent);
                }
            });


            holder.map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("type", "box");
                    intent.putExtra("lat", mInfo.latitude_baidu + "");
                    intent.putExtra("lon", mInfo.longitude_baidu + "");
                    intent.setClass(mContext, MainActivity.class);
                    mContext.startActivity(intent);
                }
            });
            holder.lock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    use = DialogFactory.getDialogFactory().showCommonDialog(mContext, "确定开锁？", "取消", "确定", new View.OnClickListener() {
                        @SuppressWarnings("unused")
                        @Override
                        public void onClick(View v) {
                            use.dismiss();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (use != null && use.isShowing()) {
                                use.dismiss();

                                if (mInfo.smart_lock != null) {
                                    showProgress(0, true);
                                    JSONObject params = new JSONObject();
                                    params.put("command", "unlock");
                                    params.put("lock_pk", mInfo.smart_lock.id);
                                    try {
                                        StringEntity entity = new StringEntity(params.toString(), "UTF-8");
                                        client.getOpenBox(mContext, mInfo.smart_lock.id + "", entity, new CommentHandler() {
                                            @Override
                                            public void onSuccess(String commen) {
                                                super.onSuccess(commen);
                                                cancelmDialog();
                                                Intent intent = new Intent(mContext, OpenGuideActivity.class);
                                                intent.putExtra("id", mInfo.id + "");
                                                mContext.startActivity(intent);
                                            }

                                            @Override
                                            public void onFailure(String msg) {
                                                super.onFailure(msg);
                                                cancelmDialog();
                                            }

                                            @Override
                                            public void onCancel() {
                                                super.onCancel();
                                                cancelmDialog();
                                            }
                                        });
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    showToast("未能获得智能锁信息，请联系管理员！");
                                }
                            }
                        }
                    });

                }
            });


        }
        return convertView;
    }


    static class ViewHolder {
        TextView code;
        TextView name;
        TextView address;
        TextView cder;
        TextView state;
        TextView detail;
        SmoothImageView image;
        ImageView lock, map;
    }
}
