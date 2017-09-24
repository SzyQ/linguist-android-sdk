package io.stringx;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.support.annotation.NonNull;

final class StringXContextWrapper extends ContextWrapper {

    private StringXResources resources;
    private StringX stringX;

    public StringXContextWrapper(Context base, @NonNull StringX stringX) {
        super(base);
        this.stringX = stringX;
    }

    public static Context wrap(Context context) {
        return new StringXContextWrapper(context, StringX.get(context));
    }

    @Override
    public Resources getResources() {
        if (resources == null) {
            resources = new StringXResources(super.getResources(), stringX);
        }
        return resources;
    }

//    @Override
//    public Object getSystemService(String name) {
//        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
//            if (inflater == null) {
//                inflater = new StringXLayoutInflater((LayoutInflater) super.getSystemService(name), getBaseContext(), viewTranslator).cloneInContext(this);
//            }
//            return inflater;
//        }
//        return super.getSystemService(name);
//    }


}
