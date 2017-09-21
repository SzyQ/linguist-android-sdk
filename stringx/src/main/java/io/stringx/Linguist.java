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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class Linguist {
    private Context context;
    private Options options;
    private boolean isTranslationChecked;

    public Linguist(Context context, Options options) {
        this.context = context;
        this.options = options;
    }

    @Nullable
    public static Linguist get(@NonNull Context context) {
        Linguist linguist = null;
        if (isInitialised(context)) {
            linguist = ((Translatable) context.getApplicationContext()).getLinguist();
        }
        if (linguist == null) {
            LL.w("Application needs to extend Translatable interface and provide Linguist instance.");
            return null;
        }
        return linguist;
    }

    public static Context wrap(Context base) {
        return isInitialised(base) && !get(base).isOptOut() ? LinguistContextWrapper.wrap(base) : base;
    }

    public static MenuInflater wrap(Activity activity, MenuInflater menuInflater) {
        return isInitialised(activity) && !get(activity).isOptOut() ? LinguistMenuInflater.wrap(activity, menuInflater) : menuInflater;
    }

    @Nullable
    public static AppCompatDelegate wrap(AppCompatActivity activity, AppCompatCallback callback) {
        return isInitialised(activity) && !get(activity).isOptOut() ? LinguistAppCompatDelegate.wrap(activity, callback) : null;
    }

    private static boolean isInitialised(Context context) {
        Context applicationContext = context.getApplicationContext();
        return applicationContext instanceof Translatable &&
                ((Translatable) applicationContext).getLinguist() != null;
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

    public String translate(String string) {
        String cachedText = options.getCache().get(string);
        return cachedText == null ? string : cachedText;
    }

    public View translate(View view) {
        if(isOptOut()){
            return view;
        }
        if (view != null) {
            if ((view instanceof Toolbar)) {
                Toolbar toolbar = (Toolbar) view;
                toolbar.setTitle(translate(toolbar.getTitle().toString()));
                return view;
            } else if ((view instanceof EditText)) {
                EditText toolbar = (EditText) view;
                CharSequence hint = toolbar.getHint();
                if (hint != null) {
                    toolbar.setHint(translate(hint.toString()));
                }
                return view;
            } else if (view instanceof TextView) {
                ((TextView) view).setText(translate(((TextView) view).getText().toString()));
            } else {
                LL.v("Unsupported view: " + view.getClass().getName());
            }
        }
        return view;
    }

    public Preference translate(Preference preference) {
        if (preference != null) {
            preference.setTitle(translate(preference.getTitle()));
            preference.setSummary(translate(preference.getSummary()));
        }
        return preference;
    }

    public void translate(Menu menu) {
        if(isOptOut()){
            return;
        }
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            item.setTitle(translate(item.getTitle()));
            item.setTitleCondensed(translate(item.getTitleCondensed()));
            SubMenu subMenu = item.getSubMenu();
            if (subMenu != null) {
                translate(subMenu);
            }
        }
    }

    public CharSequence translate(CharSequence text) {
        if(isOptOut()){
            return text;
        }
        return text != null ? new StringBuffer(translate(text.toString())) : null;
    }

    public CharSequence[] translate(CharSequence[] textArray) {
        if(isOptOut()){
            return textArray;
        }
        CharSequence[] charSequences = new CharSequence[textArray.length];
        for (int i = 0; i < textArray.length; i++) {
            charSequences[i] = translate(textArray[i]);
        }
        return charSequences;
    }

    public String[] translate(String[] textArray) {
        if(isOptOut()){
            return textArray;
        }
        String[] charSequences = new String[textArray.length];
        for (int i = 0; i < textArray.length; i++) {
            charSequences[i] = translate(textArray[i]);
        }
        return charSequences;
    }

    public void onResume(Activity activity) {
        if (isTranslationChecked) {
            return;
        }
        Language deviceLanguage = getDeviceLanguage();
        if (options.getCache().isTranslationEnabled(deviceLanguage.getCode())) {
            return;
        }
        if (options.getSupportedLanguages().contains(deviceLanguage)) {
            return;
        }
        if (options.getCache().isNeverTranslateEnabled(deviceLanguage.getCode())) {
            return;
        }
        isTranslationChecked = true;
        Intent intent = new Intent(activity, LinguistOverlayActivity.class);
        activity.startActivityForResult(intent, LinguistOverlayActivity.REQUEST_CODE);
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

    Locale getAppLocale() {
        Language deviceLanguage = getDeviceLanguage();
        boolean isSupported = options.getSupportedLanguages().contains(deviceLanguage);
        if (isSupported) {
            return getDeviceDefaultLocale();
        } else {
            return getAppDefaultLocale();
        }
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
            }
        }
        return supportedResources;
    }

    void setNeverTranslate(boolean isEnabled) {
        options.getCache().setNeverTranslateEnabled(Locale.getDefault().getCountry(), isEnabled);
    }

    void applyTranslation(Map<String, String> translation) {
        for (String text : translation.keySet()) {
            String translated = translation.get(text);
            options.getCache().put(text, translated);
        }
        options.getCache().setTranslationEnabled(getDeviceLanguage().getCode(), true);
    }

    private Language getDeviceLanguage() {
        return Language.fromLocale(Locale.getDefault());
    }

    private Language getDefaultLanguage() {
        return options.getDefaultLanguage();
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LinguistOverlayActivity.REQUEST_CODE) {
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
}
