package io.stringx;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

public final class StringXProxyActivity extends StringXActivityBase {

    public static void start(Context context) {
        context.startActivity(new Intent(context, StringXProxyActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            startStringXService();
        } catch (UnsupportedLanguageException e) {
            finish();
        }
    }

}
