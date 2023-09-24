package net.purplegoose.didnb.news.commands;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.purplegoose.didnb.annotations.CommandAnnotation;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.Command;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.news.dto.NewsChannelDTO;

import java.util.Map;

@AllArgsConstructor
@CommandAnnotation(
        name = "unregisternews",
        description = "Unregisters channel this command is sent in as news channel",
        usage = "/unregisternews"
)
public class UnregisterNewsChannelCommand extends Command {
    public static final String COMMAND = "unregisternews";

    private static final String FAILURE_LOG_MESSAGE = "%s tried to unregister channel with id %s in " +
            "guild with id %s as news channel, but it failed because the channel is not registered.";

    private static final String UNREGISTER_FAILURE = "Failed to unregister %s, this channel is not registered.";
    private static final String UNREGISTER_SUCCESS = "Successfully unregistered %s. Channel will no longer receive any news.";

    private final DatabaseRequests databaseRequests;
    private final GuildsCache guildsCache;

    @Override
    public void performCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        String guildID = logInfo.getGuildID();
        String channelID = logInfo.getChannelID();
        if (isChannelNotRegistered(guildID, channelID)) {
            replyToUser(event, UNREGISTER_FAILURE, true);
            String user = event.getUser().getName();
            createErrorLogEntry(logInfo, this.getClass(), String.format(FAILURE_LOG_MESSAGE, user, channelID, guildID));
            return;
        }

        replyToUser(event, UNREGISTER_SUCCESS, true);
        createUseLogEntry(logInfo, this.getClass());
        registerNewNewsChannel(channelID, guildID);
    }


    private void registerNewNewsChannel(String textChannelID, String guildID) {
        NewsChannelDTO newsChannelDTO = new NewsChannelDTO(textChannelID, guildID);
        guildsCache.getClientGuildByID(guildID).addNewsChannel(newsChannelDTO);
        databaseRequests.insertNewsChannel(newsChannelDTO);
    }

    private boolean isChannelNotRegistered(String guildID, String channelID) {
        Map<String, NewsChannelDTO> guildNewsChannels = guildsCache.getClientGuildByID(guildID).getNewsChannels();
        return !guildNewsChannels.containsKey(channelID);
    }
}