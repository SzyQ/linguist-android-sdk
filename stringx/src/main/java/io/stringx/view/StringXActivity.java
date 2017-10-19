package io.stringx.view;

import android.support.v7.app.AppCompatActivity;

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
