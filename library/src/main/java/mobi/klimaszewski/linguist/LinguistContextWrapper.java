package mobi.klimaszewski.linguist;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.view.LayoutInflater;

public class LinguistContextWrapper extends ContextWrapper {

    private LinguistResources resources;
    private LinguistViewTranslator viewTranslator;
    private Linguist linguist;
    private LayoutInflater inflater;

    public LinguistContextWrapper(Context base, LinguistViewTranslator viewTranslator, Linguist linguist) {
        super(base);
        this.viewTranslator = viewTranslator;
        this.linguist = linguist;
    }

    @Override
    public Resources getResources() {
        if (resources == null) {
            resources = new LinguistResources(super.getResources(), linguist);
        }
        return resources;
    }

    @Override
    public Object getSystemService(String name) {
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (inflater == null) {
                inflater = new LinguistLayoutInflater((LayoutInflater) super.getSystemService(name), getBaseContext(), viewTranslator).cloneInContext(this);
            }
            return inflater;
        }
        return super.getSystemService(name);
    }


}
