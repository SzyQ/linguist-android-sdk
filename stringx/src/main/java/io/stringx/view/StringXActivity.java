package io.stringx.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuInflater;

import io.stringx.StringX;

public class StringXActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        StringX stringX = StringX.get(this);
        if (stringX != null) {
            stringX.onResume(this);
        }
    }
}
