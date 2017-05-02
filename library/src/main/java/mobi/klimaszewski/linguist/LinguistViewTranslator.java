package mobi.klimaszewski.linguist;


import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class LinguistViewTranslator {

    private Linguist linguist;

    public LinguistViewTranslator(Linguist linguist) {
        this.linguist = linguist;
    }

    public View translateView(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setText(linguist.translate(((TextView) view).getText().toString()));
            } else if ((view instanceof Toolbar)) {
                Toolbar toolbar = (Toolbar) view;
                toolbar.setTitle(linguist.translate(toolbar.getTitle().toString()));
                return view;
            }
        }
        return view;
    }
}
