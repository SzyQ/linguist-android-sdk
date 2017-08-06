package io.stringx;

import java.util.List;
import java.util.Map;
import io.stringx.TranslationConfig;

interface TranslationInterface {

    TranslationConfig getConfig();

    void onTranslationCompleted(in Map translation);
}
