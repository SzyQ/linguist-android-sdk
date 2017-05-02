package mobi.klimaszewski.linguist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.app.LinguistAppCompatDelegate;

public class LinguistActivity extends AppCompatActivity {

    private AppCompatDelegate mDelegate;
    private LinguistViewTranslator viewTranslator;

    @Override
    protected void attachBaseContext(Context newBase) {
        Linguist linguist = new Linguist();
        viewTranslator = new LinguistViewTranslator(linguist);
        super.attachBaseContext(new LinguistContextWrapper(newBase, viewTranslator,linguist));
    }

    /**
     * @return The {@link AppCompatDelegate} being used by this Activity.
     */
    @NonNull
    public AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = LinguistAppCompatDelegate.wrap(this,this,viewTranslator);
        }
        return mDelegate;
    }

}
