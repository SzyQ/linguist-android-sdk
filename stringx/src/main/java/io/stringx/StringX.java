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
    private boolean isLanguageSupported;
    private SharedPreferences preferences;
    private Locale defaultLocale;
    private Options options;
    private boolean isTranslationChecked;
    @Nullable
    private Locale locale;
    private TranslationListener listener;
    private boolean isForcingDefaultLocale;

    public StringX(Options options) {
        Context context = options.getContext();
        this.options = options;
        defaultLocale = Locale.getDefault();
        try {
            preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
            StringXLanguageReceiver.from(context).addListener(this);
            isLanguageSupported = true;
            forceDefault(context);
        } catch (UnsupportedLanguageException e) {
            SXLog.e("Unsupported locale" + defaultLocale.getDisplayLanguage() + "-" + defaultLocale.getDisplayCountry(), e);
            if (listener != null) {
                listener.onLanguageNotSupported(defaultLocale);
            }
        }
    }

    /**
     * @param context
     * @return Instance of StringX service or null if it's not initialised or device language is not supported
     */
    public static StringX get(@NonNull Context context) {
        StringX stringX = null;
        if (isInitialised(context)) {
            stringX = ((Translatable) context.getApplicationContext()).getStringX();
        } else {
            throw new IllegalStateException("StringX is not initialised. Follow instructions here http://www.stringx.io/docs/guides");
        }
        return stringX;
    }

    private static boolean isInitialised(Context context) {
        Context applicationContext = context.getApplicationContext();
        return applicationContext instanceof Translatable &&
                ((Translatable) applicationContext).getStringX() != null;
    }

    void forceDefault(Context context) throws UnsupportedLanguageException {
        boolean translationAvailable = isTranslationAvailable();
        boolean enabled = isEnabled();
        if (translationAvailable && !enabled) {
            forceLocale(context, getAppDefaultLanguage().toLocale(), true);
        }
    }

    public boolean isTranslationAvailable() {
        try {
            return isLanguageSupported && getOptions().getAutoTranslatedLanguages().contains(getDeviceLanguage());
        } catch (UnsupportedLanguageException e) {
            return false;
        }
    }

    public void restart() {
        options.getRestartStrategy().restart();
    }

    private void forceLocale(Context context, @Nullable Locale locale, boolean isDefault) {
        if (locale == null) {
            return;
        }
        SXLog.d("Forcing " + locale.getDisplayLanguage());
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
        forceLocale(context, locale, false);
    }

    boolean isForcingLocale() {
        return !isForcingDefaultLocale && locale != null && !locale.equals(defaultLocale);
    }

    //TODO this exception won't be thrown because device is not supported and StringX will not be returned
    boolean isEnabled() throws UnsupportedLanguageException {
        return preferences.getBoolean(getPreferenceKey(), false);
    }

    public void setEnabled(boolean isEnabled) {
        try {
            preferences
                    .edit()
                    .putBoolean(getPreferenceKey(), isEnabled)
                    .apply();
        } catch (UnsupportedLanguageException e) {
            SXLog.w("Couldn't toggle translation. Language is not supported!");
        }
    }

    public boolean isOptOut() {
        return preferences.getBoolean(KEY_OPT_OUT, false);
    }

    public void setOptOut(boolean isOptOut) {
        preferences
                .edit()
                .putBoolean(KEY_OPT_OUT, isOptOut)
                .apply();
    }

    private String getPreferenceKey() throws UnsupportedLanguageException {
        return KEY_LANGUAGE_ENABLED + getDeviceLanguage().getCode();
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
            SXLog.w("Unsupported device language!");
            return;
        }
        isTranslationChecked = true;
        showTranslationHint(activity);
    }

    public void showTranslationHint(Activity activity) {
        Intent intent = new Intent(activity, StringXOverlayActivity.class);
        activity.startActivityForResult(intent, StringXOverlayActivity.REQUEST_CODE);
    }

    List<Language> getSupportedLanguages() {
        return options.getSupportedLanguages();
    }

    public Options getOptions() {
        return options;
    }

    @Override
    public void onLanguageChanged(Language language) {
        defaultLocale = language.toLocale();
        isTranslationChecked = false;
    }

    Language getDeviceLanguage() throws UnsupportedLanguageException {
        return Language.fromLocale(defaultLocale);
    }

    public TranslationListener getListener() {
        return listener;
    }

    public void setListener(TranslationListener listener) {
        this.listener = listener;
    }

    Language getAppDefaultLanguage() {
        return getOptions().getDefaultLanguage();
    }

    public interface TranslationListener {
        void onTranslationCanceled();

        void onTranslationDisabled();

        void onTranslationEnabled();

        void onLanguageNotSupported(Locale defaultLocale);
    }
}
