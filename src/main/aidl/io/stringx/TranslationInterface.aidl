package io.stringx;

import java.util.List;
import java.util.Map;
import io.stringx.TranslationConfig;
import io.stringx.ConfigCallback;
import io.stringx.StringResource;

interface TranslationInterface {

    void getConfig(ConfigCallback callback);

    void onTranslationCompleted(in Map translation);
}
