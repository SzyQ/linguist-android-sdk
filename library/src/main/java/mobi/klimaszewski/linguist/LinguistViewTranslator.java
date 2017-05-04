package mobi.klimaszewski.linguist;


import android.preference.Preference;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LinguistViewTranslator {

    private Linguist linguist;

    public LinguistViewTranslator(Linguist linguist) {
        this.linguist = linguist;
    }

    public View translateView(View view) {
        if (view != null) {
            if ((view instanceof Toolbar)) {
                Toolbar toolbar = (Toolbar) view;
                toolbar.setTitle(linguist.translate(toolbar.getTitle().toString()));
                return view;
            } else if ((view instanceof EditText)) {
                EditText toolbar = (EditText) view;
                CharSequence hint = toolbar.getHint();
                if(hint != null) {
                    toolbar.setHint(linguist.translate(hint.toString()));
                }
                return view;
            } else if (view instanceof TextView) {
                ((TextView) view).setText(linguist.translate(((TextView) view).getText().toString()));
            } else {
                LL.v("Unsupported view: "+view.getClass().getName());
            }
        }
        return view;
    }

    public Preference translatePreference(Preference preference) {
        if(preference != null){
            preference.setTitle(linguist.translate(preference.getTitle()));
            preference.setSummary(linguist.translate(preference.getSummary()));
        }
        return preference;
    }
}
