package io.stringx.translate;

import android.preference.Preference;
import android.view.Menu;
import android.view.View;

public interface Translator {

    String translate(String string);

    View translate(View view);

    Preference translate(Preference preference);

    void translate(Menu menu);

    CharSequence translate(CharSequence text);

    CharSequence[] translate(CharSequence[] textArray);

    String[] translate(String[] textArray);
}
