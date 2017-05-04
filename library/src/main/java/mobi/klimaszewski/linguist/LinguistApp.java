package mobi.klimaszewski.linguist;

import android.app.Application;
import android.content.Context;

public abstract class LinguistApp extends Application {

    private Linguist linguist;
    private LinguistViewTranslator viewTranslator;

    @Override
    protected void attachBaseContext(Context base) {
        linguist = new Linguist.Builder(base, getStringClasses())
                .setPrefetch(false)
                .build();
        viewTranslator = new LinguistViewTranslator(linguist);
//        super.attachBaseContext(new LinguistContextWrapper(base, viewTranslator, linguist));
        super.attachBaseContext(base);
    }

    public abstract Class[] getStringClasses();

    public static Linguist getLinguist(Context context){
        LinguistApp linguistApp = (LinguistApp) context.getApplicationContext();
        return linguistApp.linguist;
    }

    public static LinguistViewTranslator getViewTranslator(Context context){
        LinguistApp linguistApp = (LinguistApp) context.getApplicationContext();
        return linguistApp.viewTranslator;
    }

}
