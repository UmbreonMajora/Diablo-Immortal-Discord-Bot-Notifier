package net.purplegoose.didnb.enums;

public enum Preset {
    PRESET_IMMORTAL("presetimmortal", "Enable all immortal clan event messages."),
    PRESET_SHADOW("presetshadow", "Enable all shadow clan event messages."),
    PRESET_OVERWORLD_ON("presetoverworldon", "Enable all overworld event messages."),
    PRESET_OVERWORLD_OFF("presetoverworldoff", "Disable all overworld event messages."),
    PRESET_DAILY_ON("presetdailyon", "Enable all daily event messages."),
    PRESET_DAILY_OFF("presetdailyoff", "Disable all daily event messages."),
    PRESET_LIST("listpreset", "List's you the settings each preset applies to your channel.");

    public final String getAsChoiceName;
    public final String presetDescription;

    Preset(String label, String presetDescription) {
        this.getAsChoiceName = label;
        this.presetDescription = presetDescription;
    }

    public static Preset findPresetByChoiceName(String presetChoiceName) {
        for (Preset availablePreset : Preset.values()) {
            if (availablePreset.getAsChoiceName.equalsIgnoreCase(presetChoiceName)) {
                return availablePreset;
            }
        }
        return null;
    }
}
