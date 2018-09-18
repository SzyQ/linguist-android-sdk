package mobi.klimaszewski.linguist;

import android.util.Log;

import mobi.klimaszewski.linguist.BuildConfig;

class LinguistLog {
    private static final String TAG = "Linguist";

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
