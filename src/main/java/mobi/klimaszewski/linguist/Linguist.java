package mobi.klimaszewski.linguist;

import android.content.Context;
import android.preference.Preference;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Linguist {
    private static final String LINGUIST = "Linguist ";

    public static Linguist from(Context context){
        Translatable linguistApp = (Translatable) context.getApplicationContext();
        return linguistApp.getLinguist();
    }

    public String translate(String string) {
        int length = string.length();
        int linguistLength = LINGUIST.length();
        int wordCount = length / linguistLength;
        String result = "";
        for (int i = 0; i < wordCount; i++) {
            result += LINGUIST;
        }
        result += LINGUIST.substring(0, length % linguistLength);
        return result;
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
                if(hint != null) {
                    toolbar.setHint(translate(hint.toString()));
                }
                return view;
            } else if (view instanceof TextView) {
                ((TextView) view).setText(translate(((TextView) view).getText().toString()));
            } else {
                LL.v("Unsupported view: "+view.getClass().getName());
            }
        }
        return view;
    }

    public Preference translate(Preference preference) {
        if(preference != null){
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
            if(subMenu != null){
                translate(subMenu);
            }
        }
    }

    public CharSequence translate(CharSequence text) {
        return text != null ? new StringBuffer(translate(text.toString())): text;
    }

    public CharSequence[] translate(CharSequence[] textArray) {
        CharSequence[] charSequences = new CharSequence[textArray.length];
        for(int i= 0; i < textArray.length; i++){
            charSequences[i]=translate(textArray[i]);
        }
        return charSequences;
    }

    public String[] translate(String[] textArray) {
        String[] charSequences = new String[textArray.length];
        for(int i= 0; i < textArray.length; i++){
            charSequences[i]=translate(textArray[i]);
        }
        return charSequences;
    }

    public static class Builder {

        public Builder(){
        }

        public Linguist build(){
            return new Linguist();
        }
    }
}
