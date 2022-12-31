package me.umbreon.didn.enums;

public enum Weekday {
    MONDAY("monday", "Monday"),
    TUESDAY("tuesday", "Tuesday"),
    WEDNESDAY("wednesday", "Wednesday"),
    THURSDAY("thursday", "Thursday"),
    FRIDAY("friday", "Friday"),
    SATURDAY("saturday", "Saturday"),
    SUNDAY("sunday", "Sunday"),
    EVERYDAY("everyday", "Everyday");

    public final String rawName;
    public final String name;

    Weekday(String rawName, String name) {
        this.rawName = rawName;
        this.name = name;
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
