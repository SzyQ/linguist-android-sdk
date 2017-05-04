package mobi.klimaszewski.linguist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.app.LinguistAppCompatDelegate;
import android.view.MenuInflater;

public class LinguistActivity extends AppCompatActivity {

    private AppCompatDelegate mDelegate;
    private LinguistViewTranslator viewTranslator;

    @Override
    protected void attachBaseContext(Context base) {
        viewTranslator = LinguistApp.getViewTranslator(base);
        super.attachBaseContext(new LinguistContextWrapper(base, viewTranslator, LinguistApp.getLinguist(base)));
    }

    /**
     * @return The {@link AppCompatDelegate} being used by this Activity.
     */
    @NonNull
    public AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = LinguistAppCompatDelegate.wrap(this, this, viewTranslator);
        }
        return mDelegate;
    }

    @Override
    public MenuInflater getMenuInflater() {
        return new LinguistMenuInflater(this, super.getMenuInflater());
    }
}
