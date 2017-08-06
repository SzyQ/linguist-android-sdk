package io.stringx;


import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import io.stringx.app.R;

public final class LinguistOverlayActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 3001;
    private static final int REQUEST_CODE_TRANSLATE = 3002;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlay);
        setupTranslateButton();
        setupNeverTranslateButton();
        setupCloseButton();
        setupMessage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_TRANSLATE) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setupMessage() {
        TextView message = findViewById(R.id.message);
        final Linguist linguist = Linguist.get(LinguistOverlayActivity.this);
        String defaultLanguage = linguist.getAppDefaultLocale().getDisplayLanguage();
        message.setText(getString(R.string.translate_message, defaultLanguage));
    }

    private void setupCloseButton() {
        View close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void setupNeverTranslateButton() {
        TextView neverTranslate = findViewById(R.id.never_translate);
        final Linguist linguist = Linguist.get(LinguistOverlayActivity.this);
        String deviceLanguage = linguist.getAppDefaultLocale().getDisplayLanguage();
        neverTranslate.setText(getString(R.string.never_translate, deviceLanguage));
        neverTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linguist.setNeverTranslate(true);
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void setupTranslateButton() {
        View translate = findViewById(R.id.translate);
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String packageName = "io.stringx";
                intent.setComponent(new ComponentName(packageName, "io.stringx.MainActivity"));
                intent.putExtra("KEY_PACKAGE",getPackageName());
                try {
                    startActivityForResult(intent, REQUEST_CODE_TRANSLATE);
                } catch (ActivityNotFoundException e) {
                    Utils.openStore(getApplicationContext(), packageName);
                }
            }
        });
    }
}
