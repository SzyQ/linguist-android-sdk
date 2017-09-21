package io.stringx;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

final class PreferencesCache implements Cache {

    private static final String KEY_OPT_OUT = "KEY_OPT_OUT";
    private static final String KEY_ENABLED = "KEY_ENABLED_";
    private final SharedPreferences preferences;

    PreferencesCache(Context context) {
        preferences = context.getSharedPreferences("Stringx", Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public String get(String text) {
        return preferences.getString(text, null);
    }

    @Override
    public boolean isOptOut() {
        return preferences.getBoolean(KEY_OPT_OUT, false);
    }

    @Override
    public void setOptOut(boolean isOptedOut) {
        preferences.edit()
                .putBoolean(KEY_OPT_OUT, isOptedOut)
                .apply();
    }

    @Override
    public boolean isEnabled(Language language) {
        return preferences.getBoolean(KEY_ENABLED + language.getCode(), false);
    }

    @Override
    public void setEnabled(Language language, boolean isEnabled) {
        preferences
                .edit()
                .putBoolean(KEY_ENABLED + language.getCode(), isEnabled)
                .apply();
    }

    @Override
    public void put(String text, String translated) {
        preferences
                .edit()
                .putString(text, translated)
                .apply();
    }
}
