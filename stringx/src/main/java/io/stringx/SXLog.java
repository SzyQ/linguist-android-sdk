package io.stringx;

import android.util.Log;

import io.stringx.sdk.BuildConfig;

class SXLog {
    private static final String TAG = "stringX";

    public static void d(String string) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, string);
        }
    }

    public static void v(String string) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, string);
        }
    }

    public static void e(String error, Throwable e) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, error, e);
        }
    }

    public static void d(String s, Exception exception) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, s, exception);
        }
    }

    public static void w(String s) {
        if (BuildConfig.DEBUG) {
            Log.w(TAG, s);
        }
    }
}
