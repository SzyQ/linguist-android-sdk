package io.stringx;

import android.content.res.Resources;
import android.support.annotation.NonNull;

final class LinguistResources extends Resources {
    private final Resources resources;
    private Linguist linguist;


    LinguistResources(Resources resources, Linguist linguist) {
        super(resources.getAssets(), resources.getDisplayMetrics(), resources.getConfiguration());
        this.resources = resources;
        this.linguist = linguist;
    }

    @NonNull
    @Override
    public CharSequence getText(int id) throws NotFoundException {
        return linguist.translate(resources.getText(id));
    }

    @NonNull
    @Override
    public CharSequence getQuantityText(int id, int quantity) throws NotFoundException {
        return linguist.translate(resources.getQuantityText(id, quantity));
    }

    @NonNull
    @Override
    public String getString(int id) throws NotFoundException {
        return linguist.translate(resources.getString(id));
    }

    @NonNull
    @Override
    public String getString(int id, Object... formatArgs) throws NotFoundException {
        return String.format(linguist.translate(resources.getString(id)), formatArgs);
    }

    @NonNull
    @Override
    public String getQuantityString(int id, int quantity, Object... formatArgs)
            throws NotFoundException {
        return String.format(linguist.translate(resources.getQuantityString(id, quantity)),formatArgs);
    }

    @NonNull
    @Override
    public String getQuantityString(int id, int quantity) throws NotFoundException {
        return linguist.translate(resources.getQuantityString(id, quantity));
    }

    @Override
    public CharSequence getText(int id, CharSequence def) {
        return linguist.translate(resources.getText(id, def));
    }

    @NonNull
    @Override
    public CharSequence[] getTextArray(int id) throws NotFoundException {
        return linguist.translate(resources.getTextArray(id));
    }

    @NonNull
    @Override
    public String[] getStringArray(int id) throws NotFoundException {
        return linguist.translate(resources.getStringArray(id));
    }
}
