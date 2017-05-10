package mobi.klimaszewski.linguist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.app.LinguistAppCompatDelegate;
import android.view.MenuInflater;

public class LinguistActivity extends AppCompatActivity {

    private AppCompatDelegate mDelegate;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LinguistContextWrapper.wrap(base));
    }

    /**
     * @return The {@link AppCompatDelegate} being used by this Activity.
     */
    @NonNull
    public AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = LinguistAppCompatDelegate.wrap(this, this);
        }
        return mDelegate;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Linguist.getInstance().onResume(this);
    }

    @Override
    public MenuInflater getMenuInflater() {
        return LinguistMenuInflater.wrap(this, super.getMenuInflater());
    }
}
