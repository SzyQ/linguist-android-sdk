package io.stringx.view;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import io.stringx.StringX;
import io.stringx.UnsupportedLanguageException;
import io.stringx.client.R;

public class StringXPreference extends android.preference.CheckBoxPreference {

    public StringXPreference(final Context context, AttributeSet attrs) {
        super(context, attrs);
        final StringX stringX = StringX.get(getContext());
        if(stringX == null){
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
        setIcon(R.mipmap.sx_logo);
        setTitle(R.string.sX_preference_title);
        setSummary(R.string.sX_preference_summary);

        setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                Boolean isOptedIn = (Boolean) o;
                try {
                    stringX.setEnabled(isOptedIn);
                } catch (UnsupportedLanguageException ignored) {
                }
                stringX.restart();
                return true;
            }
        });

    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        try {
            StringX stringX = StringX.get(getContext());
            if(stringX != null){
                super.onSetInitialValue(stringX.isEnabled(), false);
            }
        } catch (UnsupportedLanguageException ignored) {
        }
    }
}
