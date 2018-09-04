package io.stringx.developersample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Locale;

import io.stringx.StringX;

public class MainActivity extends AppCompatActivity {

    /** This needs to be set if language is forced
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(StringX.get(newBase).forceLocale(newBase, Locale.forLanguageTag("pl")));
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.settings).setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, PreferenceActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        StringX.get(this).onResume(this);
    }
}
