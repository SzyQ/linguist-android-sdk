package mobi.klimaszewski.linguist;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;

public class LinguistContextWrapper extends ContextWrapper {

    private LinguistResources resources;
    private LinguistLayoutInflater inflater;
    private Context originalContext;

    public LinguistContextWrapper(Context base) {
        super(base);
        originalContext = base;
    }

    @Override
    public Resources getResources() {
        if (resources == null) {
            resources = new LinguistResources(super.getResources());
        }
        return resources;
    }

    @Override
    public Object getSystemService(String name) {
//        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
//            if (inflater == null) {
//                inflater = new LinguistLayoutInflater(this,originalContext);
//            }
//            return inflater;
//        }
        return super.getSystemService(name);
    }
}
