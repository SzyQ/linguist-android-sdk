package io.stringx.view;

import android.content.Context;
import android.content.Intent;
import android.preference.Preference;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;

import io.stringx.R;
import io.stringx.RestartBroadcast;
import io.stringx.StringXProxyActivity;
import io.stringx.Stringx;

public class StringXPreference extends android.preference.CheckBoxPreference {

    public StringXPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setKey("sX_preference_opt_in");
        setIcon(R.mipmap.sx_logo);
        setTitle(R.string.sX_preference_title);
        setSummary(R.string.sX_preference_summary);
        final Stringx stringx = Stringx.get(getContext());
        if (stringx != null && stringx.getOptions().getSupportedLanguages().contains(stringx.getDeviceLanguage())) {
            setEnabled(false);
        }
        setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                if (stringx != null) {
                    Boolean isOptedIn = (Boolean) o;
                    setValue(isOptedIn);
                    stringx.setOptOut(!isOptedIn);
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

    private void setValue(Boolean isOptedIn) {
        getEditor().putBoolean(getKey(), isOptedIn).apply();
    }

}
