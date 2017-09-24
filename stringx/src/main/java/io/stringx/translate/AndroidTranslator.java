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
import io.stringx.LL;
import io.stringx.StringX;

public class AndroidTranslator implements Translator {

    private StringX cache;
    private Cache cache2;

    public AndroidTranslator(StringX cache, Cache cache2) {
        this.cache = cache;
        this.cache2 = cache2;
    }

    public String translate(String string) {
        String cachedText = cache2.get(string);
        return cachedText == null ? string : cachedText;
    }

    public View translate(View view) {
        if (cache.isEnabled()) {
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
        if (cache.isEnabled()) {
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
        if (cache.isEnabled()) {
            return text;
        }
        return text != null ? new StringBuffer(translate(text.toString())) : null;
    }

    public CharSequence[] translate(CharSequence[] textArray) {
        if (cache.isEnabled()) {
            return textArray;
        }
        CharSequence[] charSequences = new CharSequence[textArray.length];
        for (int i = 0; i < textArray.length; i++) {
            charSequences[i] = translate(textArray[i]);
        }
        return charSequences;
    }

    public String[] translate(String[] textArray) {
        if (cache.isEnabled()) {
            return textArray;
        }
        String[] charSequences = new String[textArray.length];
        for (int i = 0; i < textArray.length; i++) {
            charSequences[i] = translate(textArray[i]);
        }
        return charSequences;
    }
}
