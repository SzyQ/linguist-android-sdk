package mobi.klimaszewski.linguist;

import java.util.List;

public interface TranslationsFactory {
    List<String> translate(List<String> text, String fromCode, String toCode);

    void reply(String packageName, int charactersCount, String name);
}
