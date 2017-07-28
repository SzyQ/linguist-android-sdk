package mobi.klimaszewski.linguist;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utils {

    static List<String> getAppStrings(Context context, List<Integer> resources) {
        Set<String> strings = new HashSet<>();
        for (Integer resource : resources) {
            try {
                String string = context.getResources().getString(resource);
                strings.add(string);
            } catch (Resources.NotFoundException ignore) {
            }
        }
        return new ArrayList<>(strings);
    }

    static List<Integer> getAppStringResources(Context context, List<Class> stringClasses) {
        List<Integer> resources = new ArrayList<>();
        for (Class stringClass : stringClasses) {
            Field[] fields = stringClass.getDeclaredFields();
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
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ActivityNotFoundException anfe) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
