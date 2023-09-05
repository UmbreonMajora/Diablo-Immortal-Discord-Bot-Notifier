package net.purplegoose.didnb.events;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.database.DatabaseRequests;

@Slf4j
@AllArgsConstructor
public class GuildLeave extends ListenerAdapter {
    private final DatabaseRequests databaseRequests;
    private final GuildsCache guildsCache;

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        String guildID = event.getGuild().getId();
        deleteDataFromDatabase(guildID);
        guildsCache.getAllGuilds().remove(guildID);
        log.info(event.getGuild().getId() + " where removed from guilds.");
    }

    private void deleteDataFromDatabase(String guildID) {
        databaseRequests.deleteGuildByGuildID(guildID);
        databaseRequests.deleteMessagesByGuildID(guildID);
        databaseRequests.deleteChannelsByGuildID(guildID);
        databaseRequests.deleteScheduledEventsSettings(guildID);
    }
}
