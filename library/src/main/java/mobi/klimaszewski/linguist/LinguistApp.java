package mobi.klimaszewski.linguist;

import android.app.Application;

public abstract class LinguistApp extends Application implements Translatable {

    private Linguist linguist;

    @Override
    public void onCreate() {
        super.onCreate();
        linguist = new Linguist.Builder()
                .build();
    }

    @Override
    public Linguist getLinguist() {
        return linguist;
    }
}
