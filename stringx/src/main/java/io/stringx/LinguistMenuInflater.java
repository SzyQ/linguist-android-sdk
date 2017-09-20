package io.stringx;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.MenuRes;
import android.view.Menu;
import android.view.MenuInflater;

class LinguistMenuInflater extends MenuInflater {

    private final Linguist linguist;
    private MenuInflater inflater;

    private LinguistMenuInflater(Context context, MenuInflater inflater) {
        super(context);
        this.inflater = inflater;
        linguist = Linguist.get(context);
    }

    public static MenuInflater wrap(Activity activity, MenuInflater menuInflater) {
        return new LinguistMenuInflater(activity, menuInflater);
    }

    @Override
    public void inflate(@MenuRes int menuRes, Menu menu) {
        inflater.inflate(menuRes, menu);
        linguist.translate(menu);
    }
}
