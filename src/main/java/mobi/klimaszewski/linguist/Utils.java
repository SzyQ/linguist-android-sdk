package mobi.klimaszewski.linguist;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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

    public static List<Integer> getAppStringResources(Context context, List<Class> stringClasses) {
        List<Integer> resources = new ArrayList<>();
        for (Class stringClass : stringClasses) {
            Field[] fields = stringClass.getDeclaredFields(); // or Field[] fields = R.string.class.getFields();
            for (Field field : fields) {
                int resId = context.getResources().getIdentifier(field.getName(), "string", context.getPackageName());
                if (resId != 0) {
                    resources.add(resId);
                }
            }
        }
        return resources;
    }

    public static void openStore(Context context, String packageName) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
        } catch (ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
    }
}
