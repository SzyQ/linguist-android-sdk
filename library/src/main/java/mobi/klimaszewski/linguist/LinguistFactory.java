package mobi.klimaszewski.linguist;


import android.content.Context;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class LinguistFactory implements LayoutInflaterFactory {

    private LayoutInflaterFactory factory;
    private Linguist linguist;

    public LinguistFactory(LayoutInflaterFactory factory, Linguist linguist) {
        this.factory = factory;
        this.linguist = linguist;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return translateView(factory.onCreateView(parent, name, context, attrs));
    }

    private View translateView(View view) {
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
