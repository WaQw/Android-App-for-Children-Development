package com.viadroid.app.growingtree.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast
 */

public final class Ts {

    public static void showShort(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
