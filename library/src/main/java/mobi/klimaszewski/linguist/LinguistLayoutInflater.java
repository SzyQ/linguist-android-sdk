package mobi.klimaszewski.linguist;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

public class LinguistLayoutInflater extends LayoutInflater {

    private static final String[] sClassPrefixList = {
            "android.widget.",
            "android.webkit.",
            "android.app."
    };

    private LinguistViewTranslator viewTranslator;

    public LinguistLayoutInflater(LayoutInflater layoutInflater, Context context, LinguistViewTranslator viewTranslator) {
        super(layoutInflater, context);
        this.viewTranslator = viewTranslator;
    }

    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return new LinguistLayoutInflater(this, newContext, viewTranslator);
    }

    @Override
    protected View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
        for (String prefix : sClassPrefixList) {
            try {
                View view = createView(name, prefix, attrs);
                if (view != null) {
                    return viewTranslator.translateView(view);
                }
            } catch (ClassNotFoundException e) {
                // In this case we want to let the base class take a crack
                // at it.
            }
        }

        return viewTranslator.translateView(super.onCreateView(name, attrs));
    }


    @Override
    protected View onCreateView(View parent, String name, AttributeSet attrs) throws ClassNotFoundException {
        return viewTranslator.translateView(super.onCreateView(parent, name, attrs));
    }
}
