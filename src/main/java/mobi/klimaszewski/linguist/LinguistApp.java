package mobi.klimaszewski.linguist;

import android.app.Application;

import java.util.Collections;

public abstract class LinguistApp extends Application implements Translatable {

    private Linguist linguist;

    @Override
    public void onCreate() {
        super.onCreate();
        linguist = new Linguist(this,
                new ServiceTranslationFactory(this),
                new PreferencesCache(this),
                getStringClasses(),
                "en",
                Collections.singletonList("en"));
    }

    protected abstract Class getStringClasses();

    @Override
    public Linguist getLinguist() {
        return linguist;
    }
}
