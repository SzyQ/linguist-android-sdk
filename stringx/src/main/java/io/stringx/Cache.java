package io.stringx;


import android.support.annotation.Nullable;

public interface Cache {

    @Nullable
    String get(String text);

    void put(String text, String translated);
}
