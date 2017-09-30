package com.odfbox.activity;

import android.app.Activity;
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
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.odfbox.OdfboxApplication;
import com.odfbox.R;
import com.odfbox.entity.Attachments;
import com.odfbox.entity.BoxDefineList;
import com.odfbox.entity.SmartLock;
import com.odfbox.handle.BoxDefineHandler;
import com.odfbox.handle.CommentHandler;
import com.odfbox.utils.BoxUtils;
import com.odfbox.utils.GetPathFromUri4kitkat;
import com.odfbox.utils.Preferences;
import com.odfbox.utils.StringUtils;
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

public class AddBoxActivityNew extends BaseActivity {

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
    ImageView boxImage;
    @Bind(R.id.box_near_image)
    ImageView boxNearImage;
    @Bind(R.id.box_inner_image)
    ImageView boxInnerImage;
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
    @Bind(R.id.ed_terminal)
    EditText edTerminal;
    @Bind(R.id.image_scan)
    ImageView imageScan;
    @Bind(R.id.ll_news)
    LinearLayout llNews;
    @Bind(R.id.add)
    Button add;
    @Bind(R.id.ed_state)
    EditText edState;
    private Uri photoUri;
    private File mUriFile;
    private static final int CAMERA_PIC = 21;
    private static final int CROP_PIC = 22;
    private static final int CODE_CAMERA_REQUEST = 30;
    private static final int CODE_RESULT_REQUEST = 33;


    ArrayList<Attachments> attachments = new ArrayList<>();

    String picture1 = "";
    String picture2 = "";
    String picture3 = "";

    String[] OdfBoxModel = null;
    String[] OdfBoxMaterial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OdfboxApplication.addActivity(this);
        setContentView(R.layout.activity_add_box_new);
        ButterKnife.bind(this);
        setTitleTextView("新增光交箱", null);

        for (int i = 0; i < 3; i++) {
            Attachments a = new Attachments();
            a.mimetype = "image/jpeg";
            a.base64 = "";
            attachments.add(a);
        }


        getConstantDefine("OdfBoxModel");
        getConstantDefine("OdfBoxMaterial");
        initCheckbox();


        edType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                tv.setTextColor(getResources().getColor(R.color.text_grey));    //设置颜色
                tv.setTextSize(12.0f);    //设置大小
                tv.setGravity(Gravity.CENTER_HORIZONTAL);   //设置居中
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }


    @OnClick({R.id.map_address, R.id.image_scan, R.id.add, R.id.box_image, R.id.box_near_image, R.id.box_inner_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.map_address:
                startActivityForResult(new Intent(mContext, SearchActivity.class), Preferences.GETADDRESS);
                break;
//            case R.id.current_address:
//                editText.setText(prefs.getCurrentAddress()[0] + "," + prefs.getCurrentAddress()[1]);
//                edDescribe.setText(prefs.getCurrentAddress()[2]);
//                break;
            case R.id.image_scan:
                //二维码
                startActivityForResult(new Intent(mContext, CaptureActivity.class), 5);
                break;
            case R.id.add:
                addBox();
                break;
            case R.id.box_image:
                Intent intent1 = new Intent(mContext, SelectPicPopupWindowActivity.class);
                startActivityForResult(intent1, PIC);
                imageType = 1;
                break;
            case R.id.box_inner_image:
                Intent intent3 = new Intent(mContext, SelectPicPopupWindowActivity.class);
                startActivityForResult(intent3, PIC);
                imageType = 2;
                break;
            case R.id.box_near_image:
                Intent intent2 = new Intent(mContext, SelectPicPopupWindowActivity.class);
                startActivityForResult(intent2, PIC);
                imageType = 3;
                break;


        }
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
        // 建立数据源
//        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, OdfBoxModel);
        //绑定 Adapter到控件
        edType.setAdapter(typeAdapter);
        edType.setSelection(0, true);
    }

    String caizhi = "";
    String door = "";

    public void addBox() {
        if (!edCode.getText().toString().isEmpty() &&
                !edDescribe.getText().toString().isEmpty() &&
                !editText.getText().toString().isEmpty() &&
                !edContent.getText().toString().isEmpty() &&
                !edFactory.getText().toString().isEmpty() &&
                door.length() != 0 &&
                attachments.get(0).base64 != null &&
                attachments.get(1).base64 != null &&
                attachments.get(2).base64 != null) {
            JSONObject object = new JSONObject();
            object.put("serial_text", edCode.getText().toString());
            object.put("address", edDescribe.getText().toString());
            object.put("longitude_baidu", editText.getText().toString().split(",")[0]);
            object.put("latitude_baidu", editText.getText().toString().split(",")[1]);
            object.put("material", caizhi);
            object.put("door_type", door);
            object.put("status", edState.getText().toString());
            object.put("model", edType.getSelectedItem().toString());
            object.put("business_capacity", edContent.getText().toString());
            object.put("manufacturer", edFactory.getText().toString());
            if (edTerminal.getText().toString().length() > 0) {
                SmartLock lock = new SmartLock();
                lock.serial_no = edTerminal.getText().toString();
                object.put("smart_lock", lock);
            }
            object.put("picture", attachments.get(0));
            object.put("inside_picture", attachments.get(1));
            object.put("env_picture", attachments.get(2));
            Log.d("object", "addBox: object" + object.toString());
            try {
                showProgress(0, true);
                StringEntity entity = new StringEntity(object.toString(), "UTF-8");
                client.getAddBox(mContext, entity, new CommentHandler() {
                    @Override
                    public void onSuccess(String commen) {
                        super.onSuccess(commen);
                        cancelmDialog();
                        showToast("添加成功");
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

        } else {
            showToast("请补充交光箱信息！");
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5) {
            if (!StringUtils.isEmpty(data.getStringExtra("code"))) {
                edTerminal.setText(data.getStringExtra("code"));
            }
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
                    // 一些机型无法从getData中获取uri，则需手动指定拍照后存储照片的Uri (魅族无法获取到URI)
                    if (uri == null) {
                        if (photoUri != null) {
                            uri = photoUri;
                        }
                    }
                    // 头像正常情况 下，需要剪裁
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
                        // 一些机型无法从getData中获取uri，则需手动指定拍照后存储照片的Uri
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
            startActivityForResult(intent, AddBoxActivityNew.PICKPHOTO);// test
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
            startActivityForResult(intent, AddBoxActivityNew.PICKPHOTO);
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
