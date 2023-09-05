package net.purplegoose.didnb.cache;

import net.purplegoose.didnb.data.ScheduledEventsSetting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScheduledEventsSettingsCacheTest {

    private ScheduledEventsSettingsCache cache;

    @BeforeEach
    public void setUp() {
        cache = new ScheduledEventsSettingsCache();
    }

    @Test
    void testAddSettings() {
        ScheduledEventsSetting setting = createSampleSetting("guild123");
        cache.addSettings(setting);
        assertTrue(cache.contains("guild123"));
    }

    @Test
    void testContains() {
        assertFalse(cache.contains("guild456"));
        ScheduledEventsSetting setting = createSampleSetting("guild456");
        cache.addSettings(setting);
        assertTrue(cache.contains("guild456"));
    }

    @Test
    void testRemoveSettings() {
        ScheduledEventsSetting setting = createSampleSetting("guild789");
        cache.addSettings(setting);
        assertTrue(cache.contains("guild789"));
        cache.removeSettings("guild789");
        assertFalse(cache.contains("guild789"));
    }

    @Test
    void testUpdateSettings() {
        ScheduledEventsSetting initialSetting = createSampleSetting("guildABC");
        ScheduledEventsSetting updatedSetting = createSampleSetting("guildABC");
        updatedSetting.setAncientArenaEnabled(true);
        updatedSetting.setStormpointEnabled(true);

        cache.addSettings(initialSetting);
        assertFalse(cache.getScheduledEventsSettingsCache().stream()
                .filter(seSetting -> seSetting.getGuildID().equals("guildABC"))
                .findFirst().orElse(null).isAncientArenaEnabled());
        assertFalse(cache.getScheduledEventsSettingsCache().stream()
                .filter(seSetting -> seSetting.getGuildID().equals("guildABC"))
                .findFirst().orElse(null).isStormpointEnabled());

        cache.updateSettings(updatedSetting);
        assertTrue(cache.getScheduledEventsSettingsCache().stream()
                .filter(seSetting -> seSetting.getGuildID().equals("guildABC"))
                .findFirst().orElse(null).isAncientArenaEnabled());
        assertTrue(cache.getScheduledEventsSettingsCache().stream()
                .filter(seSetting -> seSetting.getGuildID().equals("guildABC"))
                .findFirst().orElse(null).isStormpointEnabled());
    }

    private ScheduledEventsSetting createSampleSetting(String guildID) {
        ScheduledEventsSetting setting = new ScheduledEventsSetting(guildID);
        setting.setGuildID(guildID);
        return setting;
    }
}
