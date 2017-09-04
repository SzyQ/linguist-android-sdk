package io.stringx;

import java.util.List;
import java.util.Map;
import io.stringx.TranslationConfig;
import io.stringx.StringResource;

interface ConfigCallback {
    void onConfigCreated(out TranslationConfig confiog);
}
