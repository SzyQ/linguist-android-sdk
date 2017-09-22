package io.stringx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import io.stringx.translate.AndroidTranslator;
import io.stringx.translate.Translator;

import static android.app.Activity.RESULT_OK;

public class Stringx implements Translator {
    private Context context;
    private Options options;
    private boolean isTranslationChecked;
    private Translator translator;

    public Stringx(Context context, Options options) {
        this.context = context;
        this.options = options;
        this.translator = new AndroidTranslator(options.getCache());
    }

    @Nullable
    public static Stringx get(@NonNull Context context) {
        Stringx stringx = null;
        if (isInitialised(context)) {
            stringx = ((Translatable) context.getApplicationContext()).getStringx();
        }
        if (stringx == null) {
            LL.w("Application needs to extend Translatable interface and provide Stringx instance.");
            return null;
        }
        return stringx;
    }

    public static Context wrap(Context base) {
        return isInitialised(base) && !get(base).isOptOut() ? StringxContextWrapper.wrap(base) : base;
    }

    public static MenuInflater wrap(Activity activity, MenuInflater menuInflater) {
        return isInitialised(activity) && !get(activity).isOptOut() ? StringxMenuInflater.wrap(activity, menuInflater) : menuInflater;
    }

    @Nullable
    public static AppCompatDelegate wrap(AppCompatActivity activity, AppCompatCallback callback) {
        return isInitialised(activity) && !get(activity).isOptOut() ? LinguistAppCompatDelegate.wrap(activity, callback) : null;
    }

    private static boolean isInitialised(Context context) {
        Context applicationContext = context.getApplicationContext();
        return applicationContext instanceof Translatable &&
                ((Translatable) applicationContext).getStringx() != null;
    }

    public boolean isOptOut() {
        return options.getCache().isOptOut();
    }

    public void setOptOut(boolean isOptOut) {
        options.getCache().setOptOut(isOptOut);
    }

    public void refresh() {
        isTranslationChecked = false;
    }

    public Translator getTranslator() {
        return translator;
    }

    public void setTranslator(Translator translator) {
        this.translator = translator;
    }

    public void onResume(Activity activity) {
        if (isTranslationChecked) {
            return;
        }
        Language deviceLanguage = getDeviceLanguage();
        if (options.getCache().isEnabled(deviceLanguage)) {
            return;
        }
        if (options.getCache().isOptOut()) {
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
        Locale[] availableLocales = Locale.getAvailableLocales();
        Locale defaultLocale = null;
        Language defaultLanguage = getDefaultLanguage();
        for (Locale locale : availableLocales) {
            if (locale.getLanguage().equals(defaultLanguage.getCode())) {
                defaultLocale = locale;
                break;
            }
        }
        return defaultLocale;
    }

    Locale getDeviceDefaultLocale() {
        return Locale.getDefault();
    }

    void fetch(ConfigCallback callback) throws RemoteException {
        List<Pair<Integer, String>> supportedResources = getResourcesIds();
        Utils.getAppStrings(context, supportedResources, callback);
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
