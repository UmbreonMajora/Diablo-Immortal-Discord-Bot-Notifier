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
        name = "registernews",
        description = "Registers channel this command is sent in as news channel.",
        usage = "/registernews"
)
public class RegisterNewsChannelCommand extends Command {
    public static final String COMMAND = "registernews";

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
        if (isChannelAlreadyRegistered(guildID, channelID)) {
            replyToUser(event, REGISTER_FAILURE, true);
            String user = logInfo.getExecutor();
            createErrorLogEntry(logInfo, this.getClass(), String.format(FAILURE_LOG_MESSAGE, user, channelID, guildID));
            return;
        }

        if (registerNewNewsChannel(channelID, guildID)) {
            replyToUser(event, REGISTER_SUCCESS, true);
            createUseLogEntry(logInfo, this.getClass());
        } else {
            replyToUser(event, "Failed to register news channel to database. Please try again.", true);
            String msg = String.format("%s tried to register a news channel with id %s in guild with id %s, but it " +
                    "failed because it couldn't be added to the database.", logInfo.getExecutor(), channelID, guildID);
            createErrorLogEntry(logInfo, this.getClass(), msg);
        }
    }

    private boolean registerNewNewsChannel(String textChannelID, String guildID) {
        NewsChannelDTO newsChannelDTO = new NewsChannelDTO(textChannelID, guildID);
        if (databaseRequests.insertNewsChannel(newsChannelDTO)) {
            guildsCache.getClientGuildByID(guildID).addNewsChannel(newsChannelDTO);
            return true;
        }
        return false;
    }

    private boolean isChannelAlreadyRegistered(String guildID, String channelID) {
        Map<String, NewsChannelDTO> guildNewsChannels = guildsCache.getClientGuildByID(guildID).getNewsChannels();
        return guildNewsChannels.containsKey(channelID);
    }
}