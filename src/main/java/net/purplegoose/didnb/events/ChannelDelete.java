package net.purplegoose.didnb.events;

import lombok.extern.slf4j.Slf4j;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Slf4j
public class ChannelDelete extends ListenerAdapter {

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
            log.info("TextChannel with ID {} was deleted. Removing from system.", textChannelID);
        }
    }

    private boolean isChannelTypeNotTextChannel(Channel channel) {
        return channel.getType().getId() != 0;
    }
}
