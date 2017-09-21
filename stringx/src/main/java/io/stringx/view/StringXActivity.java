package io.stringx.view;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuInflater;

import io.stringx.RestartBroadcast;
import io.stringx.Stringx;

public class StringXActivity extends AppCompatActivity {

    private AppCompatDelegate mDelegate;
    private RestartBroadcast broadcast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        broadcast = new RestartBroadcast(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcast, new IntentFilter(RestartBroadcast.ACTION));
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcast);
    }

    @Override
    protected void onPause() {
        super.onPause();
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
//            finish();
//            startActivity(getIntent());
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
