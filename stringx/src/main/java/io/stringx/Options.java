package io.stringx;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class Options {
    private List<Class> stringClasses;
    private List<Class> excludedClasses;
    private List<Integer> excludedStringIds;
    private Language defaultLanguage;
    private List<Language> supportedLanguages;
    private Mode mode;

    private Options() {

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

    private void setDefaultLanguage(Language defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public List<Language> getSupportedLanguages() {
        return supportedLanguages;
    }

    private void setSupportedLanguages(List<Language> supportedLanguages) {
        this.supportedLanguages = supportedLanguages;
    }

    public Mode getMode() {
        return mode;
    }

    private void setMode(Mode mode) {
        this.mode = mode;
    }

    public List<Integer> getExcludedStringIds() {
        return excludedStringIds;
    }

    private void setExcludedStringIds(List<Integer> excludedStringIds) {
        this.excludedStringIds = excludedStringIds;
    }

    public enum Mode {
        User, Developer
    }

    public static class Builder {
        private Context context;
        private Language defaultLanguage;
        private Language[] supportedLanguages;
        private List<Class> supportedStrings;
        private List<Class> excludedStrings;
        private int[] excludedStringIds;
        private Mode mode;

        public Builder(Context context, Language defaultLanguage) {
            this.context = context;
            this.defaultLanguage = defaultLanguage;
            supportedStrings = new ArrayList<>();
            excludedStrings = new ArrayList<>();
        }

        public Builder setMode(Mode mode) {
            this.mode = mode;
            return this;
        }

        public Builder setSupportedLanguages(Language... supportedLanguages) {
            this.supportedLanguages = supportedLanguages;
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

        public Options build() {
            Options options = new Options();
            if (mode == null) {
                mode = Mode.User;
            }
            if (excludedStringIds == null) {
                excludedStringIds = new int[0];
            }
            ArrayList<Integer> ids = new ArrayList<>();
            for (int stringId : excludedStringIds) {
                ids.add(stringId);
            }

            options.setExcludedStringIds(ids);
            options.setMode(mode);
            options.setStringClasses(supportedStrings);
            options.setExcludedClasses(excludedStrings);
            options.setDefaultLanguage(defaultLanguage);
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
            return options;
        }

        public Builder excludeString(int... stringId) {
            excludedStringIds = stringId;
            return this;
        }
    }
}
