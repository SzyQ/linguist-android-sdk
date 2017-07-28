package mobi.klimaszewski.linguist;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import mobi.klimaszewski.linguist.Linguist;

public final class LinguistFactory implements LayoutInflater.Factory2 {

    private LayoutInflater.Factory2 factory;
    private Linguist linguist;

    public LinguistFactory(LayoutInflater.Factory2 factory, Linguist linguist) {
        this.factory = factory;
        this.linguist = linguist;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return linguist.translate(factory.onCreateView(parent, name, context, attrs));
    }

    @Override
    public View onCreateView(String s, Context context, AttributeSet attributeSet) {
        return linguist.translate(factory.onCreateView(s, context, attributeSet));
    }
}
