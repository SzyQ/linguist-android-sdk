package io.stringx.translate;

import android.preference.Preference;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import io.stringx.Cache;

public final class AndroidTranslator implements Translator {

    private Cache cache;
    private ViewTranslator viewTranslator;

    public AndroidTranslator(Cache cache) {
        this.cache = cache;
        viewTranslator = new DefaultViewTranslator();
    }

    public String translate(String string) {
        String cachedText = cache.get(string);
        return cachedText == null ? string : cachedText;
    }

    public View translate(View view) {
        return view != null ? viewTranslator.translate(cache, view) : null;
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

    public void setViewTranslator(ViewTranslator viewTranslator) {
        this.viewTranslator = viewTranslator;
    }

    public interface ViewTranslator {

        View translate(Cache cache, View view);
    }

    public static class DefaultViewTranslator implements ViewTranslator {

        public String getTranslation(Cache cache, String string) {
            String cachedText = cache.get(string);
            return cachedText == null ? string : cachedText;
        }

        @Override
        public View translate(Cache cache, View view) {
            if ((view instanceof Toolbar)) {
                Toolbar toolbar = (Toolbar) view;
                toolbar.setTitle(getTranslation(cache, toolbar.getTitle().toString()));
                return view;
            } else if ((view instanceof EditText)) {
                EditText toolbar = (EditText) view;
                CharSequence hint = toolbar.getHint();
                if (hint != null) {
                    toolbar.setHint(getTranslation(cache, hint.toString()));
                }
                return view;
            } else if (view instanceof TextView) {
                ((TextView) view).setText(getTranslation(cache, ((TextView) view).getText().toString()));
            }
            return view;
        }
    }

}
