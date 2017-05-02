package mobi.klimaszewski.linguist;

import android.app.Application;
import android.content.Context;

public class LinguistApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        Linguist linguist = new Linguist();
        super.attachBaseContext(new LinguistContextWrapper(base, new LinguistViewTranslator(linguist),linguist));
    }
}
