package net.purplegoose.didnb.languages;

import net.purplegoose.didnb.enums.Language;

public class LanguageController {

    private LanguageController() {
        // static use only
    }

    public static String getMessage(Language language, String message) {
        return switch (language) {
            case FRENCH -> LanguageFrench.getMessage(message);
            case GERMAN -> LanguageGerman.getMessage(message);
            case INDONESIA -> LanguageIndonesia.getMessage(message);
            case ITALIAN -> LanguageItalia.getMessage(message);
            case POLISH -> LanguagePolish.getMessage(message);
            case RUSSIAN -> LanguageRussian.getMessage(message);
            case UKRAINIAN -> LanguageUkrainian.getMessage(message);
            case BRAZILIAN_PORTUGUESE -> LanguageBrazilianPortuguese.getMessage(message);
            case SPAIN_SPANISH -> LanguageSpainSpanish.getMessage(message);
            case AMERICA_SPANISH -> LanguageAmericaSpanish.getMessage(message);
            default -> LanguageEnglish.getMessage(message);
        };
    }
}
