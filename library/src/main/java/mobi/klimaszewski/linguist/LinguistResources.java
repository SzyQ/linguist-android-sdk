package mobi.klimaszewski.linguist;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.support.annotation.AnyRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.TypedValue;

import mobi.klimaszewski.linguist.R;

public class LinguistResources extends Resources {
    private final Resources mResources;


    public LinguistResources(Resources resources) {
        super(resources.getAssets(), resources.getDisplayMetrics(), resources.getConfiguration());
        mResources = resources;
    }

    @Override
    public CharSequence getText(int id) throws NotFoundException {
        return mResources.getText(id);
    }

    @Override
    public CharSequence getQuantityText(int id, int quantity) throws NotFoundException {
        return mResources.getQuantityText(id, quantity);
    }

    @Override
    public void getValue(@AnyRes int id, TypedValue outValue, boolean resolveRefs) throws NotFoundException {
        super.getValue(id, outValue, resolveRefs);
    }

    @Override
    public void getValue(String name, TypedValue outValue, boolean resolveRefs) throws NotFoundException {
        super.getValue(name, outValue, resolveRefs);
    }

    @NonNull
    @Override
    public String getString(int id) throws NotFoundException {
        switch (id) {
            case R.string.text_code:
                return "Translated Text code";
            case R.string.text_resource: //Do not work
                return "Translated Text resource";
        }
        return mResources.getString(id);
    }

    @Override
    public String getString(int id, Object... formatArgs) throws NotFoundException {
        return mResources.getString(id, formatArgs);
    }

    @Override
    public String getQuantityString(int id, int quantity, Object... formatArgs)
            throws NotFoundException {
        return mResources.getQuantityString(id, quantity, formatArgs);
    }

    @Override
    public String getQuantityString(int id, int quantity) throws NotFoundException {
        return mResources.getQuantityString(id, quantity);
    }

    @Override
    public CharSequence getText(int id, CharSequence def) {
        return mResources.getText(id, def);
    }

    @Override
    public CharSequence[] getTextArray(int id) throws NotFoundException {
        return mResources.getTextArray(id);
    }

    @Override
    public String[] getStringArray(int id) throws NotFoundException {
        return mResources.getStringArray(id);
    }

    @Override
    public int[] getIntArray(int id) throws NotFoundException {
        return mResources.getIntArray(id);
    }

    @Override
    public XmlResourceParser getXml(int id) throws NotFoundException {
        return mResources.getXml(id);
    }

    @Override
    public XmlResourceParser getLayout(@LayoutRes int id) throws NotFoundException {
        return super.getLayout(id);
    }
}
