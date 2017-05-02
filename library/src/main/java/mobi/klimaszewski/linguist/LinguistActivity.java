package mobi.klimaszewski.linguist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.app.LinguistAppCompatDelegate;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class LinguistActivity extends AppCompatActivity {

    private AppCompatDelegate mDelegate;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("Linguist", "Creating view");
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new LinguistContextWrapper(newBase, new Linguist()));
    }

    /**
     * @return The {@link AppCompatDelegate} being used by this Activity.
     */
    @NonNull
    public AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = new LinguistAppCompatDelegate(this, getWindow(), this, new Linguist());
        }
        return mDelegate;
    }

}
