package mobi.klimaszewski.linguist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.preference.Preference;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Linguist {
    private final Context context;
    private TranslationsFactory translationFactory;
    private Cache cache;
    private Class stringClass;
    private String defaultLanguage;
    private List<String> supportedLanguages;
    private boolean isTranslationChecked;

    public static Linguist from(Context context) {
        Translatable linguistApp = (Translatable) context.getApplicationContext();
        return linguistApp.getLinguist();
    }

    public Linguist(Context context, TranslationsFactory factory, Cache cache, Class stringClass, String defaultLanguage, List<String> supportedLanguages) {
        this.context = context;
        translationFactory = factory;
        this.cache = cache;
        this.stringClass = stringClass;
        this.defaultLanguage = defaultLanguage;
        this.supportedLanguages = supportedLanguages;
    }

    public void fetch() {
//        Set<Integer> stringResources = Utils.getAppStringResources(context, stringClass);
//        Set<String> appStrings = Utils.getAppStrings(context, stringResources);
//        translationFactory.translate(new ArrayList<>(appStrings), defaultLanguage, Locale.getDefault().getCountry());
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
        return text != null ? new StringBuffer(translate(text.toString())) : text;
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
        String countryCode = Locale.getDefault().getCountry();
        if (supportedLanguages.contains(countryCode)) {
            return;
        }
        if (cache.isNeverTranslateEnabled(countryCode)) {
            return;
        }
        if (isTranslationChecked) {
            return;
        }
        isTranslationChecked = true;
        Intent intent = new Intent(activity, LinguistOverlayActivity.class);
        activity.startActivityForResult(intent, LinguistOverlayActivity.REQUEST_CODE);
    }

    public void setNeverTranslate(boolean isEnabled) {
        cache.setNeverTranslateEnabled(Locale.getDefault().getCountry(), isEnabled);
    }

    public void replyToService() {
        List<String> appStrings = Utils.getAppStrings(context, Utils.getAppStringResources(context, stringClass));
        int charactersCount = 0;
        for (String string : appStrings) {
            charactersCount += string.length();
        }

        try {
            PackageManager packageManager = context.getPackageManager();
            Drawable icon = packageManager.getApplicationIcon(context.getPackageName());
            CharSequence appName = context.getApplicationInfo().loadLabel(packageManager);
            translationFactory.reply(context.getPackageName(), charactersCount, appName.toString());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
