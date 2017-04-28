package mobi.klimaszewski.linguist;


import android.content.Context;
import android.support.v4.view.LayoutInflaterFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class LinguistFactory implements LayoutInflaterFactory {

    private LayoutInflaterFactory factory;

    public LinguistFactory(LayoutInflaterFactory factory) {
        this.factory = factory;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return translateView(factory.onCreateView(parent, name, context, attrs));
    }

    private View translateView(View view) {
        if (view != null && view instanceof TextView) {
            ((TextView) view).setText("Dupa");
        }
        return view;
    }
}
