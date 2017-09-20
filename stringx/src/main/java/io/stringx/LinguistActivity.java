package io.stringx;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuInflater;

public class LinguistActivity extends AppCompatActivity {

    private AppCompatDelegate mDelegate;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Linguist.wrap(base));
    }

    @NonNull
    public AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = Linguist.wrap(this, this);
        }
        if (mDelegate == null) {
            mDelegate = super.getDelegate();
        }
        return mDelegate;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Linguist linguist = Linguist.get(this);
        if (linguist != null) {
            linguist.onResume(this);
        }
    }

    @NonNull
    @Override
    public MenuInflater getMenuInflater() {
        return Linguist.wrap(this, super.getMenuInflater());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Linguist linguist = Linguist.get(this);
        if (linguist != null && linguist.onActivityResult(requestCode, resultCode, data)) {
            finish();
            startActivity(getIntent());
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
