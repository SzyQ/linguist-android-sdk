package io.stringx;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Utils {

    static List<StringResource> getAppStrings(Context context, List<Pair<Integer, String>> resources, Locale locale) {
        List<StringResource> result = new ArrayList<>();
        List<StringResource> mainStrings = new ArrayList<>();
        for (Pair<Integer, String> resource : resources) {
            try {
                String string = context.getResources().getString(resource.first);
                StringResource stringResource = new StringResource();
                stringResource.string = string;
                stringResource.fieldName = resource.second;
                stringResource.resourceId = resource.first;
                stringResource.language = Language.fromCode(locale.getLanguage());
                mainStrings.add(stringResource);
            } catch (Resources.NotFoundException ignore) {
            }
        }
        result.addAll(mainStrings);
        if (Build.VERSION.SDK_INT >= 17) {
            for (Language language : Language.values()) {
                Locale sideLocale = new Locale(language.getCode());
                ArrayList<StringResource> sideStrings = new ArrayList<>();
                if (locale.equals(sideLocale)) {
                    continue;
                }

                Resources localizedResources = getLocalizedResources(context, sideLocale);
                for (Pair<Integer, String> resourceId : resources) {
                    StringResource resource = new StringResource();
                    resource.resourceId = resourceId.first;
                    resource.fieldName = resourceId.second;
                    resource.string = localizedResources.getString(resourceId.first);
                    //if the same as in main strings, then it's not translated
                    if (mainStrings.contains(resource)) {
                        sideStrings.add(resource);
                    }
                }
                result.addAll(sideStrings);
            }
        }
        return result;
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
