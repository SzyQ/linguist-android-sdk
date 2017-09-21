package io.stringx;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.MenuRes;
import android.view.Menu;
import android.view.MenuInflater;

class StringxMenuInflater extends MenuInflater {

    private final Stringx stringx;
    private MenuInflater inflater;

    private StringxMenuInflater(Context context, MenuInflater inflater) {
        super(context);
        this.inflater = inflater;
        stringx = Stringx.get(context);
    }

    public static MenuInflater wrap(Activity activity, MenuInflater menuInflater) {
        return new StringxMenuInflater(activity, menuInflater);
    }

    @Override
    public void inflate(@MenuRes int menuRes, Menu menu) {
        inflater.inflate(menuRes, menu);
        stringx.translate(menu);
    }
}
