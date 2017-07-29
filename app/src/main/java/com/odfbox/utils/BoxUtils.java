package com.odfbox.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static android.os.Environment.getExternalStorageDirectory;

public class BoxUtils {
    private static final String SDCARD_ROOT = getExternalStorageDirectory().getPath();
    private static final String DOCTOR_SDCARD_PATH = SDCARD_ROOT
            + "/zhsaq_app";

    public static final String APP_PACKAGE_NAME = "com.zhsaq.family";
    public static final String APP_CACHE_PHONE_PATH = "/data/data/"
            + APP_PACKAGE_NAME + "/files/cache/";

    public static String getTmpCachePath() {
        String cachePath;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED) == false) {
            cachePath = APP_CACHE_PHONE_PATH;
        } else {
            cachePath = DOCTOR_SDCARD_PATH + "/cache/";
        }

        tryMakePath(cachePath);

        return cachePath;
    }

    /**
     * try to make the path if not existing, and change the mode of path to 777
     *
     * @param pathName
     */
    private static void tryMakePath(String pathName) {
        if (!new File(pathName).exists()) {
            new File(pathName).mkdirs();

            Process p;
            int status;
            try {
                p = Runtime.getRuntime().exec("chmod 777 " + pathName);
                status = p.waitFor();
                if (status == 0) {
                } else {
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    /**
     * @param @param  ctx
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     * @Title: readDeviceId
     * @Description: 获取手机信息
     */
    public static String readDeviceId(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        if (TextUtils.isEmpty(deviceId)) {
            String time = Long.toString((System.currentTimeMillis() / (1000 * 60 * 60)));
            deviceId = time + time;
        }
        return deviceId;
    }

    public static String getImgBase64(String imgFile) {
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Base64", "getImgBase64: " + Base64.encodeBase64(data));
        return new String(Base64.encodeBase64(data));
    }

    private static HashMap<Integer, Integer> zoomCache = new HashMap<>();

    static {
        zoomCache.put(3, 2000000);
        zoomCache.put(4, 1000000);
        zoomCache.put(5, 500000);
        zoomCache.put(6, 200000);
        zoomCache.put(7, 100000);
        zoomCache.put(8, 50000);
        zoomCache.put(9, 25000);
        zoomCache.put(10, 20000);
        zoomCache.put(11, 10000);
        zoomCache.put(12, 5000);
        zoomCache.put(13, 2000);
        zoomCache.put(14, 1000);
        zoomCache.put(15, 500);
        zoomCache.put(16, 200);
        zoomCache.put(17, 100);
        zoomCache.put(18, 50);
        zoomCache.put(19, 20);
        zoomCache.put(20, 10);
        zoomCache.put(21, 5);
        zoomCache.put(22, 2);


    }

    public static int getZoom(int level) {
//        @0,
//        @0,
//        @0, // < 3
//        @2000000, // level 3
//        @1000000, // 4
//        @500000, // 5
//        @200000, // 6
//        @100000, // 7
//        @50000, // 8
//        @25000, // 9
//        @20000, // 10
//        @10000, // 11
//        @5000, // 12
//        @2000, // 13
//        @1000, // 14
//        @500, // 15
//        @200, // 16
//        @100, // 17
//        @50, // 18
//        @20, // 19
//        @10, // 20
//        @5, // 21
//        @2 // 22

        Integer key = new Integer(level);
        if (zoomCache.containsKey(key)) {
            return zoomCache.get(key)* 10 ;
        }
        return 200* 10 ;
    }

}
