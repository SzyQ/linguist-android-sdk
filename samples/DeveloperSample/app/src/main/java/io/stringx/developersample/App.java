package io.stringx.developersample;

import android.app.Application;

import io.stringx.Language;
import io.stringx.Options;
import io.stringx.Stringx;
import io.stringx.Translatable;

public class App extends Application implements Translatable {

    private Stringx linguist;

    @Override
    public void onCreate() {
        super.onCreate();
        Options options = new Options.Builder(this, Language.English)
                .setMode(Options.Mode.Developer)
                .addStrings(R.string.class)
                .excludeString(R.string.app_name)
                .excludeStrings(android.support.v4.R.string.class)
                .excludeStrings(android.support.v7.appcompat.R.string.class)
                .build();
        linguist = new Stringx(this, options);
    }

    @Override
    public Stringx getStringx() {
        return linguist;
    }
}
