package io.stringx;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

public final class StringxOverlayActivity extends StringXActivityBase {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlay);
        setupTranslateButton();
        setupNeverTranslateButton();
        setupCloseButton();
        setupMessage();
    }

    private void setupMessage() {
        TextView message = findViewById(R.id.message);
        final Stringx stringx = Stringx.get(StringxOverlayActivity.this);
        String defaultLanguage = stringx.getAppDefaultLocale().getDisplayLanguage();
        message.setText(getString(R.string.sX_translate_message, defaultLanguage));
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
        final Stringx stringx = Stringx.get(StringxOverlayActivity.this);
        String deviceLanguage = stringx.getAppDefaultLocale().getDisplayLanguage();
        neverTranslate.setText(getString(R.string.sX_never_translate, deviceLanguage));
        neverTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringx.setOptOut(true);
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
                startStringXService();
            }
        });
    }
}
