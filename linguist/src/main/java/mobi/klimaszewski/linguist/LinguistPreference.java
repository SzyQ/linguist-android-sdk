package mobi.klimaszewski.linguist;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import mobi.klimaszewski.linguist.R;

/**
 * Stores {@link Linguist} settings
 */
public class LinguistPreference extends android.preference.CheckBoxPreference {

    public LinguistPreference(final Context context, AttributeSet attrs) {
        super(context, attrs);
        final Linguist linguist = Linguist.get(getContext());
        if (linguist == null) {
            setChecked(false);
            setEnabled(false);
            return;
        }
        try {
            boolean enabled = linguist.isEnabled();
            boolean translationAvailable = linguist.isTranslationAvailable() && !linguist.isForcingLocale();
            setChecked(enabled);
            setEnabled(translationAvailable);
        } catch (UnsupportedLanguageException e) {
            setEnabled(false);
        }
        setTitle(R.string.ls_preference_title);
        setSummary(R.string.ls_preference_summary);

        setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                Boolean isOptedIn = (Boolean) o;
                linguist.setEnabled(isOptedIn);
                linguist.restart();
                return true;
            }
        });

    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        try {
            Linguist linguist = Linguist.get(getContext());
            if (linguist != null) {
                super.onSetInitialValue(linguist.isEnabled(), false);
            }
        } catch (UnsupportedLanguageException ignored) {
        }
    }
}
