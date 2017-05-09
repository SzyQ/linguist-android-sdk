package mobi.klimaszewski.linguist;


import android.content.Context;
import android.support.v4.view.LayoutInflaterFactory;
import android.util.AttributeSet;
import android.view.View;

public class LinguistFactory implements LayoutInflaterFactory {

    private LayoutInflaterFactory factory;
    private Linguist linguist;

    public LinguistFactory(LayoutInflaterFactory factory, Linguist linguist) {
        this.factory = factory;
        this.linguist = linguist;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return linguist.translate(factory.onCreateView(parent, name, context, attrs));
    }
}
