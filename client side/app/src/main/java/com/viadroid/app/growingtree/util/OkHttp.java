package com.viadroid.app.growingtree.util;


import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.viadroid.app.growingtree.App;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * OkHttp3
 */

public class OkHttp {

    private static final String TAG = OkHttp.class.getSimpleName();

    private static final int TIME_OUT = 1000 * 5;

    private static OkHttp mInstance = null;

    private OkHttpClient mHttpClient;
    private Handler mHandler;

    private String url;
    private Headers.Builder headerBuilder;
    private FormBody.Builder bodyBuilder;
    private Map<String, String> queryParameters;

    public static OkHttp getInstance() {
        if (mInstance == null) {
            synchronized (OkHttp.class) {
                if (mInstance == null) {
                    mInstance = new OkHttp();
                }
            }
        }
        mInstance.url = null;
        mInstance.headerBuilder = new Headers.Builder();
        mInstance.bodyBuilder = new FormBody.Builder();
        mInstance.queryParameters = new HashMap<>();

        mInstance.headerBuilder.add("username", App.getApp().getUserName());
        mInstance.headerBuilder.add("TOKEN", App.getApp().getToken());
        return mInstance;
    }

    private OkHttp() {
        mHttpClient = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
//                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize))
                .build();
        mHandler = new Handler();
    }

    public OkHttp setCache(Context context, int cacheSize) {
        File cacheFile = context.getExternalCacheDir();
        mHttpClient = mHttpClient.newBuilder()
                .cache(new Cache(cacheFile.getAbsoluteFile(), cacheSize))
                .build();
        return mInstance;
    }

    public OkHttp setUrl(String url) {
        this.url = url;
        return mInstance;
    }

    public OkHttp addHeader(String line) {
        headerBuilder.add(line);
        return mInstance;
    }

    public OkHttp addHeader(String name, String value) {
        headerBuilder.add(name, value);
        return mInstance;
    }

    public OkHttp addQueryParameter(String name, String value) {
        queryParameters.put(name, value);
        return mInstance;
    }

    public OkHttp addBodyParam(String name, String value) {
        bodyBuilder.add(name, value);
        return mInstance;
    }


    //异步get
    public void asyncGet(String url, NetCallBack callBack) {
        HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();
        for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
            builder.addQueryParameter(entry.getKey(), entry.getValue());
            L.d(TAG, "Parameter: " + entry.getKey() + ":" + entry.getValue());
        }
        Request request = new Request.Builder()
                .url(builder.build())
                .headers(headerBuilder.build())
                .build();

        handleResult(request, callBack);
    }

    public void asyncPost(String url, NetCallBack callBack) {

        Request request = new Request.Builder()
                .url(url)
                .build();
    }

    public void asyncPostJson(String json, NetCallBack callBack) {

        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        json);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .headers(headerBuilder.build())
                .post(requestBody)
                .build();

        handleResult(request, callBack);
    }

    public void asyncPostForm(NetCallBack callBack) {
        Request request = new Request.Builder()
                .url(url)
                .headers(headerBuilder.build())
                .post(bodyBuilder.build())
                .build();

        handleResult(request, callBack);
    }

    /**
     * 上传图片
     *
     * @param imagePath 图片路径
     */
    public void asyncUploadImage(String url, String imagePath, NetCallBack callBack) {
        Log.d(TAG, imagePath);
        File file = new File(imagePath);
        RequestBody image = RequestBody.create(MediaType.parse("image/png"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", imagePath, image)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        handleResult(request, callBack);
    }


    private void handleResult(Request request, final NetCallBack callBack) {
        Call call = mHttpClient.newCall(request);
        call.enqueue(new OkCallback() {
            @Override
            public void onFailure(@NonNull final Call call, @NonNull IOException e) {
                L.e(TAG, "onFailure:", e);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailure(call);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull final Call call, @NonNull final Response response) throws IOException {

//                int code = response.code();
//                Protocol protocol = response.protocol();
//                Headers headers = response.headers();
                final String bodyStr = response.body().string();

                L.d(TAG, "onResponse:" + bodyStr);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(call, response, bodyStr);
                    }
                });
            }
        });
    }


    public interface NetCallBack {
        void onFailure(Call call);

        void onSuccess(Call call, Response response, String bodyStr);
    }
}
