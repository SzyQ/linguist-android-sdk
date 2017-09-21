package io.stringx;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.support.annotation.NonNull;

final class StringxContextWrapper extends ContextWrapper {

    private StringxResources resources;
    private Stringx stringx;

    public StringxContextWrapper(Context base, @NonNull Stringx stringx) {
        super(base);
        this.stringx = stringx;
    }

    public static Context wrap(Context context) {
        return new StringxContextWrapper(context, Stringx.get(context));
    }

    @Override
    public Resources getResources() {
        if (resources == null) {
            resources = new StringxResources(super.getResources(), stringx);
        }
        return resources;
    }

//    @Override
//    public Object getSystemService(String name) {
//        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
//            if (inflater == null) {
//                inflater = new StringxLayoutInflater((LayoutInflater) super.getSystemService(name), getBaseContext(), viewTranslator).cloneInContext(this);
//            }
//            return inflater;
//        }
//        return super.getSystemService(name);
//    }


}
