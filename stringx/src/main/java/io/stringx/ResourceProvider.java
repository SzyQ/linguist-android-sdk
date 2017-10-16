package io.stringx;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public abstract class ResourceProvider {

    @NonNull
    protected final StringX stringX;
    protected final List<Pair<Integer, String>> resources;
    protected Context context;
    protected ConfigCallback callback;

    public ResourceProvider(Context context, ConfigCallback callback) {
        stringX = StringX.get(context);
        if (stringX == null) {
            throw new IllegalArgumentException("stringX is not set up!");
        }
        this.context = context;
        this.callback = callback;
        resources = getResourcesIds();
    }

    public static ResourceProvider newResourceProvider(Context applicationContext, ConfigCallback callback) {
        if (Build.VERSION.SDK_INT >= 17) {
            return new ResourceProviderV17(applicationContext, callback);
        } else {
            return new ResourceProviderCompat(applicationContext, callback);
        }
    }

    private static int[] toIntArray(List<Integer> mainStringIds) {
        int[] ids = new int[mainStringIds.size()];
        for (int i = 0; i < mainStringIds.size(); i++) {
            ids[i] = mainStringIds.get(i);
        }
        return ids;
    }

    protected int[] getStrings(List<String> mainStrings, Resources localizedResources) {
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

    @NonNull
    private List<Pair<Integer, String>> getResourcesIds() {
        Options options = stringX.getOptions();
        List<Pair<Integer, String>> supportedResources = getAppStringResources(options.getStringClasses());
        List<Pair<Integer, String>> excludedResources = getAppStringResources(options.getExcludedClasses());
        Iterator<Pair<Integer, String>> iterator = supportedResources.iterator();
        while (iterator.hasNext()) {
            Pair<Integer, String> resourceId = iterator.next();
            if (excludedResources.contains(resourceId)) {
                iterator.remove();
            } else if (options.getExcludedStringIds().contains(resourceId.first)) {
                iterator.remove();
            }
        }
        return supportedResources;
    }

    private List<Pair<Integer, String>> getAppStringResources(List<Class> stringClasses) {
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

    public abstract void fetchStringIdentifiers(List<String> mainStrings) throws UnsupportedLanguageException, RemoteException;

    public abstract void fetchDefaultStrings(List<String> mainStrings, List<String> mainStringNames, List<Integer> mainStringIds) throws RemoteException;

    protected void fetchDefaultStrings(Resources androidResources, List<Pair<Integer, String>> resources, List<String> mainStrings, List<String> mainStringNames, List<Integer> mainStringIds) throws RemoteException {
        for (Pair<Integer, String> resource : resources) {
            try {
                String string = androidResources.getString(resource.first);
                mainStrings.add(string);
                mainStringNames.add(resource.second);
                mainStringIds.add(resource.first);
                LL.v("R.id." + resource.second + " -> \"" + string + "\"");
            } catch (Resources.NotFoundException ignore) {
            }
        }
        callback.onDefaultStringIdsReceived(toIntArray(mainStringIds));
        callback.onDefaultStringNamesReceived(mainStringNames);
        callback.onDefaultStringsReceived(mainStrings);
    }

    private static class ResourceProviderCompat extends ResourceProvider {

        private ResourceProviderCompat(Context context, ConfigCallback callback) {
            super(context, callback);
        }

        @Override
        public void fetchStringIdentifiers(List<String> mainStrings) throws UnsupportedLanguageException, RemoteException {
            Resources localisedResources = context.getResources();
            Configuration conf = localisedResources.getConfiguration();
            Locale savedLocale = conf.locale;
            for (Language language : Language.values()) {
                if (stringX.getDeviceLanguage() == language) {
                    continue;
                }
                conf.locale = new Locale(language.getCode());
                localisedResources.updateConfiguration(conf, null);
                int[] strings = getStrings(mainStrings, localisedResources);
                callback.onLanguageReceived(language.getCode(), strings);
            }
            conf.locale = savedLocale;
            localisedResources.updateConfiguration(conf, null);
        }

        @Override
        public void fetchDefaultStrings(List<String> mainStrings, List<String> mainStringNames, List<Integer> mainStringIds) throws RemoteException {
            Locale appDefaultLocale = stringX.getAppLanguage().toLocale();
            Resources defaultResources = context.getResources();
            Configuration conf = defaultResources.getConfiguration();
            Locale savedLocale = conf.locale;
            conf.locale = appDefaultLocale;
            defaultResources.updateConfiguration(conf, null);
            fetchDefaultStrings(defaultResources, resources, mainStrings, mainStringNames, mainStringIds);
            conf.locale = savedLocale;
            defaultResources.updateConfiguration(conf, null);
        }
    }

    private static class ResourceProviderV17 extends ResourceProvider {

        public ResourceProviderV17(Context context, ConfigCallback callback) {
            super(context, callback);
        }

        @Override
        public void fetchDefaultStrings(List<String> mainStrings, List<String> mainStringNames, List<Integer> mainStringIds) throws RemoteException {
            Locale appDefaultLocale = stringX.getAppLanguage().toLocale();
            Resources defaultResources = getLocalizedResources(appDefaultLocale);
            fetchDefaultStrings(defaultResources, resources, mainStrings, mainStringNames, mainStringIds);
        }

        @Override
        public void fetchStringIdentifiers(List<String> mainStrings) throws RemoteException, UnsupportedLanguageException {
            for (Language language : Language.values()) {
                //TODO Shouldn't be get app default?
                if (stringX.getAppLanguage() == language) {
                    continue;
                }
                Locale sideLocale = new Locale(language.getCode());
                Resources localizedResources = getLocalizedResources(sideLocale);
                int[] strings = getStrings(mainStrings, localizedResources);
                callback.onLanguageReceived(language.getCode(), strings);
            }

        }

        @NonNull
        @TargetApi(17)
        private Resources getLocalizedResources(Locale desiredLocale) {
            Configuration conf = context.getResources().getConfiguration();
            conf = new Configuration(conf);
            conf.setLocale(desiredLocale);
            Context localizedContext = context.createConfigurationContext(conf);
            return localizedContext.getResources();
        }
    }
}
