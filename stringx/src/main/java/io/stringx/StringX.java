package io.stringx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.preference.Preference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.app.LinguistAppCompatDelegate;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.stringx.ConfigCallback;
import io.stringx.translate.AndroidTranslator;
import io.stringx.translate.Translator;

import static android.app.Activity.RESULT_OK;

public class StringX implements Translator {
    static final String PREFERENCE_NAME = "StringX";
    static final String KEY_ENABLED = "KEY_ENABLED";
    private static final String KEY_LANGUAGE_ENABLED = "KEY_ENABLED_";
    private static SharedPreferences preferences;
    private Context context;
    private Options options;
    private boolean isTranslationChecked;
    private Translator translator;

    public StringX(Context context, Options options) {
        this.context = context;
        this.options = options;
        this.translator = new AndroidTranslator(this, options.getCache());
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
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return stringX;
    }

    public static Context wrap(Context base) {
        return isInitialised(base) && get(base).isEnabled() ? StringXContextWrapper.wrap(base) : base;
    }

    public static MenuInflater wrap(Activity activity, MenuInflater menuInflater) {
        return isInitialised(activity) && get(activity).isEnabled() ? StringXMenuInflater.wrap(activity, menuInflater) : menuInflater;
    }

    @Nullable
    public static AppCompatDelegate wrap(AppCompatActivity activity, AppCompatCallback callback) {
        return isInitialised(activity) && get(activity).isEnabled() ? LinguistAppCompatDelegate.wrap(activity, callback) : null;
    }

    private static boolean isInitialised(Context context) {
        Context applicationContext = context.getApplicationContext();
        return applicationContext instanceof Translatable &&
                ((Translatable) applicationContext).getStringX() != null;
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

    Locale getAppDefaultLocale() {
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
            options.getCache().put(text, translated);
        }
    }

    public Language getDeviceLanguage() {
        return Language.fromLocale(Locale.getDefault());
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
        return translator.translate(string);
    }

    @Override
    public View translate(View view) {
        return translator.translate(view);
    }

    @Override
    public Preference translate(Preference preference) {
        return translator.translate(preference);
    }

    @Override
    public void translate(Menu menu) {
        translator.translate(menu);
    }

    @Override
    public CharSequence translate(CharSequence text) {
        return translator.translate(text);
    }

    @Override
    public CharSequence[] translate(CharSequence[] textArray) {
        return translator.translate(textArray);
    }

    @Override
    public String[] translate(String[] textArray) {
        return translator.translate(textArray);
    }
}
