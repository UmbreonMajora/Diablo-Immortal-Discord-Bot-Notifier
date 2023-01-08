package me.umbreon.didn.events;

import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.database.DatabaseRequests;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuildLeave extends ListenerAdapter {

    private final Logger logger = LoggerFactory.getLogger(GuildLeave.class);

    private final DatabaseRequests databaseRequests;
    private final GuildsCache guildsCache;

    public GuildLeave(DatabaseRequests databaseRequests, GuildsCache guildsCache) {
        this.databaseRequests = databaseRequests;
        this.guildsCache = guildsCache;
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        String guildID = event.getGuild().getId();
        databaseRequests.deleteGuildByGuildID(guildID);
        databaseRequests.deleteMessagesByGuildID(guildID);
        databaseRequests.deleteChannelsByGuildID(guildID);
        guildsCache.getAllGuilds().remove(guildID);
        logger.info(event.getGuild().getId() + " where removed from guilds.");
    }
}
