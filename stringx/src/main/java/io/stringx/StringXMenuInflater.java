package io.stringx;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.MenuRes;
import android.view.Menu;
import android.view.MenuInflater;

class StringXMenuInflater extends MenuInflater {

    private final StringX stringX;
    private MenuInflater inflater;

    private StringXMenuInflater(Context context, MenuInflater inflater) {
        super(context);
        this.inflater = inflater;
        stringX = StringX.get(context);
    }

    public static MenuInflater wrap(Activity activity, MenuInflater menuInflater) {
        return new StringXMenuInflater(activity, menuInflater);
    }

    @Override
    public void inflate(@MenuRes int menuRes, Menu menu) {
        inflater.inflate(menuRes, menu);
        stringX.translate(menu);
    }
}
