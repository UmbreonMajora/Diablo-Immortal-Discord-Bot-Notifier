package me.umbreon.didn.enums;

public enum Preset {
    PRESET_IMMORTAL("presetimmortal"),
    PRESET_SHADOW("presetshadow"),
    PRESET_OVERWORLD_ON("presetoverworldon"),
    PRESET_OVERWORLD_OFF("presetoverworldoff"),
    PRESET_DAILY_ON("presetdailyon"),
    PRESET_DAILY_OFF("presetdailyoff"),
    PRESET_LIST("listpreset");

    public final String getAsChoiceName;

    Preset(String label) {
        this.getAsChoiceName = label;
    }


}
