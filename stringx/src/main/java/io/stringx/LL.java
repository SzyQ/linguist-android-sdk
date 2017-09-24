package io.stringx;

import android.util.Log;

public class LL {
    public static final String TAG = "StringX";

    public static void d(String string) {
        Log.d(TAG, string);
    }

    public static void v(String string) {
        Log.v(TAG, string);
    }

    public static void e(String error, Throwable e) {
        Log.e(TAG, error, e);
    }

    public static void d(String s, Exception exception) {
        Log.d(TAG, s, exception);
    }

    public static void w(String s) {
        Log.w(TAG,s);
    }
}
