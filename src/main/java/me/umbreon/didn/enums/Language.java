package me.umbreon.didn.enums;

public enum Language {
    ENGLISH("English", "ENG", "languages/english.yaml"),
    GERMAN("German", "GER", "languages/french.yaml"),
    FRENCH("French", "FRA", "languages/german.yaml"),
    INDONESIA("Indonesia", "IND", "languages/indonesia.yaml"),
    ITALIAN("Italian", "ITA", "languages/italian.yaml"),
    POLISH("Polish", "POL", "languages/polish.yaml"),
    SPAIN("Spain", "ESP", "languages/spain.yaml"),
    UKRAINIAN("Ukrainian", "UKR", "languages/ukrainian.yaml"),
    RUSSIAN("Russia", "RUS", "languages/russian.yaml"),
    BRAZILIAN_PORTUGUESE("Brazilian-Portuguese", "BRPT", "languages/brazilian-portuguese.yaml");

    public final String rawName;
    public final String shortName;
    public final String path;

    Language(String rawName, String shortName, String path) {
        this.rawName = rawName;
        this.shortName = shortName;
        this.path = path;
    }

    public static Language findLanguageByRawName(String rawName) {
        for (Language language : Language.values()) {
            if (language.rawName.equalsIgnoreCase(rawName)) {
                return language;
            }
        }
        return null;
    }

    public static Language findLanguageByShortName(String shortName) {
        for (Language language : Language.values()) {
            if (language.shortName.equalsIgnoreCase(shortName)) {
                return language;
            }
        }
        return null;
    }
}
