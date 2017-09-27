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

    static void getAppStrings(StringX stringX, Context context, List<Pair<Integer, String>> resources, ConfigCallback callback) throws RemoteException {
        List<String> mainStrings = new ArrayList<>();
        List<String> mainStringNames = new ArrayList<>();
        List<Integer> mainStringIds = new ArrayList<>();
        Resources defaultResources;

        Locale appDefaultLocale = stringX.getDefaultLocale();
        if (Build.VERSION.SDK_INT >= 17) {
            defaultResources = getLocalizedResources(context, appDefaultLocale);
            fetchDefaultStrings(resources, mainStrings, mainStringNames, mainStringIds, defaultResources);
        } else {
            defaultResources = context.getResources();
            Configuration conf = defaultResources.getConfiguration();
            Locale savedLocale = conf.locale;
            conf.locale = appDefaultLocale;
            defaultResources.updateConfiguration(conf, null);
            fetchDefaultStrings(resources, mainStrings, mainStringNames, mainStringIds, defaultResources);
            conf.locale = savedLocale;
            defaultResources.updateConfiguration(conf, null);
        }
        callback.onDefaultStringIdsReceived(toIntArray(mainStringIds));
        callback.onDefaultStringNamesReceived(mainStringNames);
        callback.onDefaultStringsReceived(mainStrings);

        if (Build.VERSION.SDK_INT >= 17) {
            for (Language language : Language.values()) {
                if(stringX.getOptions().getMode() == Options.Mode.User && stringX.getDefaultDeviceLanguage() != language){
                    continue;
                }
                Locale sideLocale = new Locale(language.getCode());
                Resources localizedResources = getLocalizedResources(context, sideLocale);
                int[] strings = getStrings(resources, mainStrings, localizedResources);
                callback.onLanguageReceived(language.getCode(), strings);
            }
        } else {
            Resources localisedResources = context.getResources();
            Configuration conf = localisedResources.getConfiguration();
            Locale savedLocale = conf.locale;
            for (Language language : Language.values()) {
                if(stringX.getOptions().getMode() == Options.Mode.User && stringX.getDefaultDeviceLanguage() != language){
                    continue;
                }
                conf.locale = new Locale(language.getCode());
                localisedResources.updateConfiguration(conf, null);
                int[] strings = getStrings(resources, mainStrings, localisedResources);
                callback.onLanguageReceived(language.getCode(), strings);
            }
            conf.locale = savedLocale;
            localisedResources.updateConfiguration(conf, null);
        }
    }

    private static void fetchDefaultStrings(List<Pair<Integer, String>> resources, List<String> mainStrings, List<String> mainStringNames, List<Integer> mainStringIds, Resources defaultResources) {
        for (Pair<Integer, String> resource : resources) {
            try {
                String string = defaultResources.getString(resource.first);
                mainStrings.add(string);
                mainStringNames.add(resource.second);
                mainStringIds.add(resource.first);
                LL.v("R.id." + resource.second + " -> \"" + string + "\"");
            } catch (Resources.NotFoundException ignore) {
            }
        }
    }

    private static int[] getStrings(List<Pair<Integer, String>> resources, List<String> mainStrings, Resources localizedResources) throws RemoteException {
        ArrayList<Integer> sideStrings = new ArrayList<>();
        for (Pair<Integer, String> resourceId : resources) {
            try {
                String string = localizedResources.getString(resourceId.first);
                if (mainStrings.contains(string)) {//TODO sometimes in both languages translation is the same...
                    sideStrings.add(resourceId.first);
                }
            } catch (Resources.NotFoundException ignored) {
            }
        }
        return toIntArray(sideStrings);
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
