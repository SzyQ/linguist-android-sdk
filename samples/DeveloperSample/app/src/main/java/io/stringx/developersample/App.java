package io.stringx.developersample;

import android.app.Application;

import mobi.klimaszewski.linguist.Language;
import mobi.klimaszewski.linguist.Linguist;
import mobi.klimaszewski.linguist.Options;
import mobi.klimaszewski.linguist.Translatable;

public class App extends Application implements Translatable {

    private Linguist linguist;

    @Override
    public Linguist getLinguist() {
        return linguist;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Options options = new Options.Builder(this, Language.English)
                .setAutoTranslatedLanguages(Language.values())
                .setSupportedLanguages(Language.Polish)
                .addStrings(R.string.class)
                .excludeStrings(android.support.v7.appcompat.R.string.class)
                .build();
        linguist = Linguist.init(options);
    }
}
