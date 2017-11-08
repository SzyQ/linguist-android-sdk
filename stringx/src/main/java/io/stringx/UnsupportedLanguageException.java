package io.stringx;

import java.util.Locale;

class UnsupportedLanguageException extends Exception {

    public UnsupportedLanguageException(Locale locale) {
        super("Unsupported language: " + locale.getLanguage() + "-" + locale.getCountry());
    }

    public UnsupportedLanguageException(String code) {
        super("Unsupported language: " + code);
    }
}
