package mobi.klimaszewski.services;

import java.util.List;
import java.util.Map;

interface TranslationInterface {

    List<String> onStringsRequested();

    void onTranslationCompleted(in Map translation);
}
