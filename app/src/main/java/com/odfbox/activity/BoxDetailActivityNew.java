package com.odfbox.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.odfbox.OdfboxApplication;
import com.odfbox.R;
import com.odfbox.entity.Attachments;
import com.odfbox.entity.BoxDefineList;
import com.odfbox.entity.Odfbox;
import com.odfbox.entity.SmartLock;
import com.odfbox.handle.BoxDefineHandler;
import com.odfbox.handle.CommentHandler;
import com.odfbox.utils.BoxUtils;
import com.odfbox.utils.GetPathFromUri4kitkat;
import com.odfbox.utils.Preferences;
import com.odfbox.utils.StringUtils;
import com.odfbox.views.DialogFactory;
import com.odfbox.views.SmoothImageView;
import com.odfbox.zxing.activity.CaptureActivity;

import org.apache.http.entity.StringEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.odfbox.utils.Preferences.REFRESH_BOXLIST;

public class BoxDetailActivityNew extends BaseActivity {


    Dialog dialog;
    public int ifChange = 0;

    ArrayList<Attachments> attachments = new ArrayList<>();

    String picture1 = "";
    String picture2 = "";
    String picture3 = "";

    /**
     * 头像文件
     */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";
    public static final int PIC = 1;
    public static final int TAKEPHOTO = 11;
    public static final int PICKPHOTO = 12;
    public static final int PICK_PHOTO_FROM_MEIZU = 16;
    private static final int TAKE_PHOTO = 10;
    private static int imageType = 1;

    protected Bitmap photo;
    protected File mCurrentPhotoFile;
    @Bind(R.id.box_image)
    SmoothImageView boxImage;
    @Bind(R.id.box_near_image)
    SmoothImageView boxNearImage;
    @Bind(R.id.box_inner_image)
    SmoothImageView boxInnerImage;
    @Bind(R.id.line1)
    ImageView line1;
    @Bind(R.id.editText)
    EditText editText;
    @Bind(R.id.map_address)
    ImageView mapAddress;
    @Bind(R.id.ed_describe)
    EditText edDescribe;
    @Bind(R.id.ed_code)
    EditText edCode;
    @Bind(R.id.checkbox1)
    CheckBox checkbox1;
    @Bind(R.id.checkbox2)
    CheckBox checkbox2;
    @Bind(R.id.checkbox3)
    CheckBox checkbox3;
    @Bind(R.id.ed_factory)
    EditText edFactory;
    @Bind(R.id.ed_type)
    Spinner edType;
    @Bind(R.id.checkbox4)
    CheckBox checkbox4;
    @Bind(R.id.checkbox5)
    CheckBox checkbox5;
    @Bind(R.id.checkbox6)
    CheckBox checkbox6;
    @Bind(R.id.ed_content)
    EditText edContent;
    @Bind(R.id.ed_time)
    TextView edTime;
    @Bind(R.id.ed_state)
    EditText edState;
    @Bind(R.id.ed_terminal)
    EditText edTerminal;
    @Bind(R.id.image_scan)
    ImageView imageScan;
    @Bind(R.id.last_close_time)
    TextView lastCloseTime;
    @Bind(R.id.tv_eltric)
    TextView tvEltric;
    @Bind(R.id.eltric)
    TextView eltric;
    @Bind(R.id.ll_news)
    LinearLayout llNews;
    @Bind(R.id.btn_cancel)
    TextView btnCancel;
    @Bind(R.id.btn_sure)
    TextView btnSure;
    @Bind(R.id.ll_change)
    LinearLayout llChange;

    private Uri photoUri;
    private File mUriFile;
    private static final int CAMERA_PIC = 21;
    private static final int CROP_PIC = 22;
    private static final int CODE_CAMERA_REQUEST = 30;
    private static final int CODE_RESULT_REQUEST = 33;
    Odfbox odfbox;
    String[] OdfBoxModel = null;
    String[] OdfBoxMaterial;
    String caizhi = "";
    String door = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_detail_new);
        ButterKnife.bind(this);
        OdfboxApplication.addActivity(this);
        setTitleTextView("光交箱详细信息", null);
        Bundle bundle = getIntent().getExtras();
        odfbox = (Odfbox) bundle.getSerializable("info");
        Log.d("odfbox", "onCreate: odfbox" + odfbox.toString());
        initData();
        getConstantDefine("OdfBoxModel");
        getConstantDefine("OdfBoxMaterial");
        initCheckbox();
    }


    public void initData() {
        edDescribe.setEnabled(false);
        edCode.setEnabled(false);
        editText.setEnabled(false);
        edFactory.setEnabled(false);
        edContent.setEnabled(false);
        edTime.setEnabled(false);
        edState.setEnabled(false);
        edTerminal.setEnabled(false);
        edType.setEnabled(false);
        checkbox1.setEnabled(false);
        checkbox2.setEnabled(false);
        checkbox3.setEnabled(false);
        checkbox4.setEnabled(false);
        checkbox5.setEnabled(false);
        checkbox6.setEnabled(false);
        imageScan.setVisibility(View.GONE);
        for (int i = 0; i < 3; i++) {
            Attachments a = new Attachments();
            a.mimetype = "image/jpeg";
            a.base64 = "";
            attachments.add(a);
        }
        if (odfbox == null) {
            odfbox = new Odfbox();
        }


        if (odfbox.picture != null) {
            ImageLoader.getInstance().displayImage("http:" + odfbox.picture.url, boxImage);
        }
        if (odfbox.env_picture != null) {
            ImageLoader.getInstance().displayImage("http:" + odfbox.env_picture.url, boxNearImage);
        }
        if (odfbox.inside_picture != null) {
            ImageLoader.getInstance().displayImage("http:" + odfbox.inside_picture.url, boxInnerImage);
        }
        editText.setText(odfbox.longitude_baidu + "," + odfbox.latitude_baidu);
        edDescribe.setText(odfbox.address);
        edCode.setText(odfbox.serial_text);
        edFactory.setText(odfbox.manufacturer);
        edContent.setText(odfbox.business_capacity);
        edState.setText(odfbox.status);
        if (odfbox.door_type.equals("双门双面")) {
            checkbox1.setChecked(true);
            door = "双门双面";
        } else if (odfbox.door_type.equals("单面双门")) {
            checkbox2.setChecked(true);
            door = "单面双门";
        } else if (odfbox.door_type.equals("单面单门")) {
            checkbox3.setChecked(true);
            door = "单面单门";
        }

        if (odfbox.material.equals("SMC玻璃钢")) {
            checkbox4.setChecked(true);
            caizhi = "SMC玻璃钢";
        } else if (odfbox.material.equals("不锈钢")) {
            checkbox5.setChecked(true);
            caizhi = "不锈钢";
        } else if (odfbox.material.equals("冷轧板")) {
            checkbox6.setChecked(true);
            caizhi = "冷轧板";
        }
        if (odfbox.smart_lock != null) {
            edTime.setText(odfbox.smart_lock.install_date);
            edTerminal.setText(odfbox.smart_lock.serial_no);
            if (odfbox.smart_lock.last_close_datetime != null) {
                lastCloseTime.setText("上次关门时间：" + odfbox.smart_lock.last_close_datetime);
            }
            if (odfbox.smart_lock.battery_left != null) {
                tvEltric.setVisibility(View.VISIBLE);
                eltric.setVisibility(View.VISIBLE);
                eltric.setText(odfbox.smart_lock.battery_left);
            }
        } else {
            tvEltric.setVisibility(View.INVISIBLE);
            eltric.setVisibility(View.INVISIBLE);
        }

        setRightText("编辑", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ifChange == 0) {
                    ifChange = 1;
                    llChange.setVisibility(View.VISIBLE);
                    edCode.setEnabled(true);
                    edCode.setFocusable(true);
                    edCode.requestFocus();
                    editText.setEnabled(true);
                    editText.setFocusable(true);
                    editText.requestFocus();
                    edFactory.setEnabled(true);
                    edFactory.setFocusable(true);
                    edFactory.requestFocus();
                    edContent.setEnabled(true);
                    edContent.setFocusable(true);
                    edContent.requestFocus();
                    edTime.setEnabled(true);
                    edTime.setFocusable(true);
                    edTime.requestFocus();
                    edState.setEnabled(true);
                    edState.setFocusable(true);
                    edState.requestFocus();
                    edTerminal.setEnabled(true);
                    edTerminal.setFocusable(true);
                    edTerminal.requestFocus();
                    edDescribe.setEnabled(true);
                    edDescribe.setFocusable(true);
                    edDescribe.requestFocus();
                    edType.setEnabled(true);
                    checkbox1.setEnabled(true);
                    checkbox2.setEnabled(true);
                    checkbox3.setEnabled(true);
                    checkbox4.setEnabled(true);
                    checkbox5.setEnabled(true);
                    checkbox6.setEnabled(true);
                    imageScan.setVisibility(View.VISIBLE);
                } else {
                    ifChange = 0;
                    llChange.setVisibility(View.GONE);
                    edDescribe.setEnabled(false);
                    edCode.setEnabled(false);
                    edFactory.setEnabled(false);
                    edContent.setEnabled(false);
                    edTime.setEnabled(false);
                    edState.setEnabled(false);
                    edTerminal.setEnabled(false);
                    edType.setEnabled(false);
                    checkbox1.setEnabled(false);
                    checkbox2.setEnabled(false);
                    checkbox3.setEnabled(false);
                    checkbox4.setEnabled(false);
                    checkbox5.setEnabled(false);
                    checkbox6.setEnabled(false);
                    editText.setEnabled(false);
                    imageScan.setVisibility(View.GONE);
                }
            }
        });


        edType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                tv.setTextColor(getResources().getColor(R.color.text_grey));    //设置颜色
                tv.setTextSize(12.0f);    //设置大小
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }


    public void getConstantDefine(final String type) {
        client.getConstantDefine(mContext, type, new BoxDefineHandler() {
            @Override
            public void onSuccess(BoxDefineList commen) {
                super.onSuccess(commen);
                if (type.equals("OdfBoxMaterial")) {
                    ArrayList<String> material = new ArrayList<String>();
                    material.add("材质");
                    for (int i = 0; i < commen.results.size(); i++) {
                        material.add(commen.results.get(i).text);
                    }
                    OdfBoxMaterial = (String[]) material.toArray(new String[commen.results.size()]);

                } else if (type.equals("OdfBoxModel")) {
                    ArrayList<String> model = new ArrayList<String>();
                    model.add("型号");
                    for (int i = 0; i < commen.results.size(); i++) {
                        model.add(commen.results.get(i).text);
                    }
                    OdfBoxModel = (String[]) model.toArray(new String[commen.results.size()]);
                }
                if (OdfBoxMaterial != null && OdfBoxModel != null) {
                    setData();
                }

            }
        });
    }

    public void setData() {
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, OdfBoxModel);
        edType.setAdapter(typeAdapter);

        if (odfbox.material != null) {
            for (int i = 0; i < OdfBoxMaterial.length; i++) {
                if (odfbox.material.equals(OdfBoxMaterial[i])) {
                }
            }
        }
        if (odfbox.model != null) {
            for (int i = 0; i < OdfBoxModel.length; i++) {
                if (odfbox.model.equals(OdfBoxModel[i])) {
                    edType.setSelection(i, true);
                }
            }
        }

    }


    @OnClick({R.id.box_image, R.id.box_inner_image, R.id.box_near_image, R.id.btn_cancel, R.id.btn_sure, R.id.map_address, R.id.image_scan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.box_image:
                if (ifChange == 1) {
                    //可编辑
                    Intent intent1 = new Intent(mContext, SelectPicPopupWindowActivity.class);
                    startActivityForResult(intent1, PIC);
                    imageType = 1;
                } else {
                    Intent intent = new Intent(mContext, SpaceImageDetailActivity.class);
                    if (odfbox.picture != null) {
                        intent.putExtra("images", "http:" + odfbox.picture.url);
                    }
                    int[] location = new int[2];
                    boxImage.getLocationOnScreen(location);
                    intent.putExtra("locationX", location[0]);
                    intent.putExtra("locationY", location[1]);
                    intent.putExtra("width", boxImage.getWidth());
                    intent.putExtra("height", boxImage.getHeight());
                    startActivity(intent);
                    overridePendingTransition(0, 0);

                }

                break;
            case R.id.box_inner_image:

                if (ifChange == 1) {
                    //可编辑
                    Intent intent3 = new Intent(mContext, SelectPicPopupWindowActivity.class);
                    startActivityForResult(intent3, PIC);
                    imageType = 2;
                } else {
                    Intent intent = new Intent(mContext, SpaceImageDetailActivity.class);
                    if (odfbox.inside_picture != null) {
                        intent.putExtra("images", "http:" + odfbox.inside_picture.url);
                    }
                    int[] location = new int[2];
                    boxInnerImage.getLocationOnScreen(location);
                    intent.putExtra("locationX", location[0]);
                    intent.putExtra("locationY", location[1]);
                    intent.putExtra("width", boxInnerImage.getWidth());
                    intent.putExtra("height", boxInnerImage.getHeight());
                    startActivity(intent);
                    overridePendingTransition(0, 0);

                }
                break;
            case R.id.box_near_image:
                if (ifChange == 1) {
                    //可编辑
                    Intent intent2 = new Intent(mContext, SelectPicPopupWindowActivity.class);
                    startActivityForResult(intent2, PIC);
                    imageType = 3;
                } else {
                    Intent intent = new Intent(mContext, SpaceImageDetailActivity.class);
                    if (odfbox.env_picture != null) {
                        intent.putExtra("images", "http:" + odfbox.env_picture.url);
                    }
                    int[] location = new int[2];
                    boxNearImage.getLocationOnScreen(location);
                    intent.putExtra("locationX", location[0]);
                    intent.putExtra("locationY", location[1]);
                    intent.putExtra("width", boxNearImage.getWidth());
                    intent.putExtra("height", boxNearImage.getHeight());
                    startActivity(intent);
                    overridePendingTransition(0, 0);

                }
                break;
            case R.id.btn_cancel:

                dialog = DialogFactory.getDialogFactory().showCommonDialog(mContext, "您尚未提交修改，是否退出？", "取消", "确定", new View.OnClickListener() {
                    @SuppressWarnings("unused")
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog != null && dialog.isShowing()) {
                            llChange.setVisibility(View.GONE);
                            ifChange = 0;
                            llChange.setVisibility(View.GONE);
                            edDescribe.setEnabled(false);
                            edCode.setEnabled(false);
                            edFactory.setEnabled(false);
                            edContent.setEnabled(false);
                            edTime.setEnabled(false);
                            edState.setEnabled(false);
                            edTerminal.setEnabled(false);
                            edType.setEnabled(false);
                            checkbox1.setEnabled(false);
                            checkbox2.setEnabled(false);
                            checkbox3.setEnabled(false);
                            checkbox4.setEnabled(false);
                            checkbox5.setEnabled(false);
                            checkbox6.setEnabled(false);
                            editText.setEnabled(false);
                            imageScan.setVisibility(View.GONE);
                            dialog.dismiss();
                        }

                    }
                });

                break;
            case R.id.btn_sure:
                editBox();
                break;
            case R.id.map_address:
                if (ifChange == 1) {
                    startActivityForResult(new Intent(mContext, SearchActivity.class), Preferences.GETADDRESS);
                } else {
                    Intent intent = new Intent(mContext, OdfboxLocationActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("odfbox", odfbox);
                    bundle.putString("type", odfbox.alarming + "");
                    bundle.putString("lat", odfbox.latitude_baidu + "");
                    bundle.putString("lon", odfbox.longitude_baidu + "");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                break;
//            case R.id.current_address:
//                editText.setText(prefs.getCurrentAddress()[0] + "," + prefs.getCurrentAddress()[1]);
//                edDescribe.setText(prefs.getCurrentAddress()[2]);
//                break;
            case R.id.image_scan:
                //二维码
                startActivityForResult(new Intent(mContext, CaptureActivity.class), 5);
                break;
            default:
                break;
        }
    }


    public void editBox() {
        JSONObject object = new JSONObject();
        if (edCode.getText().toString().trim().length() > 0) {
            object.put("serial_text", edCode.getText().toString());
        }

        if (edDescribe.getText().toString().trim().length() > 0) {
            object.put("address", edDescribe.getText().toString());
        }
        if (editText.getText().toString().trim().length() > 0) {
            object.put("longitude_baidu", editText.getText().toString().split(",")[0]);
            object.put("latitude_baidu", editText.getText().toString().split(",")[1]);
        }

        if (caizhi.length() > 0) {
            object.put("material", caizhi);
        }
        if (door.length() > 0) {
            object.put("door_type", door);
        }

        if (edType.getSelectedItem().toString().trim().length() > 0) {
            object.put("model", edType.getSelectedItem().toString());
        }


        if (edContent.getText().toString().trim().length() > 0) {
            object.put("business_capacity", edContent.getText().toString());
        }

        if (edType.getSelectedItem().toString().trim().length() > 0 && !edType.getSelectedItem().toString().trim().equals("型号")) {
            object.put("model", edType.getSelectedItem().toString());
        }

        if (edFactory.getText().toString().trim().length() > 0) {
            object.put("manufacturer", edFactory.getText().toString());
        }
        if (edState.getText().toString().trim().length() > 0) {
            object.put("status", edState.getText().toString());
        }

        SmartLock lock = new SmartLock();
        if (!StringUtils.isEmpty(edTime.getText().toString())) {
            lock.install_date = edTime.getText().toString();
        } else {
            lock.install_date = "";
        }

        if (odfbox.smart_lock != null) {
            if (!"".equals(edTerminal.getText().toString().trim())) {
                lock.serial_no = edTerminal.getText().toString();
                object.put("smart_lock", lock);
            } else {
                showToast("请输入终端编号！");
                return;
            }
        }

        if (!StringUtils.isEmpty(attachments.get(0).base64)) {
            object.put("picture", attachments.get(0));
        }
        if (!StringUtils.isEmpty(attachments.get(1).base64)) {
            object.put("inside_picture", attachments.get(1));
        }
        if (!StringUtils.isEmpty(attachments.get(2).base64)) {
            object.put("env_picture", attachments.get(2));
        }

        Log.d("object", "editBox: object" + object.toString());
        try {
            showProgress(0, true);
            StringEntity entity = new StringEntity(object.toString(), "UTF-8");
            client.getEditBox(mContext, entity, odfbox.id + "", new CommentHandler() {
                @Override
                public void onSuccess(String commen) {
                    super.onSuccess(commen);
                    cancelmDialog();
                    showToast("修改成功");
                    sendBroadcast(new Intent(REFRESH_BOXLIST));
                    finish();
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


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 5) {
                if (!StringUtils.isEmpty(data.getStringExtra("code"))) {
                    edTerminal.setText(data.getStringExtra("code"));
                }

            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }

        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        } else {
            switch (requestCode) {
                case TAKE_PHOTO:
                    String originalPath = null;
                    Uri uri = null;
                    if (data != null && data.getData() != null) {
                        uri = data.getData();
                    }
                    if (uri == null) {
                        if (photoUri != null) {
                            uri = photoUri;
                        }
                    }
                    tryCropProfileImage(uri);
                    break;
                default:
                    break;
            }

        }
        if (data != null) {
            switch (requestCode) {
                case PIC:
                    String pic = data.getStringExtra("picmethod");
                    if (pic.equals("TAKEPHOTO")) {
                        String state = Environment.getExternalStorageState();
                        if (state.equals(Environment.MEDIA_MOUNTED)) {
                            Intent takeintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            System.gc();
                            SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMM_dd_HH_mm_ss");
                            String filename = timeStampFormat.format(new Date());
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, filename);
                            photoUri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            takeintent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                            startActivityForResult(takeintent, TAKE_PHOTO);
                        } else {
                            Toast.makeText(mContext, "没有SD卡", Toast.LENGTH_SHORT).show();
                        }
                    } else if (pic.equals("PICKPHOTO")) {
                        doPickPhoto();
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    if (data != null) {
                        setImageToHeadView(data);
                    }
                    break;
                case CODE_CAMERA_REQUEST:
                    if (hasSdcard()) {
                        File tempFile = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
                        cropRawPhoto(Uri.fromFile(tempFile));
                    } else {
                        Toast.makeText(mContext, "没有SDCard!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case PICK_PHOTO_FROM_MEIZU:
                    photo = data.getParcelableExtra("data");
                    if (photo == null) {
                        Uri originalUri;
                        if (data.getData() == null) {
                            String filePath = data.getStringExtra("filePath");
                            originalUri = Uri.fromFile(new File(filePath));
                        } else {
                            originalUri = data.getData();
                        }
                        tryCropProfileImage(originalUri);
                    } else {
                        mCurrentPhotoFile = new File(BoxUtils.getTmpCachePath() + "screenshot.png");
                        try {
                            saveBitmap(mCurrentPhotoFile.getAbsolutePath(), photo);
                            onHeaderSelectedCallBack(photo);
                            if (imageType == 1) {
                                picture1 = updateAvatorByFile(mCurrentPhotoFile.getAbsolutePath());
                                attachments.get(0).base64 = picture1;
                            } else if (imageType == 2) {
                                picture2 = updateAvatorByFile(mCurrentPhotoFile.getAbsolutePath());
                                attachments.get(1).base64 = picture2;
                            } else if (imageType == 3) {
                                picture3 = updateAvatorByFile(mCurrentPhotoFile.getAbsolutePath());
                                attachments.get(2).base64 = picture3;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case PICKPHOTO:
                    if (mCurrentPhotoFile != null && mCurrentPhotoFile.exists()) {
                        mCurrentPhotoFile.delete();
                    }

                    photo = data.getParcelableExtra("data");
                    if (photo == null) {
                        // get photo url
                        Uri originalUri;
                        if (data.getData() == null) {
                            String filePath = data.getStringExtra("filePath");
                            originalUri = Uri.fromFile(new File(filePath));
                        } else {
                            originalUri = data.getData();
                        }
                        try {
                            onLicenseSelectedCallBack(originalUri);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        mCurrentPhotoFile = new File(BoxUtils.getTmpCachePath() + "screenshot.png");
                        try {
                            saveBitmap(mCurrentPhotoFile.getAbsolutePath(), photo);
                            onHeaderSelectedCallBack(photo);
                            if (imageType == 1) {
                                picture1 = updateAvatorByFile(mCurrentPhotoFile.getAbsolutePath());
                                attachments.get(0).base64 = picture1;
                            } else if (imageType == 2) {
                                picture2 = updateAvatorByFile(mCurrentPhotoFile.getAbsolutePath());
                                attachments.get(1).base64 = picture2;
                            } else if (imageType == 3) {
                                picture3 = updateAvatorByFile(mCurrentPhotoFile.getAbsolutePath());
                                attachments.get(2).base64 = picture3;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case TAKE_PHOTO:
                    if (resultCode == RESULT_OK) {
                        String originalPath = null;
                        Uri uri = null;
                        if (data != null && data.getData() != null) {
                            uri = data.getData();
                        }
                        if (uri == null) {
                            if (photoUri != null) {
                                uri = photoUri;
                            }
                        }

                        originalPath = uri2filePath(uri);
                        if (StringUtils.isEmpty(originalPath)) {
                            if (imageType == 1) {
                                picture1 = updateAvatorByFile(mCurrentPhotoFile.getAbsolutePath());
                                attachments.get(0).base64 = picture1;
                            } else if (imageType == 2) {
                                picture2 = updateAvatorByFile(mCurrentPhotoFile.getAbsolutePath());
                                attachments.get(1).base64 = picture2;
                            } else if (imageType == 3) {
                                picture3 = updateAvatorByFile(mCurrentPhotoFile.getAbsolutePath());
                                attachments.get(2).base64 = picture3;
                            }

                        } else {
                            Toast.makeText(mContext, "拍照失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case TAKEPHOTO:
                    tryCropProfileImage(Uri.fromFile(mCurrentPhotoFile));
                    break;
                case CAMERA_PIC:
                    cropPhoto(Uri.fromFile(mUriFile));
                    break;
                case Preferences.GETADDRESS:

                    if (!StringUtils.isEmpty(data.getStringExtra("jd")) && !StringUtils.isEmpty(data.getStringExtra("wd"))) {
                        editText.setText(data.getStringExtra("jd") + "," + data.getStringExtra("wd"));
                    }
                    if (!StringUtils.isEmpty(data.getStringExtra("address"))) {
                        edDescribe.setText(data.getStringExtra("address"));
                    }
                    break;
            }


        }
    }


    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
        }
    }

    protected void onLicenseSelectedCallBack(Uri url) {
        if (imageType == 1) {
            boxImage.setImageURI(url);
        } else if (imageType == 2) {
            boxInnerImage.setImageURI(url);
        } else if (imageType == 3) {
            boxNearImage.setImageURI(url);
        }
        updateAvatorByUrl(url);
    }


    private void updateAvatorByUrl(Uri url) {
        updateAvatorByFile(getAbsoluteImagePath(url));
    }

    //
    private String updateAvatorByFile(String absolutePath) {
        return BoxUtils.getImgBase64(absolutePath);
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    /**
     * 把Uri转化成文件路径
     */
    private String uri2filePath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = mContext.getContentResolver().query(uri, proj, null, null, null);
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(index);
        return path;
    }

    protected String getAbsoluteImagePath(final Uri uri) {
        // can post image
        final String[] proj = {MediaStore.Images.Media.DATA};
        String path = GetPathFromUri4kitkat.getPath(mContext, uri);
        if (!StringUtils.isEmpty(path)) {
            return path;
        }
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, proj, // Which columns to return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    protected void onHeaderSelectedCallBack(Bitmap bp) {
        if (bp != null) {
            if (imageType == 1) {
                boxImage.setImageBitmap(bp);
            } else if (imageType == 2) {
                boxInnerImage.setImageBitmap(bp);
            } else if (imageType == 3) {
                boxNearImage.setImageBitmap(bp);
            }
        }

    }

    private void tryCropProfileImage(Uri uri) {
        try {
            // start gallery to crop photo
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, AddBoxActivity.PICKPHOTO);// test
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void saveBitmap(String filePath, Bitmap bitmap) throws IOException {
        if (bitmap != null) {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream out = new FileOutputStream(file);
            bitmap = compressImage(bitmap);
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, out);// 图片设置为压缩一半
            // 11.24 18：50
            out.flush();
            out.close();
        }
    }

    protected Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 50) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    //图片操作
    private void doPickPhoto() {
        if (Build.BRAND.equalsIgnoreCase("Meizu")) {
            Intent itentFromGallery = new Intent();
            itentFromGallery.setType("image/*");
            itentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(itentFromGallery, PICK_PHOTO_FROM_MEIZU);
            return;
        }

        try {
            // Launch picker to choose photo for selected contact
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setData(Uri.parse("content://media/internal/images/media"));
            intent.setType("image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, AddBoxActivity.PICKPHOTO);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    private boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }


    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        // intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, CROP_PIC);
    }

    public void initCheckbox() {
        checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    door = "双面双门";
                    checkbox2.setChecked(false);
                    checkbox3.setChecked(false);
                }

            }
        });
        checkbox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    door = "单面双门";
                    checkbox1.setChecked(false);
                    checkbox3.setChecked(false);
                }

            }
        });
        checkbox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    door = "单门";
                    checkbox2.setChecked(false);
                    checkbox1.setChecked(false);
                }

            }
        });

        checkbox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    caizhi = "SMC玻璃钢";
                    checkbox5.setChecked(false);
                    checkbox6.setChecked(false);
                }

            }
        });
        checkbox5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    caizhi = "不锈钢";
                    checkbox4.setChecked(false);
                    checkbox6.setChecked(false);
                }

            }
        });
        checkbox6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    caizhi = "冷轧板";
                    checkbox4.setChecked(false);
                    checkbox5.setChecked(false);
                }

            }
        });
    }
}
