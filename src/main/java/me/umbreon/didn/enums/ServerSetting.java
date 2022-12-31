package me.umbreon.didn.enums;

public enum ServerSetting {
    WARN_MESSAGES("warnmessages"),
    EVENT_MESSAGES("eventmessages");

    public final String rawName;

    ServerSetting(String label) {
        this.rawName = label;
    }

    public static ServerSetting findServerSettingByRawName(String rawName) {
        for (ServerSetting serverSetting : ServerSetting.values()) {
            if (serverSetting.rawName.equalsIgnoreCase(rawName)) {
                return serverSetting;
            }
        }
        return null;
    }
}
