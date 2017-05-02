package mobi.klimaszewski.linguist;


import android.content.Context;
import android.support.v4.view.LayoutInflaterFactory;
import android.util.AttributeSet;
import android.view.View;

public class LinguistFactory implements LayoutInflaterFactory {

    private LayoutInflaterFactory factory;
    private LinguistViewTranslator linguist;

    public LinguistFactory(LayoutInflaterFactory factory, LinguistViewTranslator viewTranslator) {
        this.factory = factory;
        this.linguist = viewTranslator;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return linguist.translateView(factory.onCreateView(parent, name, context, attrs));
    }
}
