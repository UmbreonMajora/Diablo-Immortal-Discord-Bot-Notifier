package net.purplegoose.didnb.cache;

import net.purplegoose.didnb.data.ScheduledEventsSetting;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ScheduledEventsSettingsCache {

    private Set<ScheduledEventsSetting> scheduledEventsSettingsCache = new HashSet<>();

    public void addSettings(ScheduledEventsSetting seSetting) {
        scheduledEventsSettingsCache.add(seSetting);
    }

    public boolean contains(String guildID) {
        return scheduledEventsSettingsCache.stream().anyMatch(seSetting -> seSetting.getGuildID().equals(guildID));
    }

    public void removeSettings(String guildID) {
        scheduledEventsSettingsCache = scheduledEventsSettingsCache.stream()
                .filter(seSetting -> !seSetting.getGuildID().equals(guildID)).collect(Collectors.toSet());
    }

    public void updateSettings(ScheduledEventsSetting seSettings) {
        scheduledEventsSettingsCache = scheduledEventsSettingsCache.stream()
                .map(seSetting -> seSetting.getGuildID().equalsIgnoreCase(seSettings.getGuildID()) ? seSettings : seSetting)
                .collect(Collectors.toSet());
    }

    Set<ScheduledEventsSetting> getScheduledEventsSettingsCache() {
        return scheduledEventsSettingsCache;
    }
}
