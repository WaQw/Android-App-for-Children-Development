package com.viadroid.app.growingtree.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class GsonUtil {


    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return new Gson().fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return new Gson().fromJson(json, typeOfT);
    }
}
