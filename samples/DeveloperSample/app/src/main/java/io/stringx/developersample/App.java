package io.stringx.developersample;

import android.app.Application;

import io.stringx.Language;
import io.stringx.Options;
import io.stringx.StringX;
import io.stringx.Translatable;

public class App extends Application implements Translatable {

    private StringX stringX;

    @Override
    public StringX getStringX() {
        return stringX;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Options options = new Options.Builder(this, Language.English)
                .setSupportedLanguages(Language.Polish)
                .addStrings(R.string.class)
                .excludeString(R.string.app_name)
                .excludeStrings(android.support.v7.appcompat.R.string.class)
                .build();
        stringX = new StringX(options);
    }
}
