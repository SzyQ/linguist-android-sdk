package io.stringx;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import io.stringx.sdk.R;

public class StringXPreference extends android.preference.CheckBoxPreference {

    public StringXPreference(final Context context, AttributeSet attrs) {
        super(context, attrs);
        final StringX stringX = StringX.get(getContext());
        if (stringX == null) {
            setChecked(false);
            setEnabled(false);
            return;
        }
        try {
            boolean enabled = stringX.isEnabled();
            boolean translationAvailable = stringX.isTranslationAvailable() && !stringX.isForcingLocale();
            setChecked(enabled);
            setEnabled(translationAvailable);
        } catch (UnsupportedLanguageException e) {
            setEnabled(false);
        }
        setTitle(R.string.sX_preference_title);
        setSummary(R.string.sX_preference_summary);

        setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                Boolean isOptedIn = (Boolean) o;
                stringX.setEnabled(isOptedIn);
                stringX.restart();
                return true;
            }
        });

    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        try {
            StringX stringX = StringX.get(getContext());
            if (stringX != null) {
                super.onSetInitialValue(stringX.isEnabled(), false);
            }
        } catch (UnsupportedLanguageException ignored) {
        }
    }
}
