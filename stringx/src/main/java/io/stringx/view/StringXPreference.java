package io.stringx.view;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import io.stringx.Linguist;
import io.stringx.app.R;

public class StringXPreference extends android.preference.CheckBoxPreference {

    public StringXPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setKey("sX_preference_opt_in");
        setIcon(R.mipmap.sx_logo);
        setTitle(R.string.sX_preference_title);
        setSummary(R.string.sX_preference_summary);
        setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                Linguist linguist = Linguist.get(getContext());
                if (linguist != null) {
                    Boolean isOptedIn = (Boolean) o;
                    setValue(isOptedIn);
                    linguist.setOptOut(!isOptedIn);
                }
                return true;
            }
        });
    }

    private void setValue(Boolean isOptedIn) {
        getEditor().putBoolean(getKey(), isOptedIn).apply();
    }

}
