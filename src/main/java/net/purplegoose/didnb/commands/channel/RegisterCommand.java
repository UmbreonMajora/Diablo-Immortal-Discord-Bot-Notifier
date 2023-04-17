package net.purplegoose.didnb.commands.channel;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.data.NotificationChannel;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Objects;

/**
 * @author Umbreon Majora
 * Allow's user to register a text channel as notification channel.
 * Command: /register [Not required: targetchannel]
 */
@Slf4j
@AllArgsConstructor
public class RegisterCommand implements IClientCommand {

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    @Override
    public void runCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        String guildID = logInfo.getGuildID();

        TextChannel targetTextChannel = getTargetTextChannel(event);
        String targetTextChannelID = targetTextChannel.getId();

        Language language = guildsCache.getGuildLanguage(guildID);

        if (isChannelAlreadyRegistered(guildID, targetTextChannelID)) {
            log.error("{} used /register. Error: Channel already registered. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), logInfo.getGuildName(), guildID, targetTextChannel.getName(), targetTextChannelID);
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CHANNEL-ALREADY-REGISTERED"));
            return;
        }

        NotificationChannel notificationChannel = new NotificationChannel(targetTextChannelID, guildID);
        guildsCache.getClientGuildByID(guildID).addNewNotificationChannel(notificationChannel);
        databaseRequests.createNotificationChannel(notificationChannel);

        log.info("{} used /register. Guild: {}({}). Channel: {}({})", logInfo.getExecutor(), logInfo.getGuildName(),
                guildID, targetTextChannel.getName(), targetTextChannelID);
        replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "CHANNEL-REGISTERED"), targetTextChannel.getAsMention()));
    }

    private boolean isChannelAlreadyRegistered(String guildID, String textChannelID) {
        return guildsCache.getClientGuildByID(guildID).isChannelRegistered(textChannelID);
    }

}
