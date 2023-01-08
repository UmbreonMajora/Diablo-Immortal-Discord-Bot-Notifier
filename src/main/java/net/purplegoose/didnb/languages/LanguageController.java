package net.purplegoose.didnb.languages;

import net.purplegoose.didnb.enums.Language;

public class LanguageController {

    public static String getMessage(Language language, String message) {
        switch (language) {
            case SPAIN:
                return LanguageSpain.getMessage(message);
            case FRENCH:
                return LanguageFrench.getMessage(message);
            case GERMAN:
                return LanguageGerman.getMessage(message);
            case INDONESIA:
                return LanguageIndonesia.getMessage(message);
            case ITALIAN:
                return LanguageItalia.getMessage(message);
            case POLISH:
                return LanguagePolish.getMessage(message);
            case RUSSIAN:
                return LanguageRussian.getMessage(message);
            case UKRAINIAN:
                return LanguageUkrainian.getMessage(message);
            case BRAZILIAN_PORTUGUESE:
                return LanguageBrazilianPortuguese.getMessage(message);
            default:
                return LanguageEnglish.getMessage(message);
        }
    }
}
