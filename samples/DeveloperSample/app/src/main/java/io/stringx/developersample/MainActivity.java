package io.stringx.developersample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.stringx.StringX;

public class MainActivity extends AppCompatActivity {

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
