package io.stringx.developersample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import mobi.klimaszewski.linguist.Linguist;

public class MainActivity extends AppCompatActivity {

    private boolean isTranslationHintShown;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Linguist.wrap(newBase));
    }

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
        Linguist linguist = Linguist.get(this);
        if (!isTranslationHintShown) {
            isTranslationHintShown = linguist.showTranslationHint(this);
        }
    }
}
