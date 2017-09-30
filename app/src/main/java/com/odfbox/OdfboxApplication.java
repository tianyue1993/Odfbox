package com.odfbox;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.odfbox.utils.GlobalSetting;

import java.util.ArrayList;

/**
 * Created by tianyue on 2017/3/23.
 */
public class OdfboxApplication extends Application {


    public static Context mCon;

    public static OdfboxApplication application;
    public static GlobalSetting prefs;
    /**
     * 接口跟Url
     */
    public static String BASE_URL;


    /**
     * 是否是第一次默认设置
     * 针对MainActivity默认设置Home为显示页面
     * <p/>
     * 每一次从其他页面返回MainActivity可见页面时，都会先初始化一下Home的初始值
     * 无故增多网络请求次数，因此添加限定
     */
    public static boolean isFirstSetDefault = true;
    /**
     * 是否为强制更新
     */
    public static boolean MUSTUPDATE = false;

    /**
     * 添加的打开过的Activity
     */
    public static ArrayList<Activity> activities = new ArrayList<Activity>();

    /**
     * 添加Activity  一键退出调用这个方法
     *
     * @param activity
     */
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * Activity 的一键退出
     */
    public static void finishActivity() {
        for (int i = 0; i < activities.size(); i++) {
            activities.get(i).finish();
        }

    }

    /**
     * 初始化域名信息
     */
    private void initDoMain() {
        //获取本应用程序信息
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //读取信息
        if (null != applicationInfo) {
            this.BASE_URL = applicationInfo.metaData.getString("serverDoMain");
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, "sDCH6H63zs9e1Zqd6lyZOmo6");
        mCon = getApplicationContext();
        application = this;
        prefs = GlobalSetting.getInstance(this);
        initDoMain();
        initImageLoader();
    }

    /**
     * 初始化图片加载框架
     */
    private void initImageLoader() {
        DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(defaultDisplayImageOptions).threadPriority(Thread.NORM_PRIORITY).discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.FIFO).memoryCache(new WeakMemoryCache()).writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
    }

    /**
     * 获取上下文对象
     *
     * @return
     */
    public static Context getContext() {
        return application;
    }

    /**
     * @return 服务器域名
     */
    public static String getServerDoMain() {
        return BASE_URL;
    }


    public static String GETCODE(String number) {
        return BASE_URL + "login/verify-code/" + number + "/?generate=true";
    }

    public static String GETTOKEN(String number, String id) {
        return BASE_URL + "login/token/?access_key=" + number + "&org_id=" + id;
    }

    public static String ACCESSKEY(String number, String code) {
        return BASE_URL + "login/access-key/?mobile=" + number + "&verify_code=" + code;
    }

    public static String ORG(String id) {
        return BASE_URL + "orgs/" + id + "/";
    }

    public static String WARNLIST(String offset, String string, boolean warn) {

        if (warn) {
            return BASE_URL + "orgs/" + prefs.getOrgId() + "/events_history/?offset=" + offset + "&limit=10&ordering=-alarm,-id&odf_box_only=true" + string;
        } else {
            return BASE_URL + "orgs/" + prefs.getOrgId() + "/events_history/?offset=" + offset + "&limit=10&ordering=-id&odf_box_only=true" + string;
        }

    }

    public static String BOXLIST(String offset, String parms) {
        if (parms.equals("")) {
            return BASE_URL + "orgs/" + prefs.getOrgId() + "/odf_boxes/?offset=" + offset + "&limit=10&ordering=-id";
        } else {
            return BASE_URL + "orgs/" + prefs.getOrgId() + "/odf_boxes/?offset=" + offset + "&limit=10&ordering=-id" + parms;
        }

    }

    public static String WORKORDERLIST(String offset, String parms) {
        if (parms.equals("")) {
            return BASE_URL + "orgs/" + prefs.getOrgId() + "/users/" + prefs.getUserId() + "/task_sheets/?offset=" + offset + "&limit=10&ordering=-id&related=odf_box&state_category=待处理";
        } else {
            return BASE_URL + "orgs/" + prefs.getOrgId() + "/users/" + prefs.getUserId() + "/task_sheets/?offset=" + offset + "&limit=10&ordering=-id&related=odf_box" + parms;
        }
    }


    public static String NEARBOX(String lat, String lon, String distance) {
        return BASE_URL + "orgs/" + prefs.getOrgId() + "/odf_boxes/?limit=100&in_range=baidu," + lat + "," + lon + "," + distance;
    }


    public static String ADDBOX() {
        return BASE_URL + "orgs/" + prefs.getOrgId() + "/odf_boxes/";
    }

    public static String EDITBOX(String id) {
        return BASE_URL + "orgs/" + prefs.getOrgId() + "/odf_boxes/" + id + "/";
    }

    public static String TASKLIST(String id) {
        return BASE_URL + "orgs/" + prefs.getOrgId() + "/users/" + prefs.getUserId() + "/task_sheets/" + id + "/task_items/";
    }

    public static String PUTORDER(String id) {
        return BASE_URL + "orgs/" + prefs.getOrgId() + "/users/" + prefs.getUserId() + "/task_sheets/" + id + "/solve/";
    }

    public static String PUTTASKS(String sheet_id, String task_id) {
        return BASE_URL + "orgs/" + prefs.getOrgId() + "/users/" + prefs.getUserId() + "/task_sheets/" + sheet_id + "/task_items/" + task_id + "/solve/";
    }

    public static String ENENTLIST(String task_id, boolean isWarns) {
        if (isWarns) {
            return BASE_URL + "orgs/" + prefs.getOrgId() + "/events_history/?related=odf_box&odf_box=" + task_id + "&ordering=-alarm,-id";
        } else {
            return BASE_URL + "orgs/" + prefs.getOrgId() + "/events_history/?related=odf_box&odf_box=" + task_id + "&ordering=-id";
        }

    }

    public static String BOXDETAIL(String id) {
        return BASE_URL + "orgs/" + prefs.getOrgId() + "/odf_boxes/" + id + "/";

    }

    public static String BOXCOUNT() {
        return BASE_URL + "util_views/odf_box_counter?status=opening,alarming,offline,total";
    }

    public static String USERGUIDE() {
        return BASE_URL + "download/documents/?limit=50";
    }

    public static String EVENTORDER() {
        return BASE_URL + "/orgs/" + prefs.getOrgId() + "/task_sheets/";
    }

    public static String GETUSER() {
        return BASE_URL + "/orgs/" + prefs.getOrgId() + "/users/";
    }

    public static String HANDLE(String id) {
        return BASE_URL + "/orgs/" + prefs.getOrgId() + "/events_history/" + id + "/handle/";
    }

    public static String MAINEVENT() {
        return BASE_URL + "/orgs/" + prefs.getOrgId() + "/events_history/" + "?&ordering=-id&alarm=true&limit=3&odf_box_only=true&treat_state=untreated";
    }

    public static String OPENBOX(String lock_pk) {
        return BASE_URL + "orgs/" + prefs.getOrgId() + "/odf_box_locks/" + lock_pk + "/commands/";
    }


    public static String CONSTANTDEFINE(String type) {
        return BASE_URL + "constant-define/?category=" + type;
    }


    public static String UNTREATED() {
        return BASE_URL + "orgs/" + prefs.getOrgId() + "/events_history/?ordering=-id&alarm=true&treat_state=untreated&limit=1";
    }


    public static String ORDERUNTREATED() {
        return BASE_URL + "orgs/" + prefs.getOrgId() + "/users/" + prefs.getUserId() + "/task_sheets/?state_category=待处理&limit=1";
    }


    public static String VERSION() {
        return BASE_URL + "download/apps/?ordering=-revision&limit=1&platform=Android";
    }

    public static String LOGINSTATE() {
        return BASE_URL + "util_views/login_state/" + prefs.getUserId() + "/";
    }


    public static String STATISTICS() {
        return BASE_URL + "orgs/" + prefs.getOrgId() + "/statistics-months/";
    }

    public static String TASKSTATISTICS() {
        return BASE_URL + "orgs/" + prefs.getOrgId() + "/users/" + prefs.getUserId() + "/statistics-months/";
    }

    public static String getAppVersions() {// 当前应用的版本号
        PackageManager manager = mCon.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(
                    mCon.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // can't run
            e.printStackTrace();
            return "" + 1.0;
        }
    }

}


