package io.stringx;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Utils {

    static void getAppStrings(Context context, List<Pair<Integer, String>> resources, ConfigCallback callback) throws RemoteException {
        List<String> mainStrings = new ArrayList<>();
        List<String> mainStringNames = new ArrayList<>();
        List<Integer> mainStringIds = new ArrayList<>();
        for (Pair<Integer, String> resource : resources) {
            try {
                String string = context.getResources().getString(resource.first);
                mainStrings.add(string);
                mainStringNames.add(resource.second);
                mainStringIds.add(resource.first);
            } catch (Resources.NotFoundException ignore) {
            }
        }
        callback.onDefaultStringIdsReceived(toIntArray(mainStringIds));
        callback.onDefaultStringNamesReceived(mainStringNames);
        callback.onDefaultStringsReceived(mainStrings);

        if (Build.VERSION.SDK_INT >= 17) {
            for (Language language : Language.values()) {
                Locale sideLocale = new Locale(language.getCode());
                ArrayList<Integer> sideStrings = new ArrayList<>();

                Resources localizedResources = getLocalizedResources(context, sideLocale);
                for (Pair<Integer, String> resourceId : resources) {
                    try {
                        String string = localizedResources.getString(resourceId.first);
                        if(mainStrings.contains(string)){//TODO sometimes in both languages translation is the same...
                            sideStrings.add(resourceId.first);
                        }
                    }catch (Resources.NotFoundException ignored){
                    }
                }
                String code = language.getCode();
                int[] stringIds = toIntArray(sideStrings);
                LL.v("Sending ids for "+code);
                callback.onLanguageReceived(code, stringIds);
            }
        }else{
            //TODO Implement for earlier apis
        }
    }

    private static int[] toIntArray(List<Integer> mainStringIds) {
        int[] ids = new int[mainStringIds.size()];
        for (int i = 0; i < mainStringIds.size(); i++) {
            ids[i] = mainStringIds.get(i);
        }
        return ids;
    }

    @NonNull
    @TargetApi(17)
    public static Resources getLocalizedResources(Context context, Locale desiredLocale) {
        Configuration conf = context.getResources().getConfiguration();
        conf = new Configuration(conf);
        conf.setLocale(desiredLocale);
        Context localizedContext = context.createConfigurationContext(conf);
        return localizedContext.getResources();
    }

    public static void get(Context context, Locale locale, List<Integer> ids) {
        Resources res = context.getResources();
        Configuration conf = res.getConfiguration();
        Locale savedLocale = conf.locale;
        conf.locale = locale; // whatever you want here
        res.updateConfiguration(conf, null); // second arg null means don't change

        // retrieve resources from desired locale
        for (Integer id : ids) {
            String str = res.getString(id);
        }

        // restore original locale
        conf.locale = savedLocale;
        res.updateConfiguration(conf, null);
    }

    static List<Pair<Integer, String>> getAppStringResources(Context context, List<Class> stringClasses) {
        List<Pair<Integer, String>> resources = new ArrayList<>();
        for (Class stringClass : stringClasses) {
            Field[] fields = stringClass.getDeclaredFields();
            for (Field field : fields) {
                int resId = context.getResources().getIdentifier(field.getName(), "string", context.getPackageName());
                if (resId != 0) {
                    resources.add(new Pair<>(resId, field.getName()));
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
