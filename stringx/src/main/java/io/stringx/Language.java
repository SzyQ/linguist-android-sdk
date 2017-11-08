package io.stringx;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.Locale;

/**
 * https://cloud.google.com/translate/docs/languages
 */
public enum Language {

    Afrikaans("af"),
    Albanian("sq"),
    Amharic("am"),
    Arabic("ar"),
    Armenian("hy"),
    Azeerbaijani("az"),
    Basque("eu"),
    Belarusian("be"),
    Bengali("bn"),
    Bosnian("bs"),
    Bulgarian("bg"),
    Catalan("ca"),
    Cebuano("ceb"),
    Chichewa("ny"),
    Chinese_Simplified("zh-CN"),
    Chinese_Traditional("zh-TW"),
    Corsican("co"),
    Croatian("hr"),
    Czech("cs"),
    Danish("da"),
    Dutch("nl"),
    English("en"),
    Esperanto("eo"),
    Estonian("et"),
    Filipino("tl"),
    Finnish("fi"),
    French("fr"),
    Frisian("fy"),
    Galician("gl"),
    Georgian("ka"),
    German("de"),
    Greek("el"),
    Gujarati("gu"),
    Haitian_Creole("ht"),
    Hausa("ha"),
    Hawaiian("haw"),
    Hebrew("iw"),
    Hindi("hi"),
    Hmong("hmn"),
    Hungarian("hu"),
    Icelandic("is"),
    Igbo("ig"),
    Indonesian("id"),
    Irish("ga"),
    Italian("it"),
    Japanese("ja"),
    Javanese("jw"),
    Kannada("kn"),
    Kazakh("kk"),
    Khmer("km"),
    Korean("ko"),
    Kurdish("ku"),
    Kyrgyz("ky"),
    Lao("lo"),
    Latin("la"),
    Latvian("lv"),
    Lithuanian("lt"),
    Luxembourgish("lb"),
    Macedonian("mk"),
    Malagasy("mg"),
    Malay("ms"),
    Malayalam("ml"),
    Maltese("mt"),
    Maori("mi"),
    Marathi("mr"),
    Mongolian("mn"),
    Burmese("my"),
    Nepali("ne"),
    Norwegian("no"),
    Pashto("ps"),
    Persian("fa"),
    Polish("pl"),
    Portuguese("pt"),
    //FIXME Google Translate API has some problems with this one Punjabi("ma"),
    Romanian("ro"),
    Russian("ru"),
    Samoan("sm"),
    Scots_Gaelic("gd"),
    Serbian("sr"),
    Sesotho("st"),
    Shona("sn"),
    Sindhi("sd"),
    Sinhala("si"),
    Slovak("sk"),
    Slovenian("sl"),
    Somali("so"),
    Spanish("es"),
    Sundanese("su"),
    Swahili("sw"),
    Swedish("sv"),
    Tajik("tg"),
    Tamil("ta"),
    Telugu("te"),
    Thai("th"),
    Turkish("tr"),
    Ukrainian("uk"),
    Urdu("ur"),
    Uzbek("uz"),
    Vietnamese("vi"),
    Welsh("cy"),
    Xhosa("xh"),
    Yiddish("yi"),
    Yoruba("yo"),
    Zulu("zu");

    private String code;

    Language(String code) {
        this.code = code;
    }

    @NonNull
    public static Language fromCode(String code) throws UnsupportedLanguageException {
        for (Language languages : values()) {
            if (code.equals(languages.getCode())) {
                return languages;
            }
        }
        throw new UnsupportedLanguageException(code);
    }

    @NonNull
    public static Language fromLocale(Locale locale) throws UnsupportedLanguageException {
        String language = locale.getLanguage();
        String country = locale.getCountry();
        if (language.equals("zh") && !TextUtils.isEmpty(country)) {
            switch (country) {
                case "TW":
                    return Chinese_Traditional;
                case "CN":
                    return Chinese_Simplified;
                default:
                    throw new UnsupportedLanguageException(locale);
            }
        }
        return fromCode(language);
    }

    @NonNull
    public Locale toLocale() {
        switch (this) {
            case Chinese_Simplified:
                return new Locale("zh", "CN");
            case Chinese_Traditional:
                return new Locale("zh", "TW");
            default:
                return new Locale(getCode());
        }
    }

    public String getCode() {
        return code;
    }

}
