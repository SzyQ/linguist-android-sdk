package mobi.klimaszewski.linguist;

import android.content.Context;
import android.content.res.Resources;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class Utils {

    public static Set<String> getAppStrings(Context context, Set<Integer> resources) {
        HashSet<String> strings = new HashSet<>();
        for (Integer resource : resources) {
            try {
                String string = context.getResources().getString(resource);
                strings.add(string);
            } catch (Resources.NotFoundException ignore) {
            }
        }
        return strings;
    }

    public static Set<Integer> getAppStringResources(Context context, Class... stringClasses) {
        HashSet<Integer> resources = new HashSet<>();
        for (Class stringClass : stringClasses) {
            Field[] fields = stringClass.getDeclaredFields(); // or Field[] fields = R.string.class.getFields();
            for (int i = 0; i < fields.length; i++) {
                int resId = context.getResources().getIdentifier(fields[i].getName(), "string", context.getPackageName());
                if (resId != 0) {
                    resources.add(resId);
                }
            }
        }
        return resources;
    }
}
