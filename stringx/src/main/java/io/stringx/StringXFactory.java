package io.stringx;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

public final class StringXFactory implements LayoutInflater.Factory2 {

    private LayoutInflater.Factory2 factory;
    private StringX stringX;

    public StringXFactory(LayoutInflater.Factory2 factory, StringX stringX) {
        this.factory = factory;
        this.stringX = stringX;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return stringX.translate(factory.onCreateView(parent, name, context, attrs));
    }

    @Override
    public View onCreateView(String s, Context context, AttributeSet attributeSet) {
        return stringX.translate(factory.onCreateView(s, context, attributeSet));
    }
}
