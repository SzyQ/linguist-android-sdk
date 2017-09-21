package io.stringx;

import android.content.res.Resources;
import android.support.annotation.NonNull;

final class StringxResources extends Resources {
    private final Resources resources;
    private Stringx stringx;


    StringxResources(Resources resources, Stringx stringx) {
        super(resources.getAssets(), resources.getDisplayMetrics(), resources.getConfiguration());
        this.resources = resources;
        this.stringx = stringx;
    }

    @NonNull
    @Override
    public CharSequence getText(int id) throws NotFoundException {
        return stringx.translate(resources.getText(id));
    }

    @NonNull
    @Override
    public CharSequence getQuantityText(int id, int quantity) throws NotFoundException {
        return stringx.translate(resources.getQuantityText(id, quantity));
    }

    @NonNull
    @Override
    public String getString(int id) throws NotFoundException {
        return stringx.translate(resources.getString(id));
    }

    @NonNull
    @Override
    public String getString(int id, Object... formatArgs) throws NotFoundException {
        return String.format(stringx.translate(resources.getString(id)), formatArgs);
    }

    @NonNull
    @Override
    public String getQuantityString(int id, int quantity, Object... formatArgs)
            throws NotFoundException {
        return String.format(stringx.translate(resources.getQuantityString(id, quantity)),formatArgs);
    }

    @NonNull
    @Override
    public String getQuantityString(int id, int quantity) throws NotFoundException {
        return stringx.translate(resources.getQuantityString(id, quantity));
    }

    @Override
    public CharSequence getText(int id, CharSequence def) {
        return stringx.translate(resources.getText(id, def));
    }

    @NonNull
    @Override
    public CharSequence[] getTextArray(int id) throws NotFoundException {
        return stringx.translate(resources.getTextArray(id));
    }

    @NonNull
    @Override
    public String[] getStringArray(int id) throws NotFoundException {
        return stringx.translate(resources.getStringArray(id));
    }
}
