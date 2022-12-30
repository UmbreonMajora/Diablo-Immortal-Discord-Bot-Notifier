package me.umbreon.didn.enums;

public enum ServerSetting {
    HEAD_UP("headup"),
    MESSAGE("messaage"),
    WARN_TIME("Warn time");

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
