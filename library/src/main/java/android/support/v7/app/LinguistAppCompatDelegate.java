package android.support.v7.app;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.LayoutInflaterCompat;
import android.util.Log;
import android.view.LayoutInflater;

import mobi.klimaszewski.linguist.LinguistFactory;
import mobi.klimaszewski.linguist.LinguistViewTranslator;

public class LinguistAppCompatDelegate extends LinguistDelegateWrapper {


    public static AppCompatDelegate wrap(Activity activity, AppCompatCallback callback, LinguistViewTranslator linguistViewTranslator) {
        return new LinguistAppCompatDelegate(AppCompatDelegate.create(activity, callback), activity, linguistViewTranslator);
    }

    public static AppCompatDelegate wrap(Dialog dialog, AppCompatCallback callback, LinguistViewTranslator linguistViewTranslator) {
        return new LinguistAppCompatDelegate(AppCompatDelegate.create(dialog, callback), dialog.getContext(), linguistViewTranslator);
    }

    private AppCompatDelegateImplV9 delegate;
    private Context context;
    private LinguistViewTranslator viewTranslator;

    public LinguistAppCompatDelegate(AppCompatDelegate delegate, Context context, LinguistViewTranslator linguistViewTranslator) {
        super(delegate);
        this.delegate = (AppCompatDelegateImplV9) delegate;
        this.context = context;
        this.viewTranslator = linguistViewTranslator;
    }

    @Override
    public void installViewFactory() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (layoutInflater.getFactory() == null) {
            LayoutInflaterCompat.setFactory(layoutInflater, new LinguistFactory(delegate, viewTranslator));
        } else {
            if (!(LayoutInflaterCompat.getFactory(layoutInflater)
                    instanceof AppCompatDelegateImplV9)) {
                Log.i(TAG, "The Activity's LayoutInflater already has a Factory installed"
                        + " so we can not install AppCompat's");
            }
        }

    }
}
