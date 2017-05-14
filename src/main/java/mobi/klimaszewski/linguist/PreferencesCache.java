package mobi.klimaszewski.linguist;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

public class PreferencesCache implements Cache {

    private static final String KEY_NEVER_TRANSLATE = "NEVER_TRANSLATE_";
    private static final String KEY_TRANSLATE_ENABLED = "TRANSLATE_ENABLED_";
    private static final String KEY_OPT_OUT = "NEVER_TRANSLATE_";
    private final SharedPreferences preferences;

    public PreferencesCache(Context context) {
        preferences = context.getSharedPreferences("Linguist", Context.MODE_PRIVATE);
    }

    private static String getNeverTranslateKey(String languageCode) {
        return KEY_NEVER_TRANSLATE + languageCode;
    }

    private static String getTranslationEnabledKey(String languageCode) {
        return KEY_TRANSLATE_ENABLED + languageCode;
    }

    @Nullable
    @Override
    public String get(String text) {
        return preferences.getString(text, null);
    }

    @Override
    public boolean isNeverTranslateEnabled(String countryCode) {
        return preferences.getBoolean(getNeverTranslateKey(countryCode), false);
    }

    @Override
    public void setNeverTranslateEnabled(String countryCode, boolean isEnabled) {
        preferences.edit()
                .putBoolean(getNeverTranslateKey(countryCode), isEnabled)
                .apply();
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
    public void setTranslationEnabled(String countryCode, boolean isEnabled) {
        preferences.edit()
                .putBoolean(getTranslationEnabledKey(countryCode), isEnabled)
                .apply();
    }

    @Override
    public boolean isTranslationEnabled(String languageCode) {
        return preferences.getBoolean(getTranslationEnabledKey(languageCode), false);
    }

    @Override
    public void put(String text, String translated) {
        preferences
                .edit()
                .putString(text, translated)
                .apply();
    }
}
