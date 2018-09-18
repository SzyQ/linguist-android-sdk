package mobi.klimaszewski.linguist;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import mobi.klimaszewski.linguist.R;

public final class LinguistOverlayActivity extends Activity {

    public static final int REQUEST_CODE = 7331;
    private Linguist linguist;

    @Override
    protected void attachBaseContext(Context newBase) {
        linguist = Linguist.get(newBase);
        if (linguist == null) {
            finish();
            return;
        }
        super.attachBaseContext(linguist.forceAppDefault(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ls_activity_overlay);
        setupTranslateButton();
        setupCloseButton();
        setupNeverTranslateButton();
        setupMessage();
    }

    private void setupMessage() {
        TextView message = findViewById(R.id.message);
        String defaultLanguage = linguist.getAppDefaultLanguage().toLocale().getDisplayLanguage();
        message.setText(getString(R.string.ls_translate_message, defaultLanguage));
    }

    private void setupCloseButton() {
        View close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Linguist.TranslationListener listener = linguist.getListener();
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
        final Linguist linguist = Linguist.get(LinguistOverlayActivity.this);
        final String deviceLanguage = linguist.getAppDefaultLanguage().toLocale().getDisplayLanguage();
        neverTranslate.setText(getString(R.string.ls_never_translate, deviceLanguage));
        neverTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Linguist.TranslationListener listener = linguist.getListener();
                if (listener != null) {
                    listener.onTranslationDisabled();
                }
                linguist.setOptOut(true);
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
                Linguist.TranslationListener listener = linguist.getListener();
                if (listener != null) {
                    listener.onTranslationEnabled();
                }
                linguist.setEnabled(true);
                linguist.restart();
                finish();
            }
        });
    }
}
