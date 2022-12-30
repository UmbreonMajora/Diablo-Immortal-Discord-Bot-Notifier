package me.umbreon.didn.events;

import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.data.ClientGuild;
import me.umbreon.didn.database.DatabaseRequests;
import me.umbreon.didn.logger.FileLogger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelDelete extends ListenerAdapter implements IClientEvent {

    private final Logger LOGGER = LoggerFactory.getLogger(ChannelDelete.class);

    private final DatabaseRequests databaseRequests;
    private final GuildsCache guildsCache;

    public ChannelDelete(DatabaseRequests databaseRequests, GuildsCache guildsCache) {
        this.databaseRequests = databaseRequests;
        this.guildsCache = guildsCache;
    }

    @Override
    public void onChannelDelete(ChannelDeleteEvent event) {
        if (!isChannelTypeTextChannelType(event.getChannel())) {
            return;
        }

        Guild guild = event.getGuild();
        String guildID = guild.getId();

        if (!guildsCache.isGuildRegistered(guildID)) {
            LOGGER.info(guild + " is not registered. Registering...");
            FileLogger.createClientFileLog(guild + " is not registered. Registering...");
            ClientGuild clientGuild = new ClientGuild(guildID);
            guildsCache.addGuild(clientGuild);
            databaseRequests.createGuild(clientGuild);
        }

        String textChannelID = event.getChannel().getId();

        ClientGuild clientGuild = guildsCache.getClientGuildByID(guildID);
        if (clientGuild.doNotificationChannelExist(textChannelID)) {
            databaseRequests.deleteNotificationChannelByID(textChannelID);
            clientGuild.deleteNotificationChannelByID(textChannelID);
        }
    }
}
