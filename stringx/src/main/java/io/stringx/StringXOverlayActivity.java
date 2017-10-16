package io.stringx;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import io.stringx.client.R;

public final class StringXOverlayActivity extends AppCompatActivity {

    public static int REQUEST_CODE = 7331;
    private StringX stringX;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stringX = StringX.get(StringXOverlayActivity.this);
        try {
            stringX.forceLocale(this, stringX.getDeviceLanguage().toLocale());
        } catch (UnsupportedLanguageException ignored) {
            finish();
        }
        setContentView(R.layout.activity_overlay);
        setupTranslateButton();
        setupCloseButton();
        try {
            setupNeverTranslateButton();
            setupMessage();
        } catch (UnsupportedLanguageException ignored) {
            finish();
        }
    }

    private void setupMessage() {
        TextView message = findViewById(R.id.message);
        String defaultLanguage = stringX.getAppLanguage().toLocale().getDisplayLanguage();
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

    private void setupNeverTranslateButton() throws UnsupportedLanguageException {
        TextView neverTranslate = findViewById(R.id.never_translate);
        final StringX stringX = StringX.get(StringXOverlayActivity.this);
        final String deviceLanguage = stringX.getAppLanguage().toLocale().getDisplayLanguage();
        neverTranslate.setText(getString(R.string.sX_never_translate, deviceLanguage));
        neverTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringX.TranslationListener listener = stringX.getListener();
                if (listener != null) {
                    listener.onTranslationDisabled();
                }
                try {
                    stringX.setEnabled(false);
                    setResult(RESULT_OK);
                } catch (UnsupportedLanguageException e) {
                    setResult(RESULT_CANCELED);
                }
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
                try {
                    stringX.setEnabled(true);
                    stringX.restart();
                } catch (UnsupportedLanguageException e) {
                    //TODO
                }
                finish();
            }
        });
    }
}
