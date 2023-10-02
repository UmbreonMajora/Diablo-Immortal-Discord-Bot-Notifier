package net.purplegoose.didnb.news.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Categories {
    HEARTHSTONE("hearthstone", "Heartstone"),
    DI("di", "Diablo Immortal"),
    OVERWATCH("overwatch", "Overwatch"),
    WOW_WEB("wow-web", "WOW-Web"),
    BNET("bnet", "BattleNET"),
    CORTEZ("cortez", "Call of Duty: Modern Warfare II"),
    D4("d4", "Diablo 4"),
    HEROES("heroes", "Heroes of the storm"),
    NEWS("news", "Inside Blizzard");

    public final String rawName;
    public final String fullName;

    public static Categories getCategoriesByRawName(String rawName) {
        for (Categories value : values()) {
            if (value.rawName.equalsIgnoreCase(rawName)) {
                return value;
            }
        }
        return null;
    }
}