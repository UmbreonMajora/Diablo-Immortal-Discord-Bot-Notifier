package net.purplegoose.didnb.languages;

import net.purplegoose.didnb.enums.Language;

public class LanguageController {

    private LanguageController() {/*static use only*/}

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
            case DANISH -> LanguageDanish.getMessage(message);
            case DUTCH -> LanguageDutch.getMessage(message);
            case FINNISH -> LanguageFinnish.getMessage(message);
            case SWEDISH -> LanguageSwedish.getMessage(message);
            case LATVIAN -> LanguageLatvian.getMessage(message);
            case ROMANIAN -> LanguageRomanian.getMessage(message);
            case BULGARIAN -> LanguageBulgarian.getMessage(message);
            case SLOVAK -> LanguageSlovak.getMessage(message);
            case CZECH -> LanguagesCzech.getMessage(message);
            case TURKISH -> LanguageTurkish.getMessage(message);
            case GREEK -> LanguageGreek.getMessage(message);
            default -> LanguageEnglish.getMessage(message);
        };
    }
}
