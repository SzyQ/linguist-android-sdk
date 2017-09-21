package io.stringx.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuInflater;

import io.stringx.Stringx;

public class LinguistActivity extends AppCompatActivity {

    private AppCompatDelegate mDelegate;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Stringx.wrap(base));
    }

    @NonNull
    public AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = Stringx.wrap(this, this);
        }
        if (mDelegate == null) {
            mDelegate = super.getDelegate();
        }
        return mDelegate;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Stringx stringx = Stringx.get(this);
        if (stringx != null) {
            stringx.onResume(this);
        }
    }

    @NonNull
    @Override
    public MenuInflater getMenuInflater() {
        return Stringx.wrap(this, super.getMenuInflater());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Stringx stringx = Stringx.get(this);
        if (stringx != null && stringx.onActivityResult(requestCode, resultCode, data)) {
            finish();
            startActivity(getIntent());
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
