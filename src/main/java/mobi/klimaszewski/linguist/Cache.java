package mobi.klimaszewski.linguist;


import android.support.annotation.Nullable;

public interface Cache {

    @Nullable
    String get(String text);

    boolean isNeverTranslateEnabled(String countryCode);

    void setNeverTranslateEnabled(String countryCode, boolean isEnabled);
}
