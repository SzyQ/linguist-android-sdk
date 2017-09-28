package io.stringx;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.RemoteException;
import android.preference.Preference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.app.LinguistAppCompatDelegate;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.stringx.translate.AndroidTranslator;
import io.stringx.translate.Translator;

import static android.app.Activity.RESULT_OK;

public class StringX implements Translator, StringXLanguageReceiver.OnLanguageChanged {
    static final String PREFERENCE_NAME = "StringX";
    static final String KEY_ENABLED = "KEY_ENABLED";
    private static final String KEY_LANGUAGE_ENABLED = "KEY_ENABLED_";
    private final SharedPreferences preferences;
    private final Cache cache;
    private Locale defaultLocale;
    private Context context;
    private Options options;
    private boolean isTranslationChecked;
    private Translator translator;
    private Boolean isValidConfig;
    @Nullable
    private Locale locale;
    private TranslationListener listener;

    public StringX(Application application, Options options) {
        this.context = application.getApplicationContext();
        this.options = options;
        StringXLanguageReceiver languageReceiver = new StringXLanguageReceiver(context);
        cache = new PreferencesCache(context, getDeviceLanguage());
        translator = new AndroidTranslator(this, this.cache);
        languageReceiver.addListener((StringXLanguageReceiver.OnLanguageChanged) cache);
        languageReceiver.addListener(this);
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        defaultLocale = Locale.getDefault();
    }

    @Nullable
    public static StringX get(@NonNull Context context) {
        StringX stringX = null;
        if (isInitialised(context)) {
            stringX = ((Translatable) context.getApplicationContext()).getStringX();
        }
        if (stringX == null) {
            LL.w("Application needs to extend Translatable interface and provide StringX instance.");
            return null;
        }
        return stringX;
    }

    public static Context wrap(Context base) {
        return isTranslationAllowed(base) ? StringXContextWrapper.wrap(base) : base;
    }

    public static MenuInflater wrap(Activity activity, MenuInflater menuInflater) {
        return isTranslationAllowed(activity) ? StringXMenuInflater.wrap(activity, menuInflater) : menuInflater;
    }

    @Nullable
    public static AppCompatDelegate wrap(AppCompatActivity activity, AppCompatCallback callback) {
        return isTranslationAllowed(activity) ? LinguistAppCompatDelegate.wrap(activity, callback) : null;
    }

    private static boolean isInitialised(Context context) {
        Context applicationContext = context.getApplicationContext();
        return applicationContext instanceof Translatable &&
                ((Translatable) applicationContext).getStringX() != null;
    }

    private static boolean isTranslationAllowed(Context context) {
        StringX stringX = get(context);
        return stringX != null && stringX.isValidConfig() && !stringX.isForcingLocale();
    }

    public static Language getDeviceLanguage() {
        return Language.fromLocale(Locale.getDefault());
    }

    public void forceLocale(Application application, @Nullable Locale locale) {
        Resources res = application.getApplicationContext().getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        if(locale == null){
            locale = defaultLocale;
        }
        Locale.setDefault(locale);
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, displayMetrics);
        this.locale = locale;
    }

    private boolean isForcingLocale() {
        return locale != null && !locale.equals(defaultLocale);
    }

    public boolean isValidConfig() {
        if (isValidConfig == null) {
            isValidConfig = isEnabled() && isEnabled(getDeviceLanguage());
        }
        return isValidConfig;
    }

    public boolean isEnabled() {
        return preferences.getBoolean(KEY_ENABLED, false);
    }

    public void setEnabled(boolean isEnabled) {
        preferences.edit()
                .putBoolean(KEY_ENABLED, isEnabled)
                .apply();
    }

    public boolean isEnabled(Language language) {
        return preferences.getBoolean(KEY_LANGUAGE_ENABLED + language.getCode(), false);
    }

    public void setEnabled(Language language, boolean isEnabled) {
        preferences
                .edit()
                .putBoolean(KEY_LANGUAGE_ENABLED + language.getCode(), isEnabled)
                .apply();
    }

    public void refresh() {
        isTranslationChecked = false;
    }

    public void setTranslator(Translator translator) {
        this.translator = translator;
    }

    public void onResume(Activity activity) {
        if (isTranslationChecked) {
            return;
        }
        Language deviceLanguage = getDeviceLanguage();
        if (isEnabled(deviceLanguage)) {
            return;
        }
        if (isEnabled()) {
            return;
        }
        if (options.getSupportedLanguages().contains(deviceLanguage)) {
            return;
        }
        isTranslationChecked = true;
        Intent intent = new Intent(activity, StringxOverlayActivity.class);
        activity.startActivityForResult(intent, StringxOverlayActivity.REQUEST_CODE);
    }

    Locale getDefaultLocale() {
        return new Locale(options.getDefaultLanguage().getCode());
    }

    void fetch(ConfigCallback callback) throws RemoteException {
        List<Pair<Integer, String>> supportedResources = getResourcesIds();
        Utils.getAppStrings(this, context, supportedResources, callback);
    }

    @NonNull
    private List<Pair<Integer, String>> getResourcesIds() {
        List<Pair<Integer, String>> supportedResources = Utils.getAppStringResources(context, options.getStringClasses());
        List<Pair<Integer, String>> excludedResources = Utils.getAppStringResources(context, options.getExcludedClasses());
        Iterator<Pair<Integer, String>> iterator = supportedResources.iterator();
        while (iterator.hasNext()) {
            Pair<Integer, String> resourceId = iterator.next();
            if (excludedResources.contains(resourceId)) {
                iterator.remove();
            } else if (options.getExcludedStringIds().contains(resourceId.first)) {
                iterator.remove();
            }
        }
        return supportedResources;
    }

    void applyTranslation(Map<String, String> translation) {
        for (String text : translation.keySet()) {
            String translated = translation.get(text);
            cache.put(text, translated);
        }
    }

    private Language getDefaultLanguage() {
        return options.getDefaultLanguage();
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == StringxOverlayActivity.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                return true;
            }
        }
        return false;
    }

    public List<Language> getSupportedLanguages() {
        return options.getSupportedLanguages();
    }

    public Options getOptions() {
        return options;
    }

    @Override
    public String translate(String string) {
        if(!isValidConfig() || isForcingLocale()){
            return string;
        }
        return translator.translate(string);
    }

    @Override
    public View translate(View view) {
        if(!isValidConfig() || isForcingLocale()){
            return view;
        }
        return translator.translate(view);
    }

    @Override
    public Preference translate(Preference preference) {
        if(!isValidConfig() || isForcingLocale()){
            return preference;
        }
        return translator.translate(preference);
    }

    @Override
    public void translate(Menu menu) {
        if(!isValidConfig() || isForcingLocale()){
            return;
        }
        translator.translate(menu);
    }

    @Override
    public CharSequence translate(CharSequence text) {
        if(!isValidConfig() || isForcingLocale()){
            return text;
        }
        return translator.translate(text);
    }

    @Override
    public CharSequence[] translate(CharSequence[] textArray) {
        if(!isValidConfig() || isForcingLocale()){
            return textArray;
        }
        return translator.translate(textArray);
    }

    @Override
    public String[] translate(String[] textArray) {
        if(!isValidConfig() || isForcingLocale()){
            return textArray;
        }
        return translator.translate(textArray);
    }

    @Override
    public void onLanguageChanged(Language language) {
        defaultLocale = new Locale(language.getCode());
        invalidate();
    }

    public void invalidate() {
        isValidConfig = null;
    }

    public Language getDefaultDeviceLanguage() {
        return Language.fromLocale(defaultLocale);
    }

    public interface TranslationListener{
        void onTranslationCanceled();

        void onTranslationDisabled();

        void onTranslationStarted();

        void onTranslationLaunched();

        void onApplicationTranslated();
    }

    public TranslationListener getListener() {
        return listener;
    }

    public void setListener(TranslationListener listener) {
        this.listener = listener;
    }
}
