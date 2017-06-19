package android.support.v7.app;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.LayoutInflaterCompat;
import android.util.Log;
import android.view.LayoutInflater;

import mobi.klimaszewski.linguist.Linguist;
import mobi.klimaszewski.linguist.LinguistFactory;

public class LinguistAppCompatDelegate extends LinguistDelegateWrapper {


    private AppCompatDelegateImplV9 delegate;
    private Context context;
    private Linguist linguist;
    public LinguistAppCompatDelegate(AppCompatDelegate delegate, Context context) {
        super(delegate);
        this.delegate = (AppCompatDelegateImplV9) delegate;
        this.context = context;
        this.linguist = Linguist.getInstance();
    }

    public static AppCompatDelegate wrap(Activity activity, AppCompatCallback callback) {
        return new LinguistAppCompatDelegate(AppCompatDelegate.create(activity, callback), activity);
    }

    public static AppCompatDelegate wrap(Dialog dialog, AppCompatCallback callback) {
        return new LinguistAppCompatDelegate(AppCompatDelegate.create(dialog, callback), dialog.getContext());
    }

    @Override
    public void installViewFactory() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (layoutInflater.getFactory() == null) {
            LayoutInflaterCompat.setFactory2(layoutInflater, new LinguistFactory(delegate, linguist));
        } else {
            if (!(layoutInflater.getFactory2()
                    instanceof AppCompatDelegateImplV9)) {
                Log.i(TAG, "The Activity's LayoutInflater already has a Factory installed"
                        + " so we can not install AppCompat's");
            }
        }

    }
}
