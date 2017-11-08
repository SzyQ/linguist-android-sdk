package io.stringx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class StringXLanguageReceiver {

    private final Set<OnLanguageChanged> listeners = new HashSet<>();

    private StringXLanguageReceiver(Context context) {
        context.registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        notifyLocaleChanged();
                    }
                },
                new IntentFilter(Intent.ACTION_LOCALE_CHANGED));
    }

    public static StringXLanguageReceiver from(Context context) {
        return new StringXLanguageReceiver(context);
    }


    public void addListener(OnLanguageChanged listener) {
        listeners.add(listener);
    }

    private void notifyLocaleChanged() {
        try {
            Language language = Language.fromLocale(Locale.getDefault());
            for (OnLanguageChanged listener : listeners) {
                listener.onLanguageChanged(language);
            }
        } catch (UnsupportedLanguageException ignored) {
        }

    }

    public interface OnLanguageChanged {
        void onLanguageChanged(Language language);
    }
}
