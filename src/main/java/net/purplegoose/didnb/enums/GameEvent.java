package net.purplegoose.didnb.enums;

public enum GameEvent {
    MESSAGE("message", "This is the actual event message. ... is starting now.", ""),
    HEAD_UP("headup", "This is the warn event message. ... is starting in ? minutes.", ""),
    ANCIENT_ARENA("ancientarena", "This is the event notification for Ancient Arena.", "ANCIENT-ARENA"),
    ANCIENT_NIGHTMARE("ancientnightmare", "This is the event notification for Ancient Nightmare.", "ANCIENT-NIGHTMARE"),
    ASSEMBLY("assembly", "This is the event notification for the Assembly.", "ASSEMBLY"),
    BATTLEGROUNDS("battlegrounds", "This is the event notification for Battlegrounds.", "BATTLEGROUND"),
    VAULT("vault", "This is the event notification for the Vault.", "VAULT"),
    DEMON_GATES("demongates", "This is the event notification for Demon Gates.", "DEMON-GATES"),
    SHADOW_LOTTERY("shadowlottery", "This is the event notification for the Shadow Lottery.", "SHADOW-LOTTERY"),
    HAUNTED_CARRIAGE("hauntedcarriage", "This is the event notification for Haunted Carriage.", "HAUNTED-CARRIAGE"),
    WRATHBORNE_INVASION("wrathborneinvasion", "This is the event notification for the Wrathborne Invasion.", "WRATHBORNE-INVASION"),
    ON_SLAUGHT("onslaught", "This is the event notification for Onslaught.", "ON-SLAUGHT"),
    TOWER_OF_VICTORY("towerofvictory", "This is the event notification for Tower of Victory.", "TOWER-OF-VICTORY"),
    SHADOW_WAR("shadowwar", "This is the event notification for the Shadow War.", "SHADOW-WAR"),

    ANCIENT_ARENA_EMBED("ancientarenaembed", "", ""),
    ANCIENT_NIGHTMARE_EMBED("ancientnightmareembed", "", ""),
    DEMON_GATES_EMBED("demongatesembed", "", ""),
    HAUNTED_CARRIAGE_EMBED("hauntedcarriageembed", "", "");

    public final String rawName;
    public final String description;
    public final String languageKey;

    GameEvent(String rawName, String description, String languageKey) {
        this.rawName = rawName;
        this.description = description;
        this.languageKey = languageKey;
    }

    public static GameEvent findGameEventByRawName(String rawName) {
        if (rawName.equalsIgnoreCase("stormpoint")) return ON_SLAUGHT;
        if (rawName.equalsIgnoreCase("accursedtower")) return TOWER_OF_VICTORY;
        for (GameEvent gameEvent : GameEvent.values()) {
            if (gameEvent.rawName.equalsIgnoreCase(rawName)) {
                return gameEvent;
            }
        }
        return null;
    }
}
