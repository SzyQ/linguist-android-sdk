package io.stringx;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

final class PreferencesCache implements Cache {

    private final SharedPreferences preferences;

    PreferencesCache(Context context) {
        preferences = context.getSharedPreferences(StringX.PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public String get(String text) {
        return preferences.getString(text, null);
    }

    @Override
    public void put(String text, String translated) {
        preferences
                .edit()
                .putString(text, translated)
                .apply();
    }
}
