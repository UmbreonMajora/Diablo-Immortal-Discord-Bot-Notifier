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
        name = "registernews",
        description = "Registers channel this command is sent in as news channel.",
        usage = "/registernews"
)
public class RegisterNewsChannelCommand extends Command {
    public static final String COMMAND = "registernews";

    public static final SlashCommandData commandData;

    static {
        commandData = Commands.slash(COMMAND, "Registers channel this command is sent in as news channel.");
    }

    //todo: add to localization
    private static final String FAILURE_LOG_MESSAGE = "%s tried to register channel with id %s in " +
            "guild with id %s as news channel, but it failed because the channel is already registered";
    private static final String REGISTER_FAILURE = "Failed to register %s as news channel, it is already registered.";
    private static final String REGISTER_SUCCESS = "Successfully registered %s as a news channel.";

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    @Override
    public void performCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        String guildID = logInfo.getGuildID();
        String channelID = logInfo.getChannelID();
        String textChannelName = event.getChannel().getName();

        if (isChannelAlreadyRegistered(guildID, channelID)) {
            replyFailureToUser(textChannelName, event);
            logFailure(logInfo);
            return;
        }

        registerNewNewsChannel(channelID, guildID);
        replySuccessToUser(textChannelName, event);
        createUseLogEntry(logInfo, this.getClass());
    }

    private void logFailure(LoggingInformation logInfo) {
        String user = logInfo.getExecutor();
        String channelID = logInfo.getChannelID();
        String guildID = logInfo.getGuildID();
        String replyMessage = String.format(FAILURE_LOG_MESSAGE, user, channelID, guildID);
        createErrorLogEntry(logInfo, this.getClass(), replyMessage);
    }

    private void replySuccessToUser(String textChannelName, SlashCommandInteractionEvent event) {
        String replyMessage = String.format(REGISTER_SUCCESS, textChannelName);
        replyWithMessageToUser(event, replyMessage, true);
    }

    private void replyFailureToUser(String textChannelName, SlashCommandInteractionEvent event) {
        String replyMessage = String.format(REGISTER_FAILURE, textChannelName);
        replyWithMessageToUser(event, replyMessage, true);
    }

    private void registerNewNewsChannel(String textChannelID, String guildID) {
        NewsChannelDTO newsChannelDTO = new NewsChannelDTO(textChannelID, guildID);
        databaseRequests.addNewsChannel(newsChannelDTO);
        guildsCache.getClientGuildByID(guildID).addNewsChannel(newsChannelDTO);
    }

    private boolean isChannelAlreadyRegistered(String guildID, String channelID) {
        Map<String, NewsChannelDTO> guildNewsChannels = guildsCache.getClientGuildByID(guildID).getNewsChannels();
        return guildNewsChannels.containsKey(channelID);
    }
}