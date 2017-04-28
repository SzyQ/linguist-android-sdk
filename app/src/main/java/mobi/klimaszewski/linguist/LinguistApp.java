package mobi.klimaszewski.linguist;

import android.app.Application;
import android.content.Context;

public class LinguistApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(new LinguistContextWrapper(base));
    }
}
