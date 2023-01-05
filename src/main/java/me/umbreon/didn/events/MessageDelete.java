package me.umbreon.didn.events;

import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.data.ClientGuild;
import me.umbreon.didn.database.DatabaseRequests;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageDelete extends ListenerAdapter implements IClientEvent {

    private final DatabaseRequests databaseRequests;
    private final GuildsCache guildsCache;

    public MessageDelete(DatabaseRequests databaseRequests, GuildsCache guildsCache) {
        this.databaseRequests = databaseRequests;
        this.guildsCache = guildsCache;
    }

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {
        if (isChannelTypeNotTextChannel(event.getChannel())) {
            return;
        }

        String messageID = event.getMessageId();
        String guildID = event.getGuild().getId();

        ClientGuild clientGuild = guildsCache.getClientGuildByID(guildID);
        if (clientGuild.doReactionRoleExist(messageID)) {
            databaseRequests.deleteReactionRoleByMessageID(messageID);
            clientGuild.deleteReactionRoleByMessageID(messageID);
        }
    }
}
