package com.odfbox.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.odfbox.R;
import com.odfbox.entity.GuideList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by admin on 2017/4/14.
 */

public class GuideAdapter extends BoxBaseAdapter<GuideList.Guide> {

    public GuideAdapter(Context context, ArrayList<GuideList.Guide> List) {
        super(context);
        this.mList = List;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final GuideList.Guide mInfo = mList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_guide, null);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.down = (TextView) convertView.findViewById(R.id.down);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mInfo != null) {
            holder.title.setText(mInfo.file_name);
            holder.time.setText(mInfo.upload_time);
            if (exsitDir(mInfo.file_name)) {
                holder.down.setText("打开");
                holder.down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File file = new File(Environment
                                .getExternalStorageDirectory()
                                + "/odf", mInfo.file_name + "");
                        openPDFReader(file);
                    }
                });

            } else {
                holder.down.setText("下载");
                holder.down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isLoading) {
                            downLoad(mInfo.document, holder.down, mInfo.file_name);
                        }

                    }
                });
            }


        }

        return convertView;
    }


    private static class ViewHolder {
        TextView title, down, time;
    }

    private boolean isLoading = false;

    public void downLoad(String url, final TextView textView, String title) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            isLoading = true;
            File file = new File(Environment
                    .getExternalStorageDirectory()
                    + "/odf", title + "");
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
            HttpUtils finalHttp = new HttpUtils();
            HttpHandler updateHandler = finalHttp.download(
                    url, file.getAbsolutePath(), true, true,
                    new RequestCallBack<File>() {
                        @Override
                        public void onStart() {
                            super.onStart();
                            textView.setText("正在下载");
                        }

                        @Override
                        public void onLoading(long total, long current, boolean isUploading) {
                            super.onLoading(total, current, isUploading);
                            int process = (int) (current * 100 / total);
                            textView.setText(process + "%");
                        }

                        @Override
                        public void onSuccess(ResponseInfo<File> responseInfo) {
                            isLoading = false;
                            showToast("下载完成");
                            textView.setText("打开");
                            openPDFReader(responseInfo.result);
                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            Toast.makeText(mContext, "下载失败...", Toast.LENGTH_SHORT).show();
                            textView.setText("下载失败");
                            isLoading = false;
                        }
                    });
        }


    }


    private static boolean exsitDir(String title) {
        File file = new File(Environment
                .getExternalStorageDirectory()
                + "/odf", title + "");
        if (!file.exists()) {
            return false;//不存在
        } else {
            return true;//存在
        }
    }


    public void openPDFReader(File file) {
//   Log.v("wenjianming>>>>>>>>>>", Global.magazinePath+tempData.name+".pdf");
        if (file.exists()) {
            Uri path = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "application/msword");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                mContext.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(mContext,
                        "No Application Available to View PDF",
                        Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(mContext,
                    "No Application Available to View PDF",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
