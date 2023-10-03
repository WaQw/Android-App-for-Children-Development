package com.viadroid.app.growingtree.util;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * OkHttp Callback
 */

public interface OkCallback<T> extends Callback {
    @Override
    void onFailure(@NonNull Call call, @NonNull IOException e);

    @Override
    void onResponse(@NonNull Call call, @NonNull Response response) throws IOException;
}
