package io.stringx;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class Options {
    private final Context context;
    private List<Class> stringClasses;
    private List<Class> excludedClasses;
    private List<Integer> excludedStringIds;
    private Language defaultLanguage;
    private List<Language> supportedLanguages;
    private List<Language> autoTranslatedLanguages;
    private RestartStrategy restartStrategy;

    private Options(Context context, Language defaultLanguage) {
        this.context = context;
        this.defaultLanguage = defaultLanguage;
    }

    public List<Class> getStringClasses() {
        return stringClasses;
    }

    private void setStringClasses(List<Class> stringClasses) {
        this.stringClasses = stringClasses;
    }

    public List<Class> getExcludedClasses() {
        return excludedClasses;
    }

    private void setExcludedClasses(List<Class> excludedClasses) {
        this.excludedClasses = excludedClasses;
    }

    public Language getDefaultLanguage() {
        return defaultLanguage;
    }

    public List<Language> getSupportedLanguages() {
        return supportedLanguages;
    }

    private void setSupportedLanguages(List<Language> supportedLanguages) {
        this.supportedLanguages = supportedLanguages;
    }

    public List<Integer> getExcludedStringIds() {
        return excludedStringIds;
    }

    private void setExcludedStringIds(List<Integer> excludedStringIds) {
        this.excludedStringIds = excludedStringIds;
    }

    public RestartStrategy getRestartStrategy() {
        return restartStrategy;
    }

    void setRestartStrategy(RestartStrategy restartStrategy) {
        this.restartStrategy = restartStrategy;
    }

    public Context getContext() {
        return context;
    }

    public List<Language> getAutoTranslatedLanguages() {
        return autoTranslatedLanguages;
    }

    public void setAutoTranslatedLanguages(List<Language> autoTranslatedLanguages) {
        this.autoTranslatedLanguages = autoTranslatedLanguages;
    }

    public static class Builder {
        private Context context;
        private Language defaultLanguage;
        private Language[] supportedLanguages;
        private Language[] autoTranslatedLanguages;
        private List<Class> supportedStrings;
        private List<Class> excludedStrings;
        private int[] excludedStringIds;
        private RestartStrategy restartStrategy;

        public Builder(Context context,Language defaultLanguage) {
            this.context = context;
            this.defaultLanguage = defaultLanguage;
            supportedStrings = new ArrayList<>();
            excludedStrings = new ArrayList<>();
        }

        public Builder setSupportedLanguages(Language... supportedLanguages) {
            this.supportedLanguages = supportedLanguages;
            return this;
        }

        public Builder setAutoTranslatedLanguages(Language... autoTranslatedLanguages) {
            this.autoTranslatedLanguages = autoTranslatedLanguages;
            return this;
        }

        public Builder addStrings(Class clazz) {
            supportedStrings.add(clazz);
            return this;
        }

        public Builder excludeStrings(Class clazz) {
            excludedStrings.add(clazz);
            return this;
        }

        public Builder setRestartStrategy(RestartStrategy restartStrategy) {
            this.restartStrategy = restartStrategy;
            return this;
        }

        public Builder excludeString(int... stringId) {
            excludedStringIds = stringId;
            return this;
        }

        public Options build() {
            Options options = new Options(context, defaultLanguage);
            if (autoTranslatedLanguages == null || autoTranslatedLanguages.length == 0) {
                throw new IllegalArgumentException("Please add automatically translated languages");
            }
            if (excludedStringIds == null) {
                excludedStringIds = new int[0];
            }
            ArrayList<Integer> ids = new ArrayList<>();
            for (int stringId : excludedStringIds) {
                ids.add(stringId);
            }

            options.setExcludedStringIds(ids);
            options.setStringClasses(supportedStrings);
            options.setExcludedClasses(excludedStrings);
            if (supportedLanguages == null) {
                supportedLanguages = new Language[]{defaultLanguage};
            }
            ArrayList<Language> languages = new ArrayList<>();
            boolean isDefaultLanguageAdded = false;
            for (Language supportedLanguage : supportedLanguages) {
                if (supportedLanguage == defaultLanguage) {
                    isDefaultLanguageAdded = true;
                }
                languages.add(supportedLanguage);
            }
            if (!isDefaultLanguageAdded) {
                languages.add(defaultLanguage);
            }
            options.setSupportedLanguages(languages);
            List<Language> autoTranslatedLanguages = Arrays.asList(this.autoTranslatedLanguages);
            autoTranslatedLanguages.removeAll(languages);
            options.setAutoTranslatedLanguages(autoTranslatedLanguages);
            if (restartStrategy == null) {
                restartStrategy = new DefaultRestartStrategy(context);
            }
            options.setRestartStrategy(restartStrategy);

            return options;
        }
    }

    public interface RestartStrategy {
        void restart();
    }

    private static class DefaultRestartStrategy implements RestartStrategy {

        private Context context;

        public DefaultRestartStrategy(Context context){
            this.context = context;
        }

        public static void triggerRebirth(Context context) {
            PackageManager packageManager = context.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK); // In case we are called with non-Activity context.
            context.startActivity(intent);
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
            Runtime.getRuntime().exit(0);
        }

        @Override
        public void restart() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    triggerRebirth(context);
                }
            },100);
        }
    }
}
