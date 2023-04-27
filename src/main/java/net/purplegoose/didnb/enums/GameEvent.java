package net.purplegoose.didnb.enums;

public enum GameEvent {
    MESSAGE("message", "This is the actual event message. ... is starting now."),
    HEAD_UP("headup", "This is the warn event message. ... is starting in ? minutes."),
    ANCIENT_ARENA("ancientarena", "This is the event notification for Ancient Arena."),
    ANCIENT_NIGHTMARE("ancientnightmare", "This is the event notification for Ancient Nightmare."),
    ASSEMBLY("assembly", "This is the event notification for the Assembly."),
    BATTLEGROUNDS("battlegrounds", "This is the event notification for Battlegrounds."),
    VAULT("vault", "This is the event notification for the Vault."),
    DEMON_GATES("demongates", "This is the event notification for Demon Gates."),
    SHADOW_LOTTERY("shadowlottery", "This is the event notification for the Shadow Lottery."),
    HAUNTED_CARRIAGE("hauntedcarriage", "This is the event notification for Haunted Carriage."),
    WRATHBORNE_INVASION("wrathborneinvasion", "This is the event notification for the Wrathborne Invasion."),
    ON_SLAUGHT("onslaught", "This is the event notification for Onslaught."),
    TOWER_OF_VICTORY("towerofvictory", "This is the event notification for Tower of Victory ."),
    SHADOW_WAR("shadowwar", "This is the event notification for the Shadow War.");

    public final String rawName;
    public final String description;

    GameEvent(String rawName, String description) {
        this.rawName = rawName;
        this.description = description;
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
