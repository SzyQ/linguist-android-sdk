package io.stringx;


import android.support.annotation.Nullable;

public interface Cache {

    @Nullable
    String get(String text);

    boolean isNeverTranslateEnabled(String countryCode);

    void setNeverTranslateEnabled(String countryCode, boolean isEnabled);

    void setOptOut(boolean isOptedOut);

    boolean isOptOut();

    void setTranslationEnabled(String countryCode, boolean isEnabled);

    boolean isTranslationEnabled(String languageCode);

    void put(String text, String translated);
}
