package io.stringx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

import java.util.List;
import java.util.Locale;

public class StringX implements StringXLanguageReceiver.OnLanguageChanged {
    private static final String PREFERENCE_NAME = "StringX";
    private static final String KEY_LANGUAGE_ENABLED = "KEY_ENABLED_";
    private static final String KEY_OPT_OUT = "KEY_OPTED_OUT";
    private final SharedPreferences preferences;
    private Locale defaultLocale;
    private Options options;
    private boolean isTranslationChecked;
    @Nullable
    private Locale locale;
    private TranslationListener listener;
    private boolean isForcingDefaultLocale;

    public StringX(Options options) throws UnsupportedLanguageException {
        Context context = options.getContext();
        this.options = options;
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        defaultLocale = Locale.getDefault();
        StringXLanguageReceiver.from(context).addListener(this);
        forceDefault(context);
    }

    public void forceDefault(Context context) throws UnsupportedLanguageException {
        if (isTranslationAvailable() && !isEnabled()) {
            forceLocale(context, getAppLanguage().toLocale(),true);
        }
    }

    public boolean isTranslationRequired() throws UnsupportedLanguageException {
        return !getOptions().getSupportedLanguages().contains(getDeviceLanguage());
    }

    public boolean isTranslationAvailable() throws UnsupportedLanguageException {
        return getOptions().getAutoTranslatedLanguages().contains(getDeviceLanguage());
    }

    @Nullable
    public static StringX get(@NonNull Context context) {
        StringX stringX = null;
        if (isInitialised(context)) {
            stringX = ((Translatable) context.getApplicationContext()).getStringX();
        }
        return stringX;
    }

    private static boolean isInitialised(Context context) {
        Context applicationContext = context.getApplicationContext();
        return applicationContext instanceof Translatable &&
                ((Translatable) applicationContext).getStringX() != null;
    }

    public void restart(){
        options.getRestartStrategy().restart();
    }

    private void forceLocale(Context context, @Nullable Locale locale, boolean isDefault) {
        if (locale == null) {
            return;
        }
        isForcingDefaultLocale = isDefault;
        Resources res = context.getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        Locale.setDefault(locale);
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, displayMetrics);
        this.locale = locale;
    }
    public void forceLocale(Context context, @Nullable Locale locale) {
        forceLocale(context,locale,false);
    }

    public boolean isForcingLocale() {
        return !isForcingDefaultLocale && locale != null && !locale.equals(defaultLocale);
    }

    public boolean isEnabled() throws UnsupportedLanguageException {
        return preferences.getBoolean(getPreferenceKey(), false);
    }

    public void setEnabled(boolean isEnabled) throws UnsupportedLanguageException {
        preferences
                .edit()
                .putBoolean(getPreferenceKey(), isEnabled)
                .apply();
    }

    public boolean isOptOut() throws UnsupportedLanguageException {
        return preferences.getBoolean(KEY_OPT_OUT, false);
    }

    public void setOptOut(boolean isOptOut) {
        preferences
                .edit()
                .putBoolean(KEY_OPT_OUT, isOptOut)
                .apply();
    }

    public String getPreferenceKey() throws UnsupportedLanguageException {
        return KEY_LANGUAGE_ENABLED + getDeviceLanguage().getCode();
    }

    public void refresh() {
        isTranslationChecked = false;
    }

    public void onResume(Activity activity) {
        try {
            if (isTranslationChecked ||
                    !isTranslationAvailable() ||
                    isEnabled() ||
                    isOptOut()) {
                isTranslationChecked = true;
                return;
            }
        } catch (UnsupportedLanguageException e) {
            LL.w("Unsupported device language!");
            return;
        }
        isTranslationChecked = true;
        showTranslationHint(activity);
    }

    public void showTranslationHint(Activity activity) {
        Intent intent = new Intent(activity, StringXOverlayActivity.class);
        activity.startActivityForResult(intent, StringXOverlayActivity.REQUEST_CODE);
    }

    public List<Language> getSupportedLanguages() {
        return options.getSupportedLanguages();
    }

    public Options getOptions() {
        return options;
    }

    @Override
    public void onLanguageChanged(Language language) {
        defaultLocale = language.toLocale();
    }

    public Language getDeviceLanguage() throws UnsupportedLanguageException {
        return Language.fromLocale(defaultLocale);
    }

    public TranslationListener getListener() {
        return listener;
    }

    public void setListener(TranslationListener listener) {
        this.listener = listener;
    }

    public Language getAppLanguage() {
        return getOptions().getDefaultLanguage();
    }

    public interface TranslationListener {
        void onTranslationCanceled();

        void onTranslationDisabled();

        void onTranslationEnabled();
    }
}
