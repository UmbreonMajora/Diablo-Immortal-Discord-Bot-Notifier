package net.purplegoose.didnb.news.commands;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
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
        description = "Unregisters channel this command is sent in as news channel.",
        usage = "/unregisternews"
)
public class UnregisterNewsChannelCommand extends Command {
    public static final String COMMAND = "unregisternews";

    public static final SlashCommandData commandData;

    static {
        commandData = Commands.slash(COMMAND, "Unregisters channel this command is sent in as news channel.");
    }

    //todo: add to localization
    private static final String FAILURE_LOG_MESSAGE = "%s tried to unregister channel with id %s in " +
            "guild with id %s as news channel, but it failed because the channel is not registered.";
    private static final String UNREGISTER_FAILURE = "Failed to unregister %s, this channel is not registered.";
    private static final String UNREGISTER_SUCCESS = "Successfully unregistered %s. Channel will no longer receive " +
            "any news.";

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    @Override
    public void performCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        String guildID = logInfo.getGuildID();
        String channelID = logInfo.getChannelID();
        String textChannelName = event.getChannel().getName();

        if (isChannelNotRegistered(guildID, channelID)) {
            replyFailureToUser(textChannelName, event);
            logFailure(logInfo);
            return;
        }

        replySuccessToUser(textChannelName, event);
        createUseLogEntry(logInfo, this.getClass());
        unregisterNewsChannel(channelID, guildID);
    }

    private void logFailure(LoggingInformation logInfo) {
        String user = logInfo.getExecutor();
        String channelID = logInfo.getChannelID();
        String guildID = logInfo.getGuildID();
        String replyMessage = String.format(FAILURE_LOG_MESSAGE, user, channelID, guildID);
        createErrorLogEntry(logInfo, this.getClass(), replyMessage);
    }

    private void replySuccessToUser(String textChannelName, SlashCommandInteractionEvent event) {
        String replyMessage = String.format(UNREGISTER_SUCCESS, textChannelName);
        replyWithMessageToUser(event, replyMessage, true);
    }

    private void replyFailureToUser(String textChannelName, SlashCommandInteractionEvent event) {
        String replyMessage = String.format(UNREGISTER_FAILURE, textChannelName);
        replyWithMessageToUser(event, replyMessage, true);
    }

    private void unregisterNewsChannel(String channelID, String guildID) {
        guildsCache.getClientGuildByID(guildID).deleteNewsChannel(channelID);
        databaseRequests.deleteNewsChannelByID(channelID);
    }

    private boolean isChannelNotRegistered(String guildID, String channelID) {
        Map<String, NewsChannelDTO> guildNewsChannels = guildsCache.getClientGuildByID(guildID).getNewsChannels();
        return !guildNewsChannels.containsKey(channelID);
    }
}