package mobi.klimaszewski.linguist;

import android.content.res.Resources;
import android.support.annotation.NonNull;

public class LinguistResources extends Resources {
    private final Resources resources;
    private Linguist linguist;


    public LinguistResources(Resources resources, Linguist linguist) {
        super(resources.getAssets(), resources.getDisplayMetrics(), resources.getConfiguration());
        this.resources = resources;
        this.linguist = linguist;
    }

    @Override
    public CharSequence getText(int id) throws NotFoundException {
        return linguist.translate(resources.getText(id));
    }

    @Override
    public CharSequence getQuantityText(int id, int quantity) throws NotFoundException {
        return linguist.translate(resources.getQuantityText(id, quantity));
    }

    @NonNull
    @Override
    public String getString(int id) throws NotFoundException {
        return linguist.translate(resources.getString(id));
    }

    @Override
    public String getString(int id, Object... formatArgs) throws NotFoundException {
        return linguist.translate(resources.getString(id, formatArgs));
    }

    @Override
    public String getQuantityString(int id, int quantity, Object... formatArgs)
            throws NotFoundException {
        return linguist.translate(resources.getQuantityString(id, quantity, formatArgs));
    }

    @Override
    public String getQuantityString(int id, int quantity) throws NotFoundException {
        return linguist.translate(resources.getQuantityString(id, quantity));
    }

    @Override
    public CharSequence getText(int id, CharSequence def) {
        return linguist.translate(resources.getText(id, def));
    }

    @Override
    public CharSequence[] getTextArray(int id) throws NotFoundException {
        return linguist.translate(resources.getTextArray(id));
    }

    @Override
    public String[] getStringArray(int id) throws NotFoundException {
        return linguist.translate(resources.getStringArray(id));
    }
}
