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

    private Linguist viewTranslator;

    public LinguistLayoutInflater(LayoutInflater layoutInflater, Context context) {
        super(layoutInflater, context);
        this.viewTranslator = Linguist.from(context);
    }

    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return new LinguistLayoutInflater(this, newContext);
    }

    @Override
    protected View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
        for (String prefix : sClassPrefixList) {
            try {
                View view = createView(name, prefix, attrs);
                if (view != null) {
                    return viewTranslator.translate(view);
                }
            } catch (ClassNotFoundException e) {
                // In this case we want to let the base class take a crack
                // at it.
            }
        }

        return viewTranslator.translate(super.onCreateView(name, attrs));
    }


    @Override
    protected View onCreateView(View parent, String name, AttributeSet attrs) throws ClassNotFoundException {
        return viewTranslator.translate(super.onCreateView(parent, name, attrs));
    }
}
