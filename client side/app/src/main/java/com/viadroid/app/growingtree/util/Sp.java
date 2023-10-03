package com.viadroid.app.growingtree.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.Map;
import java.util.Set;

/**
 * 使用建造者模式对 {@link SharedPreferences} 一次封装
 */
public class Sp {

    private SharedPreferences mSharedPreferences;

    private Sp() {
    }

    private Sp(Context context, String preferName, int mode) {
        mSharedPreferences = context.getSharedPreferences(preferName, mode);
    }

    private SharedPreferences.Editor getEditor() {
        return mSharedPreferences.edit();
    }

    //===============================================
    // 下面是存入、移除数据的方法，
    // 对应SharedPreferences.Editor里的方法
    //===============================================

    public boolean putObj(String key, Object value) {
        return putString(key, getJson(value));
    }

    public boolean putString(String key, String value) {
        return getEditor().putString(key, value).commit();
    }

    public boolean putBoolean(String key, boolean value) {
        return getEditor().putBoolean(key, value).commit();
    }

    public boolean putInt(String key, int value) {
        return getEditor().putInt(key, value).commit();
    }

    public boolean putFloat(String key, float value) {
        return getEditor().putFloat(key, value).commit();
    }

    public boolean putLong(String key, long value) {
        return getEditor().putLong(key, value).commit();
    }

    public boolean putStringSet(String key, Set<String> value) {
        return getEditor().putStringSet(key, value).commit();
    }

    public boolean remove(String key) {
        return getEditor().remove(key).commit();
    }

    public boolean clear() {
        return getEditor().clear().commit();
    }


    //===============================================
    // 下面是获取数据、是否已存在的方法
    //===============================================

    public <T> T getObj(String key, String defValue, Class<T> tClass) {
        String getStr = getString(key, defValue);
        return (null == getStr || defValue.equals(getStr) || "".equals(getStr))
                ? (T) defValue : getObj(getStr, tClass);
    }

    public String getString(String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);

    }

    public boolean getBoolean(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return mSharedPreferences.getFloat(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return mSharedPreferences.getLong(key, defValue);
    }

    public Set<String> getStringSet(String key, Set<String> defValue) {
        return mSharedPreferences.getStringSet(key, defValue);
    }

    public Map<String, ?> getAll() {
        return mSharedPreferences.getAll();
    }

    public boolean contains(String key) {
        return mSharedPreferences.contains(key);
    }

    //===============================================
    // 下面是通过Google的Gson解析Object、json的方法
    // Gson下载地址：https://github.com/google/gson
    //===============================================
    private String getJson(Object obj) {
        return new Gson().toJson(obj);
    }

    private <T> T getObj(String json, Class<T> tClass) {
        return new Gson().fromJson(json, tClass);
    }

    /**
     * Builder构造者
     */
    public static class Builder {
        /**
         * 默认的文件名
         */
        public final static String DEFAULT_PREFER_NAME = "prefer";
        /**
         * 默认的mode
         */
        public final static int DEFAULT_PREFER_MODE = Context.MODE_PRIVATE;

        private String mPreferName = DEFAULT_PREFER_NAME;
        private int mMode = DEFAULT_PREFER_MODE;

        private Context mContext;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setPreferName(String preferName) {
            if (null != preferName && !"".equals(preferName)) {
                mPreferName = preferName;
            }
            return this;
        }

        public Builder setMode(int mode) {
            mMode = mode;
            return this;
        }

        public Sp build() {
            Sp sp = new Sp(mContext, mPreferName, mMode);
            return sp;
        }
    }
}