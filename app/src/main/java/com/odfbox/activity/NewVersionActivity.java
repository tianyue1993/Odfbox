package com.odfbox.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.odfbox.OdfboxApplication;
import com.odfbox.R;
import com.odfbox.entity.VersionOdfbox;
import com.odfbox.utils.BoxUtils;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewVersionActivity extends BaseActivity {

    VersionOdfbox versionOdfbox;
    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.size_time)
    TextView sizeTime;
    @Bind(R.id.text_detail)
    TextView textDetail;
    @Bind(R.id.line)
    ImageView line;
    @Bind(R.id.detail)
    TextView detail;
    @Bind(R.id.iamge_share)
    ImageView iamgeShare;
    @Bind(R.id.share_text)
    TextView shareText;
    @Bind(R.id.download)
    Button download;
    private boolean flag = true;
    private boolean isLoading = false;//是否正在下载Apk

    private HttpHandler updateHandler;
    private TextView updateTV;
    private AlertDialog upDatadialog;
    private SeekBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_version);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        versionOdfbox = (VersionOdfbox) bundle.getSerializable("version");
        if (versionOdfbox != null) {
            sizeTime.setText(versionOdfbox.results.get(0).size / 1024 / 1024 + "MB    " + versionOdfbox.results.get(0).upload_time);
            detail.setText(versionOdfbox.results.get(0).comment);
            setTitleTextView("当前版本" + versionOdfbox.results.get(0).revision, null);
        }

    }

    @OnClick(R.id.download)
    public void onViewClicked() {
        if (isLoading) {
            //正在下载中，将对应布局继续弹出
            if (upDatadialog != null) {
                upDatadialog.show();
            }
        } else {
            initUpDate(versionOdfbox);
        }
    }


    public void down() {
        if (flag == true) {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                isLoading = true;
                File file = new File(Environment
                        .getExternalStorageDirectory()
                        + "/Odfbox", "Odfbox.apk");
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                    }
                } else {
                    //若是文件夹存在，删除文件夹中东西，重新下载
                    if (deleteDir(file)) {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                        }
                    }
                }
                HttpUtils finalHttp = new HttpUtils();
                updateHandler = finalHttp.download(
                        versionOdfbox.results.get(0).app,
                        file.getAbsolutePath(), true, true,
                        new RequestCallBack<File>() {
                            @Override
                            public void onStart() {
                                if (upDatadialog != null) {
                                    upDatadialog.dismiss();
                                }
                                showUpdataProcess(); //这二个顺序是要这样的
                                updateTV.setText("连接中...");
                            }

                            @Override
                            public void onLoading(long total,
                                                  long current,
                                                  boolean isUploading) {
                                int process = (int) (current * 100 / total);
                                updateTV.setText("进度" + process
                                        + "%");
                                pb.setMax((int) total);
                                pb.setProgress((int) current);
                                updateTV.setX(process * 8f);
                            }

                            @Override
                            public void onFailure(com.lidroid.xutils.exception.HttpException error, String msg) {
                                if (upDatadialog != null) {
                                    upDatadialog.dismiss();
                                }
                                Toast.makeText(mContext, "下载失败...", Toast.LENGTH_SHORT).show();
                                isLoading = false;
                            }


                            @Override
                            public void onSuccess(
                                    ResponseInfo<File> responseInfo) {

                                if (upDatadialog != null) {
                                    upDatadialog.dismiss();
                                }
                                isLoading = false;
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 加这个
                                intent.addCategory("android.intent.category.DEFAULT");
                                intent.setDataAndType(
                                        Uri.fromFile(responseInfo.result),
                                        "application/vnd.android.package-archive");
                                mContext.startActivity(intent);
                            }
                        });
            } else {
                isLoading = false;
                Toast.makeText(mContext, "SD卡不可用,请检查", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    private void showUpdataProcess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        View view = View.inflate(mContext, R.layout.dialog_enter_pwd, null);
        updateTV = (TextView) view.findViewById(R.id.plan_text);
        upDatadialog = builder.create();
        upDatadialog.setView(view, 0, 0, 0, 0);
        pb = (SeekBar) view.findViewById(R.id.pb_updata);
        pb.setEnabled(false);


        view.findViewById(R.id.bt_hide).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                upDatadialog.dismiss();
            }
        });
        view.findViewById(R.id.bt_cancleok).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //取消升级
                //调用stop()方法停止下载
                updateHandler.cancel();
                upDatadialog.dismiss();
            }
        });
        upDatadialog.show();
    }


    protected void initUpDate(VersionOdfbox updateObj) {
        if (updateObj.results.get(0) != null) {
            if (updateObj.results.get(0).revision.contains("v") || updateObj.results.get(0).revision.contains("V")) {
                if (BoxUtils.getIsUpdate(OdfboxApplication.getAppVersions(), updateObj.results.get(0).revision.substring(1, updateObj.results.get(0).revision.length()))) {
                    down();
                } else {
                    showToast("已是最新版本！");
                }
            } else {
                if (BoxUtils.getIsUpdate(OdfboxApplication.getAppVersions(), updateObj.results.get(0).revision.substring(0, updateObj.results.get(0).revision.length()))) {
                    down();
                } else {
                    showToast("已是最新版本！");
                }
            }

        }
    }
}
