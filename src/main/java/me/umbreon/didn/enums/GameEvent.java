package me.umbreon.didn.enums;

public enum GameEvent {
    MESSAGE("message"),
    HEAD_UP("headup"),
    ANCIENT_ARENA("ancientarena"),
    ANCIENT_NIGHTMARE("ancientnightmare"),
    ASSEMBLY("assembly"),
    BATTLEGROUNDS("battlegrounds"),
    VAULT("vault"),
    DEMON_GATES("demongates"),
    SHADOW_LOTTERY("shadowlottery"),
    HAUNTED_CARRIAGE("hauntedcarriage"),
    WRATHBORNE_INVASION("wrathborneinvasion"),
    ON_SLAUGHT("onslaught");

    public final String rawName;

    GameEvent(String rawName) {
        this.rawName = rawName;
    }

    public static GameEvent findGameEventByRawName(String rawName) {
        for (GameEvent gameEvent : GameEvent.values()) {
            if (gameEvent.rawName.equalsIgnoreCase(rawName)) {
                return gameEvent;
            }
        }
        return null;
    }
}
