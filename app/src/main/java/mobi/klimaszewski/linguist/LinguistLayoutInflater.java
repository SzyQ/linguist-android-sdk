package mobi.klimaszewski.linguist;


import android.content.Context;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


/**
 * https://github.com/chrisjenx/Calligraphy/blob/master/calligraphy/src/main/java/uk/co/chrisjenx/calligraphy/CalligraphyLayoutInflater.java
 */
public class LinguistLayoutInflater extends LayoutInflater {


    public static void installFactory(LayoutInflater inflater, LayoutInflaterFactory factory) {
        LayoutInflaterCompat.setFactory(inflater, new LinguistFactory(factory));
    }

    private final LayoutInflater layoutInflater;

    protected LinguistLayoutInflater(Context context, Context originalContext) {
        super(context);
        layoutInflater = LayoutInflater.from(originalContext);
    }

    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return layoutInflater.cloneInContext(newContext);
    }

    @Override
    protected View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
        View view = super.onCreateView(name, attrs);
        return translateView(view);
    }

    @Override
    protected View onCreateView(View parent, String name, AttributeSet attrs) throws ClassNotFoundException {
        View view = super.onCreateView(parent, name, attrs);
        return translateView(view);
    }

    private View translateView(View view) {
        if (view instanceof TextView) {
            ((TextView) view).setText("Dupa");
        }
        return view;
    }
}
