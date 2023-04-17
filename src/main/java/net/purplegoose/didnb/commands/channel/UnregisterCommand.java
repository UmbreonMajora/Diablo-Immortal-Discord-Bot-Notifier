package net.purplegoose.didnb.commands.channel;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Umbreon Majora
 * Allow's user to unregister a text channel.
 * Command: /unregister [Not required: targetchannel]
 */
@Slf4j
@AllArgsConstructor
public class UnregisterCommand implements IClientCommand {

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    @Override
    public void runCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        String guildID = logInfo.getGuildID();
        TextChannel targetTextChannel = getTargetTextChannel(event);
        String targetTextChannelID = targetTextChannel.getId();
        Language language = guildsCache.getGuildLanguage(guildID);

        if (!isChannelRegistered(guildID, targetTextChannelID)) {
            log.error("{} used /unregister. Error: Channel not registered. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), logInfo.getGuildName(), guildID, targetTextChannel.getName(), targetTextChannelID);
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CHANNEL-NOT-REGISTERED"));
            return;
        }

        guildsCache.getClientGuildByID(guildID).deleteNotificationChannelByID(targetTextChannelID);
        databaseRequests.deleteNotificationChannelByID(targetTextChannelID);

        log.info("{} used /unregister. Guild: {}({}). Channel: {}({})", logInfo.getExecutor(), logInfo.getGuildName(),
                guildID, targetTextChannel.getName(), targetTextChannelID);
        replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "CHANNEL-UNREGISTERED"), targetTextChannel.getAsMention()));
    }

    private boolean isChannelRegistered(String guildID, String textChannelID) {
        return guildsCache.getClientGuildByID(guildID).isChannelRegistered(textChannelID);
    }
}
