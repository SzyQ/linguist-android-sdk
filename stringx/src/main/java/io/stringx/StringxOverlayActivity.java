package io.stringx;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import io.stringx.client.R;

public final class StringxOverlayActivity extends StringXActivityBase {

    private StringX stringX;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stringX = StringX.get(StringxOverlayActivity.this);
        setContentView(R.layout.activity_overlay);
        setupTranslateButton();
        setupNeverTranslateButton();
        setupCloseButton();
        setupMessage();
    }

    private void setupMessage() {
        TextView message = findViewById(R.id.message);
        String defaultLanguage = stringX.getDefaultLocale().getDisplayLanguage();
        message.setText(getString(R.string.sX_translate_message, defaultLanguage));
    }

    private void setupCloseButton() {
        View close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringX.TranslationListener listener = stringX.getListener();
                if(listener != null){
                    listener.onTranslationCanceled();
                }
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void setupNeverTranslateButton() {
        TextView neverTranslate = findViewById(R.id.never_translate);
        final StringX stringX = StringX.get(StringxOverlayActivity.this);
        String deviceLanguage = stringX.getDefaultLocale().getDisplayLanguage();
        neverTranslate.setText(getString(R.string.sX_never_translate, deviceLanguage));
        neverTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringX.TranslationListener listener = stringX.getListener();
                if(listener != null){
                    listener.onTranslationDisabled();
                }
                stringX.setEnabled(false);
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
                if(listener != null){
                    listener.onTranslationStarted();
                }
                startStringXService();
            }
        });
    }
}
