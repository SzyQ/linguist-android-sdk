package mobi.klimaszewski.linguist;

import java.util.Locale;

/**
 * Exception thrown when device {@link Locale} cannot be converted to {@link Language} thus not supported
 */
public class UnsupportedLanguageException extends Exception {

    public UnsupportedLanguageException(Locale locale) {
        super("Unsupported language: " + locale.getLanguage() + "-" + locale.getCountry());
    }

    public UnsupportedLanguageException(String code) {
        super("Unsupported language: " + code);
    }
}
