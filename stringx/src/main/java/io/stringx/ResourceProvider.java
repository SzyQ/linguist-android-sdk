package io.stringx;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Pair;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

class ResourceProvider {

    @NonNull
    private final StringX stringX;
    private final List<Pair<Integer, String>> resources;
    private Context context;
    private ConfigCallback callback;

    ResourceProvider(Context context, ConfigCallback callback) {
        stringX = StringX.get(context);
        if (stringX == null) {
            throw new IllegalArgumentException("stringX is not set up!");
        }
        this.context = context;
        this.callback = callback;
        resources = getResourcesIds();
    }

    private static int[] toIntArray(List<Integer> mainStringIds) {
        int[] ids = new int[mainStringIds.size()];
        for (int i = 0; i < mainStringIds.size(); i++) {
            ids[i] = mainStringIds.get(i);
        }
        return ids;
    }

    private int[] getStrings(List<String> mainStrings, Resources localizedResources) {
        ArrayList<Integer> sideStrings = new ArrayList<>();
        for (Pair<Integer, String> resourceId : resources) {
            try {
                String string = localizedResources.getString(resourceId.first);
                if (mainStrings.contains(string)) {
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

    void fetchStringIdentifiers(final List<String> mainStrings) throws Exception {
        for (final Language language : Language.values()) {
            if (stringX.getAppDefaultLanguage() == language) {
                continue;
            }
            LocalisedResourceRunnable.get(context, language).run(new TranslationTask() {
                @Override
                public void run(Resources androidResources) throws Exception {
                    int[] strings = getStrings(mainStrings, androidResources);
                    callback.onLanguageReceived(language.getCode(), strings);
                }
            });
        }
    }

    void fetchDefaultStrings(final List<String> mainStrings, final List<String> mainStringNames, final List<Integer> mainStringIds) throws Exception {
        LocalisedResourceRunnable
                .get(context, stringX.getAppDefaultLanguage())
                .run(new TranslationTask() {
                    @Override
                    public void run(Resources androidResources) throws Exception {
                        for (Pair<Integer, String> resource : resources) {
                            try {
                                String string = androidResources.getString(resource.first);
                                mainStrings.add(string);
                                mainStringNames.add(resource.second);
                                mainStringIds.add(resource.first);
                                SXLog.v("R.id." + resource.second + " -> \"" + string + "\"");
                            } catch (Resources.NotFoundException ignore) {
                            }
                        }
                        filterIgnoredStrings(mainStrings, mainStringNames, mainStringIds);
                        callback.onDefaultStringIdsReceived(toIntArray(mainStringIds));
                        callback.onDefaultStringNamesReceived(mainStringNames);
                        callback.onDefaultStringsReceived(mainStrings);
                    }
                });
    }

    /**
     * Filters resources from non translatable strings by comparing to first supported locale.
     * Using fact that non translatable string will be the same in both locales
     */
    private void filterIgnoredStrings(final List<String> mainStrings, final List<String> mainStringNames, final List<Integer> mainStringIds) throws Exception {
        List<Language> supportedLanguages = stringX.getSupportedLanguages();
        Language supportedLanguage = null;
        Language defaultLanguage = stringX.getAppDefaultLanguage();
        for (Language language : supportedLanguages) {
            if (language != defaultLanguage) {
                supportedLanguage = language;
                break;
            }
        }

        if (supportedLanguage == null) {
            return;
        }
        LocalisedResourceRunnable.get(context, supportedLanguage).run(new TranslationTask() {
            @Override
            public void run(Resources defaultResources) {
                for (Pair<Integer, String> resource : resources) {
                    try {
                        String string = defaultResources.getString(resource.first);
                        int indexOfString = mainStrings.indexOf(string);
                        if (indexOfString != -1) {
                            mainStrings.remove(indexOfString);
                            mainStringNames.remove(indexOfString);
                            mainStringIds.remove(indexOfString);
                        }
                    } catch (Resources.NotFoundException ignore) {
                    }
                }
            }
        });
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

    private interface TranslationTask {
        void run(Resources resources) throws Exception;
    }

    private static class LocalisedResourceRunnable {

        private final Context context;
        private final Language language;

        public LocalisedResourceRunnable(Context context, Language language) {
            this.context = context;
            this.language = language;
        }

        public static LocalisedResourceRunnable get(Context context, Language language) {
            if (Build.VERSION.SDK_INT >= 17) {
                return new LocalisedResourceRunnableV17(context, language);
            } else {
                return new LocalisedResourceRunnable(context, language);
            }
        }

        public void run(TranslationTask task) throws Exception {
            Locale locale = language.toLocale();
            Resources resources = context.getResources();
            Configuration conf = resources.getConfiguration();
            Locale savedLocale = conf.locale;
            conf.locale = locale;
            resources.updateConfiguration(conf, null);
            task.run(resources);
            conf.locale = savedLocale;
            resources.updateConfiguration(conf, null);
        }
    }

    @RequiresApi(17)
    private static class LocalisedResourceRunnableV17 extends LocalisedResourceRunnable {

        private final Context context;
        private final Language language;

        public LocalisedResourceRunnableV17(Context context, Language language) {
            super(context, language);
            this.context = context;
            this.language = language;
        }

        @Override
        public void run(TranslationTask task) throws Exception {
            Configuration conf = context.getResources().getConfiguration();
            conf = new Configuration(conf);
            conf.setLocale(language.toLocale());
            Context localizedContext = context.createConfigurationContext(conf);
            Resources resources = localizedContext.getResources();
            task.run(resources);
        }
    }

}
