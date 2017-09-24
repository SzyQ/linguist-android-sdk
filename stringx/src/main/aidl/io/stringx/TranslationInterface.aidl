package io.stringX;

import java.util.Map;
import io.stringX.ConfigCallback;

interface TranslationInterface {

    void getConfig(ConfigCallback callback);

    void onTranslationCompleted(in Map translation);
}
