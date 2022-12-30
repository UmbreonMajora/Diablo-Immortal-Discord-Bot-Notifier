package me.umbreon.didn.enums;

public enum Weekday {
    MONDAY("monday"),
    TUESDAY("tuesday"),
    WEDNESDAY("wednesday"),
    THURSDAY("thursday"),
    FRIDAY("fryday"),
    SATURDAY("saturday"),
    SUNDAY("sunday"),
    EVERYDAY("everyday");

    public final String rawName;

    Weekday(String rawName) {
        this.rawName = rawName;
    }

    public static Weekday findWeekdayByRawName(String rawName) {
        for (Weekday weekday : Weekday.values()) {
            if (weekday.rawName.equalsIgnoreCase(rawName)) {
                return weekday;
            }
        }
        return null;
    }
}
