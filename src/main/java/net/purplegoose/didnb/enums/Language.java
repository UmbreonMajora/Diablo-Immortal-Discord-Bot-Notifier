package net.purplegoose.didnb.enums;

import java.util.Arrays;

public enum Language {
    AMERICA_SPANISH("America-Spanish", "USA-ESP", "languages/america-spanish.yaml"),
    SPAIN_SPANISH("Spain-Spanish", "ESP", "languages/spain-spanish.yaml"),
    DANISH("Danish", "DAN", "languages/danish.yaml"),
    DUTCH("Dutch", "DUT", "languages/dutch.yaml"),
    SWEDISH("Swedish", "SWE", "languages/swedish.yaml"),
    FINNISH("Finnish", "FIN", "languages/finnish.yaml"),
    LATVIAN("Latvian", "LAT", "languages/latvian.yaml"),
    ROMANIAN("Romanian", "ROM", "languages/romanian.yaml"),
    BULGARIAN("Bulgarian", "BUL", "languages/bulgarian.yaml"),
    SLOVAK("Slovak", "SLO", "languages/slovak.yaml"),
    CZECH("Czech", "CZE", "languages/czech.yaml"),
    TURKISH("Turkish", "TUR", "languages/turkish.yaml"),
    GREEK("Greek", "GRE", "languages/greek.yaml"),
    ENGLISH("English", "ENG", "languages/english.yaml"),
    GERMAN("German", "GER", "languages/german.yaml"),
    FRENCH("French", "FRA", "languages/french.yaml"),
    INDONESIA("Indonesia", "IND", "languages/indonesia.yaml"),
    ITALIAN("Italian", "ITA", "languages/italian.yaml"),
    POLISH("Polish", "POL", "languages/polish.yaml"),
    UKRAINIAN("Ukrainian", "UKR", "languages/ukrainian.yaml"),
    RUSSIAN("Russia", "RUS", "languages/russian.yaml"),
    BRAZILIAN_PORTUGUESE("Brazilian-Portuguese", "BRPT", "languages/brazilian-portuguese.yaml"),
    CROATIAN("Croatian", "CRO", "languages/croatian.yaml");

    public final String rawName;
    public final String shortName;
    public final String path;

    Language(String rawName, String shortName, String path) {
        this.rawName = rawName;
        this.shortName = shortName;
        this.path = path;
    }

    public static Language findLanguageByShortName(String shortName) {
        for (Language language : Language.values()) {
            if (language.shortName.equalsIgnoreCase(shortName)) {
                return language;
            }
        }
        return null;
    }

    public static Language[] getAllLanguages() {
        System.out.println(Arrays.toString(Language.values()));
        return Language.values();
    }
}
