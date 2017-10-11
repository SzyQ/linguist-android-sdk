package io.stringx.developersample;

import android.app.Application;

import io.stringx.LL;
import io.stringx.Language;
import io.stringx.Options;
import io.stringx.StringX;
import io.stringx.Translatable;
import io.stringx.UnsupportedLanguageException;

public class App extends Application implements Translatable {

    private StringX linguist;

    @Override
    public void onCreate() {
        super.onCreate();
        Options options = new Options.Builder(this, Language.English)
                .setMode(Options.Mode.AndroidResources)
                .addStrings(R.string.class)
                .excludeString(R.string.app_name)
                .excludeStrings(android.support.v4.R.string.class)
                .excludeStrings(android.support.v7.appcompat.R.string.class)
                .build();
        try {
            linguist = new StringX(options);
        } catch (UnsupportedLanguageException e) {
            LL.w("Unsupported language");
        }
    }

    @Override
    public StringX getStringX() {
        return linguist;
    }
}
