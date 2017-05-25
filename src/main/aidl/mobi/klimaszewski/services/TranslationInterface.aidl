package mobi.klimaszewski.services;

import java.util.List;
import java.util.Map;
import mobi.klimaszewski.services.TranslationConfig;

interface TranslationInterface {

    TranslationConfig getConfig();

    void onTranslationCompleted(in Map translation);
}
