package mobi.klimaszewski.linguist;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

public class PreferencesCache implements Cache {

    private final SharedPreferences preferences;

    public PreferencesCache(Context context) {
        preferences = context.getSharedPreferences("Linguist", Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public String get(String text) {
        return preferences.getString(text, null);
    }

    @Override
    public boolean isNeverTranslateEnabled(String countryCode) {
        return preferences.getBoolean(countryCode, false);
    }

    @Override
    public void setNeverTranslateEnabled(String countryCode, boolean isEnabled) {
        preferences.edit().putBoolean(countryCode, isEnabled).apply();
    }

    @Override
    public void put(String text, String translated) {
        preferences
                .edit()
                .putString(text,translated)
                .apply();
    }
}
