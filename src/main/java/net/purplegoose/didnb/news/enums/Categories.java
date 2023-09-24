package net.purplegoose.didnb.news.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Categories {
    HEARTSTONE("heartstone"),
    DI("di");

    public final String rawName;

    public static Categories getCategoriesByRawName(String rawName) {
        for (Categories value : values()) {
            if (value.rawName.equalsIgnoreCase(rawName)) {
                return value;
            }
        }
        return null;
    }
}