package mobi.klimaszewski.linguist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Receives changes of Locale on device
 * Used to notify {@link Linguist} about new Locale to switch to
 */
public class LinguistLanguageReceiver {

    private final Set<OnLanguageChanged> listeners = new HashSet<>();

    private LinguistLanguageReceiver(Context context) {
        context.registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        notifyLocaleChanged();
                    }
                },
                new IntentFilter(Intent.ACTION_LOCALE_CHANGED));
    }

    public static LinguistLanguageReceiver from(Context context) {
        return new LinguistLanguageReceiver(context);
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
