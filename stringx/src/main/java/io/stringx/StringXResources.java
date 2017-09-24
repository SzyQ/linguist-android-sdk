package io.stringx;

import android.content.res.Resources;
import android.support.annotation.NonNull;

final class StringXResources extends Resources {
    private final Resources resources;
    private StringX stringX;


    StringXResources(Resources resources, StringX stringX) {
        super(resources.getAssets(), resources.getDisplayMetrics(), resources.getConfiguration());
        this.resources = resources;
        this.stringX = stringX;
    }

    @NonNull
    @Override
    public CharSequence getText(int id) throws NotFoundException {
        return stringX.translate(resources.getText(id));
    }

    @NonNull
    @Override
    public CharSequence getQuantityText(int id, int quantity) throws NotFoundException {
        return stringX.translate(resources.getQuantityText(id, quantity));
    }

    @NonNull
    @Override
    public String getString(int id) throws NotFoundException {
        return stringX.translate(resources.getString(id));
    }

    @NonNull
    @Override
    public String getString(int id, Object... formatArgs) throws NotFoundException {
        return String.format(stringX.translate(resources.getString(id)), formatArgs);
    }

    @NonNull
    @Override
    public String getQuantityString(int id, int quantity, Object... formatArgs)
            throws NotFoundException {
        return String.format(stringX.translate(resources.getQuantityString(id, quantity)), formatArgs);
    }

    @NonNull
    @Override
    public String getQuantityString(int id, int quantity) throws NotFoundException {
        return stringX.translate(resources.getQuantityString(id, quantity));
    }

    @Override
    public CharSequence getText(int id, CharSequence def) {
        return stringX.translate(resources.getText(id, def));
    }

    @NonNull
    @Override
    public CharSequence[] getTextArray(int id) throws NotFoundException {
        return stringX.translate(resources.getTextArray(id));
    }

    @NonNull
    @Override
    public String[] getStringArray(int id) throws NotFoundException {
        return stringX.translate(resources.getStringArray(id));
    }
}
