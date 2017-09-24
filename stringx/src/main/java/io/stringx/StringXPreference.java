package io.stringx;

import android.content.Context;
import android.content.Intent;
import android.preference.Preference;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;

import io.stringx.client.R;

public class StringXPreference extends android.preference.CheckBoxPreference {

    public StringXPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setKey(StringX.KEY_ENABLED);
        final StringX stringX = StringX.get(getContext());
        if (stringX != null) {
            setDefaultValue(stringX.isEnabled());
        }
        setIcon(R.mipmap.sx_logo);
        setTitle(R.string.sX_preference_title);
        setSummary(R.string.sX_preference_summary);
        setEnabled(isStringXAvailable(stringX));
        setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                if (stringX != null) {
                    Boolean isOptedIn = (Boolean) o;
                    setValue(isOptedIn);
                    stringX.setEnabled(isOptedIn);
                    if (isOptedIn) {
                        StringXProxyActivity.start(getContext());
                    } else {
                        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(RestartBroadcast.ACTION));
                    }
                }
                return true;
            }
        });

    }

    private boolean isStringXAvailable(StringX stringX) {
        return stringX != null && !stringX.getOptions().getSupportedLanguages().contains(stringX.getDeviceLanguage());
    }

    private void setValue(Boolean isOptedIn) {
        getEditor().putBoolean(getKey(), isOptedIn).apply();
    }

}
