package io.stringx;

import java.util.Map;
import io.stringx.ConfigCallback;

interface TranslationInterface {

    void getConfig(ConfigCallback callback);

    void onTranslationCompleted(in Map translation);
}
