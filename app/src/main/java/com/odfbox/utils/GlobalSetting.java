package com.odfbox.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class GlobalSetting {

    /**
     * 数据存储工具类
     */

    private static Context mContext;
    private static SharedPreferences mPrefs;
    private static SharedPreferences mPrefsLogin;
    private static GlobalSetting mSpInstance;

    private static final String USER_TOKEN = "user_token";
    private static final String PREFERENCE_NAME = "";
    private static final String PREFERENCE_NAME_LOGIN = "";
    private static final String USER_ID = "user_id";//用户ID
    private static final String ORG_ID = "org_id";
    private static final String USER_PHONE = "user_phone";
    private static final String USER_ADDRESS = "user_address";
    private static final String USER_ADDRESS1 = "user_address1";
    private static final String USER_ADDRESS1_AREA = "user_address1_area";
    private static final String USER_ADDRESS2_AREA = "user_address2_area";
    private static final String USER_ADDRESS2 = "user_address2";
    private static final String LAN = "lan";
    private static final String LON = "lon";
    private static final String ADDRESS = "address";
    private static final String COVERAGE_RATE = "coverage_rate";
    private static final String TOTAL_ODF_BOX = "total_odf_box";
    private static final String ORG_NAME = "org_name";
    private static final String CHANNEL_ID = "channel_id";

    private GlobalSetting(Context context) {
        mContext = context;
    }

    // 单例x
    public static synchronized GlobalSetting getInstance(Context context) {
        if (mSpInstance == null) {
            mSpInstance = new GlobalSetting(context);
        }
        return mSpInstance;
    }


    // 初始化sp对象
    public SharedPreferences getSharedPreferences() {
        if (mPrefs == null) {
            mPrefs = mContext.getSharedPreferences(PREFERENCE_NAME,
                    Context.MODE_PRIVATE);
        }
        return mPrefs;
    }

    public SharedPreferences getSharedPreferencesLogin() {
        if (mPrefsLogin == null) {
            mPrefsLogin = mContext.getSharedPreferences(PREFERENCE_NAME_LOGIN,
                    Context.MODE_PRIVATE);
        }
        return mPrefsLogin;
    }

    /**
     * 推出登陆清楚用户数据
     */
    public void clear() {
        getSharedPreferences().edit().clear().commit();
    }

    public void clear_Login() {
        getSharedPreferencesLogin().edit().clear().commit();
    }

    /**
     * 存储boolean类型数据
     *
     * @param key
     * @param value
     */
    private void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    private void saveFloat(String key, Float value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    private void saveString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void saveInt(String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void saveUserPhone(String id) {
        saveString(USER_PHONE, id);
    }

    public String getUserPhone() {
        return getSharedPreferences().getString(USER_PHONE, "");
    }

    public void saveUserId(String id) {
        saveString(USER_ID, id);
    }

    public String getUserId() {
        return getSharedPreferences().getString(USER_ID, "");
//        return "210";//有数据用户
    }

    public void saveToken(String id) {
        saveString(USER_TOKEN, "Token " + id);
    }

    public String getToken() {
        return getSharedPreferences().getString(USER_TOKEN, "");
    }

    public void saveAddress(String id) {
        saveString(USER_ADDRESS, id);
    }

    public String getAddress() {
        return getSharedPreferences().getString(USER_ADDRESS, "");
    }


    public String[] getData() {
        String[] strings = {getSharedPreferences().getString(USER_ADDRESS2, ""), getSharedPreferences().getString(USER_ADDRESS2_AREA, "")};
        return strings;
    }

    public void saveOrgId(String id) {
        saveString(ORG_ID, id);
    }

    public String getOrgId() {
        return getSharedPreferences().getString(ORG_ID, "");
    }

    public void saveAddress1(String id, String area) {
        saveString(USER_ADDRESS1, id);
        saveString(USER_ADDRESS1_AREA, area);
    }

    public String[] getAddress1() {
        String[] strings = {getSharedPreferences().getString(USER_ADDRESS1, ""), getSharedPreferences().getString(USER_ADDRESS1_AREA, "")};
        return strings;
    }

    public void saveAddress2(String id, String area) {
        saveString(USER_ADDRESS2, id);
        saveString(USER_ADDRESS2_AREA, area);
    }

    public String[] getAddress2() {
        String[] strings = {getSharedPreferences().getString(USER_ADDRESS2, ""), getSharedPreferences().getString(USER_ADDRESS2_AREA, "")};
        return strings;
    }


    public String[] getCurrentAddress() {
        String[] strings = {getSharedPreferences().getString(LON, ""), getSharedPreferences().getString(LAN, ""), getSharedPreferences().getString(ADDRESS, "")};
        return strings;
    }

    public void saveCurrentAddress(String lon, String lan, String address) {
        saveString(LON, lon);
        saveString(LAN, lan);
        saveString(ADDRESS, address);
    }

    public void saveBoxData(String coverage_rate, String total_odf_box) {
        saveString(COVERAGE_RATE, coverage_rate);
        saveString(TOTAL_ODF_BOX, total_odf_box);
    }

    public String[] getBoxData() {
        String[] strings = {getSharedPreferences().getString(COVERAGE_RATE, "0%"), getSharedPreferences().getString(TOTAL_ODF_BOX, "0")};
        return strings;
    }

    public void saveOrgName(String id) {
        saveString(ORG_NAME, id);
    }

    public String getOrgName() {
        return getSharedPreferences().getString(ORG_NAME, "");
    }


}
