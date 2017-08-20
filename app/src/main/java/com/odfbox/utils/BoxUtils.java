package com.odfbox.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
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
            + "/odfbox_app";

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
     * @Description: 获取手机信息
     */
    public static String readDeviceId(Context ctx) {
        String device_id = "" + android.provider.Settings.Secure.getString(ctx.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        if (device_id.length() < 5) {
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(ctx.TELEPHONY_SERVICE);
            String tmdevice_id = "" + tm.getDeviceId();
            if (tmdevice_id.length() < 5) {
                String deviceSereisId = tm.getSimSerialNumber();
                if (deviceSereisId.length() < 5) {
                    String m_szDevIDShort = "35" + // we make this look like a
                            // valid IMEI
                            Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 + Build.TYPE.length() % 10 + Build.USER.length() % 10; // 13
                    // digits
                    device_id = "imei" + m_szDevIDShort;
                } else {
                    device_id = "tmSimSerialNumber" + deviceSereisId;
                }
            } else {
                device_id = "tmDeviceId" + tmdevice_id;
            }
        }

        Integer.parseInt(device_id, 16);
        Log.d("device_id", "readDeviceId: " + Integer.parseInt(device_id, 16));
        return Integer.parseInt(device_id, 16) + "";
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
        Integer key = new Integer(level);
        if (zoomCache.containsKey(key)) {
            return zoomCache.get(key) * 10;
        }
        return 200 * 10;
    }


    public static boolean getIsUpdate(String current, String recent) {
        String a1 = "";
        String a2 = "";
        String b1 = "";
        String b2 = "";
        a1 = current.substring(0, current.indexOf("."));
        a2 = recent.substring(0, recent.indexOf("."));
        b1 = current.substring(current.indexOf(".") + 1, current.length());
        b2 = recent.substring(recent.indexOf(".") + 1, recent.length());
        if (Integer.parseInt(a1) < Integer.parseInt(a2)) {
            return true;
        } else if (Integer.parseInt(a1) == Integer.parseInt(a2)) {
            if (Float.parseFloat(b1) < Float.parseFloat(b2)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
