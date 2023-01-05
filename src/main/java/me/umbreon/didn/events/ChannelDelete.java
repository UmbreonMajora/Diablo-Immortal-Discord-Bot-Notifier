package me.umbreon.didn.events;

import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.data.ClientGuild;
import me.umbreon.didn.database.DatabaseRequests;
import me.umbreon.didn.logger.FileLogger;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelDelete extends ListenerAdapter implements IClientEvent {

    private final Logger logger = LoggerFactory.getLogger(ChannelDelete.class);

    private final DatabaseRequests databaseRequests;
    private final GuildsCache guildsCache;

    public ChannelDelete(DatabaseRequests databaseRequests, GuildsCache guildsCache) {
        this.databaseRequests = databaseRequests;
        this.guildsCache = guildsCache;
    }

    @Override
    public void onChannelDelete(ChannelDeleteEvent event) {
        if (isChannelTypeNotTextChannel(event.getChannel())) {
            return;
        }

        String guildID = event.getGuild().getId();
        String textChannelID = event.getChannel().getId();
        ClientGuild clientGuild = guildsCache.getClientGuildByID(guildID);

        if (clientGuild.doNotificationChannelExist(textChannelID)) {
            databaseRequests.deleteNotificationChannelByID(textChannelID);
            clientGuild.deleteNotificationChannelByID(textChannelID);
            logger.info("TextChannel with ID " + textChannelID + " was deleted. Removing from system.");
            FileLogger.createClientFileLog("TextChannel with ID " + textChannelID + " was deleted. Removing from system.");
        }
    }
}
