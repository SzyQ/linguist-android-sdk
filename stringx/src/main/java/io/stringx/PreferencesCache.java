package io.stringx;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

final class PreferencesCache implements Cache, StringXLanguageReceiver.OnLanguageChanged {

    private SharedPreferences preferences;
    private Context context;

    PreferencesCache(Context context, Language language) {
        this.context = context;
        preferences = getPreferences(language);
    }

    @Nullable
    @Override
    public String get(String text) {
        return preferences.getString(text, null);
    }

    @Override
    public synchronized void put(String text, String translated) {
        preferences
                .edit()
                .putString(text, translated)
                .apply();
    }

    @Override
    public synchronized void onLanguageChanged(Language language) {
        preferences = getPreferences(language);
    }

    private SharedPreferences getPreferences(Language language) {
        return context.getSharedPreferences(StringX.PREFERENCE_NAME + ":" + language.getCode(), Context.MODE_PRIVATE);
    }
}
