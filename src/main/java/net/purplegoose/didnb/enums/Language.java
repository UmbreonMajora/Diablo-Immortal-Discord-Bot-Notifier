package net.purplegoose.didnb.enums;

public enum Language {
    ENGLISH("English", "ENG", "languages/english.yml"),
    GERMAN("German", "GER", "languages/german.yaml"),
    FRENCH("French", "FRA", "languages/french.yaml"),
    INDONESIA("Indonesia", "IND", "languages/indonesia.yaml"),
    ITALIAN("Italian", "ITA", "languages/italian.yaml"),
    POLISH("Polish", "POL", "languages/polish.yaml"),
    UKRAINIAN("Ukrainian", "UKR", "languages/ukrainian.yaml"),
    RUSSIAN("Russia", "RUS", "languages/russian.yaml"),
    BRAZILIAN_PORTUGUESE("Brazilian-Portuguese", "BRPT", "languages/brazilian-portuguese.yaml"),
    AMERICA_SPANISH("America-Spanish", "USA-ESP", "languages/america-spanish.yaml"),
    SPAIN_SPANISH("Spain-Spanish", "ESP", "languages/spain-spanish.yaml");

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
}
