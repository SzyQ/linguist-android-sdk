package mobi.klimaszewski.linguist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.preference.Preference;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Linguist {
    private static Linguist instance;
    private static boolean isInitialised;
    private Context context;
    private TranslationsFactory translationFactory;
    private Cache cache;
    private List<Class> stringClasses;
    private List<Class> excludedClasses;
    private String defaultLanguage;
    private List<String> supportedLanguages;
    private boolean isTranslationChecked;

    public synchronized static Linguist getInstance() {
        if (instance == null) {
            instance = new Linguist();
        }
        return instance;
    }

    public void refresh() {
        isTranslationChecked = false;
    }

    public static class Builder {
        private Context context;
        private String defaultLanguage;
        private List<String> supportedLanguages;
        private TranslationsFactory factory;
        private Cache cache;
        private List<Class> supportedStrings;
        private List<Class> excludedStrings;

        public Builder(Context context, String defaultLanguage, List<String> supportedLanguages){
            this.context = context;
            this.defaultLanguage = defaultLanguage;
            this.supportedLanguages = supportedLanguages;
            supportedStrings = new ArrayList<>();
            excludedStrings = new ArrayList<>();
        }

        public Builder addStrings(Class clazz){
            supportedStrings.add(clazz);
            return this;
        }

        public Builder excludeStrings(Class clazz){
            excludedStrings.add(clazz);
            return this;
        }

        public Linguist build(){
            getInstance().context = context;
            if(factory == null){
                factory = new ServiceTranslationFactory(context);
            }
            getInstance().translationFactory = factory;
            if(cache == null){
                cache = new PreferencesCache(context);
            }
            getInstance().cache = cache;
            getInstance().stringClasses = supportedStrings;
            getInstance().excludedClasses = excludedStrings;
            getInstance().defaultLanguage = defaultLanguage;
            getInstance().supportedLanguages = supportedLanguages;
            isInitialised = true;
            return getInstance();
        }
    }

    public static Locale getAppDefaultLocale() {
        Locale[] availableLocales = Locale.getAvailableLocales();
        Locale defaultLocale = null;
        String defaultLanguageCode = Linguist.getInstance().getDefaultLanguageCode();
        for (Locale locale : availableLocales) {
            if (locale.getLanguage().equals(defaultLanguageCode)) {
                defaultLocale = locale;
                break;
            }
        }
        return defaultLocale;
    }

    public static Locale getDeviceDefaultLocale() {
        return Locale.getDefault();
    }

    List<String> fetch() {
        List<Integer> supportedResources = getResourcesIds();
        return Utils.getAppStrings(context, supportedResources);
    }

    @NonNull
    private List<Integer> getResourcesIds() {
        List<Integer> supportedResources = Utils.getAppStringResources(context, stringClasses);
        List<Integer> excludedResources = Utils.getAppStringResources(context, excludedClasses);
        Iterator<Integer> iterator = supportedResources.iterator();
        while (iterator.hasNext()){
            Integer resourceId = iterator.next();
            if(excludedResources.contains(resourceId)){
                iterator.remove();
            }
        }
        return supportedResources;
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
        if (!isInitialised) {
            return;
        }
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

    void setNeverTranslate(boolean isEnabled) {
        cache.setNeverTranslateEnabled(Locale.getDefault().getCountry(), isEnabled);
    }

    void replyToService() {
        if (!isInitialised) {
            return;
        }
        List<String> appStrings = Utils.getAppStrings(context, getResourcesIds());
        int charactersCount = 0;
        for (String string : appStrings) {
            charactersCount += string.length();
        }

        PackageManager packageManager = context.getPackageManager();
        CharSequence appName = context.getApplicationInfo().loadLabel(packageManager);
        LL.d("Replying(" + charactersCount + ")");
        translationFactory.hello(context.getPackageName(), charactersCount, appName.toString());
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
}
