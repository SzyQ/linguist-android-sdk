package io.stringx;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import io.stringx.sdk.R;

public final class StringXOverlayActivity extends Activity {

    public static final int REQUEST_CODE = 7331;
    private StringX stringX;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stringX = StringX.get(StringXOverlayActivity.this);
        if (stringX == null) {
            finish();
            return;
        }
        try {
            stringX.forceLocale(this, stringX.getDeviceLanguage().toLocale());
        } catch (UnsupportedLanguageException ignored) {
            finish();
        }
        setContentView(R.layout.activity_overlay);
        setupTranslateButton();
        setupCloseButton();
        setupNeverTranslateButton();
        setupMessage();
        try {
            stringX.forceDefault(this);
        } catch (UnsupportedLanguageException ignored) {
        }
    }

    private void setupMessage() {
        TextView message = findViewById(R.id.message);
        String defaultLanguage = stringX.getAppDefaultLanguage().toLocale().getDisplayLanguage();
        message.setText(getString(R.string.sX_translate_message, defaultLanguage));
    }

    private void setupCloseButton() {
        View close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringX.TranslationListener listener = stringX.getListener();
                if (listener != null) {
                    listener.onTranslationCanceled();
                }
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void setupNeverTranslateButton() {
        TextView neverTranslate = findViewById(R.id.never_translate);
        final StringX stringX = StringX.get(StringXOverlayActivity.this);
        final String deviceLanguage = stringX.getAppDefaultLanguage().toLocale().getDisplayLanguage();
        neverTranslate.setText(getString(R.string.sX_never_translate, deviceLanguage));
        neverTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringX.TranslationListener listener = stringX.getListener();
                if (listener != null) {
                    listener.onTranslationDisabled();
                }
                stringX.setOptOut(true);
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
                StringX.TranslationListener listener = stringX.getListener();
                if (listener != null) {
                    listener.onTranslationEnabled();
                }
                stringX.setEnabled(true);
                stringX.restart();
                finish();
            }
        });
    }
}
