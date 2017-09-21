package io.stringx;


import android.support.annotation.Nullable;

public interface Cache {

    @Nullable
    String get(String text);

    boolean isOptOut();

    void setOptOut(boolean isOptedOut);

    boolean isEnabled(Language language);

    void setEnabled(Language language, boolean isEnabled);

    void put(String text, String translated);
}
