package io.stringx;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

public final class StringxFactory implements LayoutInflater.Factory2 {

    private LayoutInflater.Factory2 factory;
    private Stringx stringx;

    public StringxFactory(LayoutInflater.Factory2 factory, Stringx stringx) {
        this.factory = factory;
        this.stringx = stringx;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return stringx.translate(factory.onCreateView(parent, name, context, attrs));
    }

    @Override
    public View onCreateView(String s, Context context, AttributeSet attributeSet) {
        return stringx.translate(factory.onCreateView(s, context, attributeSet));
    }
}
