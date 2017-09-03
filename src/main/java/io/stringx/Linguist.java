package io.stringx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class Linguist {
    private Context context;
    private Cache cache;
    private List<Class> stringClasses;
    private List<Class> excludedClasses;
    private String defaultLanguage;
    private List<String> supportedLanguages;
    private boolean isTranslationChecked;

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
        return isInitialised(base) ? LinguistContextWrapper.wrap(base) : base;
    }

    public static MenuInflater wrap(Activity activity, MenuInflater menuInflater) {
        return isInitialised(activity) ? LinguistMenuInflater.wrap(activity, menuInflater) : menuInflater;
    }

    @Nullable
    public static AppCompatDelegate wrap(AppCompatActivity activity, AppCompatCallback callback) {
        return isInitialised(activity) ? LinguistAppCompatDelegate.wrap(activity, callback) : null;
    }

    private static boolean isInitialised(Context context) {
        Context applicationContext = context.getApplicationContext();
        return applicationContext instanceof Translatable && ((Translatable) applicationContext).getLinguist() != null;
    }


    public void refresh() {
        isTranslationChecked = false;
    }

    public String translate(String string) {
        String cachedText = cache.get(string);
        return cachedText == null ? string : cachedText;
    }

    public View translate(View view) {
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
        return text != null ? new StringBuffer(translate(text.toString())) : null;
    }

    public CharSequence[] translate(CharSequence[] textArray) {
        CharSequence[] charSequences = new CharSequence[textArray.length];
        for (int i = 0; i < textArray.length; i++) {
            charSequences[i] = translate(textArray[i]);
        }
        return charSequences;
    }

    public String[] translate(String[] textArray) {
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
        String countryCode = Locale.getDefault().getLanguage();
        if (cache.isTranslationEnabled(countryCode)) {
            return;
        }
        if (supportedLanguages.contains(countryCode)) {
            return;
        }
        if (cache.isNeverTranslateEnabled(countryCode)) {
            return;
        }
        isTranslationChecked = true;
        Intent intent = new Intent(activity, LinguistOverlayActivity.class);
        activity.startActivityForResult(intent, LinguistOverlayActivity.REQUEST_CODE);
    }

    Locale getAppDefaultLocale() {
        Locale[] availableLocales = Locale.getAvailableLocales();
        Locale defaultLocale = null;
        String defaultLanguageCode = getDefaultLanguageCode();
        for (Locale locale : availableLocales) {
            if (locale.getLanguage().equals(defaultLanguageCode)) {
                defaultLocale = locale;
                break;
            }
        }
        return defaultLocale;
    }

    Locale getAppLocale() {
        String deviceLanguageCode = getDeviceLanguageCode();
        boolean isSupported = supportedLanguages.contains(deviceLanguageCode);
        if (isSupported) {
            return getDeviceDefaultLocale();
        } else {
            return getAppDefaultLocale();
        }
    }

    Locale getDeviceDefaultLocale() {
        return Locale.getDefault();
    }

    List<StringResource> fetch(Locale locale) {
        List<Pair<Integer,String>> supportedResources = getResourcesIds();
        return Utils.getAppStrings(context, supportedResources, locale);
    }

    @NonNull
    private List<Pair<Integer,String>> getResourcesIds() {
        List<Pair<Integer,String>> supportedResources = Utils.getAppStringResources(context, stringClasses);
        List<Pair<Integer,String>> excludedResources = Utils.getAppStringResources(context, excludedClasses);
        Iterator<Pair<Integer,String>> iterator = supportedResources.iterator();
        while (iterator.hasNext()) {
            Pair<Integer,String> resourceId = iterator.next();
            if (excludedResources.contains(resourceId)) {
                iterator.remove();
            }
        }
        return supportedResources;
    }

    void setNeverTranslate(boolean isEnabled) {
        cache.setNeverTranslateEnabled(Locale.getDefault().getCountry(), isEnabled);
    }

    void applyTranslation(Map<String, String> translation) {
        for (String text : translation.keySet()) {
            String translated = translation.get(text);
            cache.put(text, translated);
        }
        cache.setTranslationEnabled(getDeviceLanguageCode(), true);
    }

    private String getDeviceLanguageCode() {
        return Locale.getDefault().getLanguage();
    }

    private String getDefaultLanguageCode() {
        return defaultLanguage;
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LinguistOverlayActivity.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                return true;
            }
        }
        return false;
    }

    public static class Builder {
        private Context context;
        private String defaultLanguage;
        private List<String> supportedLanguages;
        private Cache cache;
        private List<Class> supportedStrings;
        private List<Class> excludedStrings;

        public Builder(Context context, String defaultLanguage, List<String> supportedLanguages) {
            this.context = context;
            this.defaultLanguage = defaultLanguage;
            this.supportedLanguages = supportedLanguages;
            supportedStrings = new ArrayList<>();
            excludedStrings = new ArrayList<>();
        }

        public Builder addStrings(Class clazz) {
            supportedStrings.add(clazz);
            return this;
        }

        public Builder excludeStrings(Class clazz) {
            excludedStrings.add(clazz);
            return this;
        }

        public Linguist build() {
            Linguist linguist = new Linguist();
            linguist.context = context;
            if (cache == null) {
                cache = new PreferencesCache(context);
            }
            linguist.cache = cache;
            linguist.stringClasses = supportedStrings;
            linguist.excludedClasses = excludedStrings;
            linguist.defaultLanguage = defaultLanguage;
            linguist.supportedLanguages = supportedLanguages;
            return linguist;
        }
    }
}
