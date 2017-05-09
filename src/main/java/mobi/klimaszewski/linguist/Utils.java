package mobi.klimaszewski.linguist;

import android.content.Context;
import android.content.res.Resources;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utils {

    public static List<String> getAppStrings(Context context, List<Integer> resources) {
        List<String> strings = new ArrayList<>();
        for (Integer resource : resources) {
            try {
                String string = context.getResources().getString(resource);
                strings.add(string);
            } catch (Resources.NotFoundException ignore) {
            }
        }
        return strings;
    }

    public static List<Integer> getAppStringResources(Context context, Class... stringClasses) {
        List<Integer> resources = new ArrayList<>();
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
