package me.umbreon.didn.commands.channel;

import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.commands.IClientCommand;
import me.umbreon.didn.data.NotificationChannel;
import me.umbreon.didn.database.DatabaseRequests;
import me.umbreon.didn.enums.Language;
import me.umbreon.didn.languages.LanguageController;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @author Umbreon Majora
 * Allow's user to register a text channel as notification channel.
 * Command: /register [Not required: targetchannel]
 */
public class RegisterCommand implements IClientCommand {

    private final Logger LOGGER = LoggerFactory.getLogger(RegisterCommand.class);

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    public RegisterCommand(GuildsCache guildsCache, DatabaseRequests databaseRequests) {
        this.guildsCache = guildsCache;
        this.databaseRequests = databaseRequests;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        TextChannel targetTextChannel = getTargetTextChannel(event);
        String guildID = Objects.requireNonNull(event.getGuild()).getId();
        String targetTextChannelID = targetTextChannel.getId();
        User user = event.getUser();
        Language language = guildsCache.getGuildLanguage(guildID);

        if (isChannelAlreadyRegistered(guildID, targetTextChannelID)) {
            createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " tried to register " + targetTextChannelID + " but it is already registered.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CHANNEL-ALREADY-REGISTERED"));
            return;
        }

        NotificationChannel notificationChannel = new NotificationChannel(targetTextChannelID, guildID);
        guildsCache.getClientGuildByID(guildID).addNewNotificationChannel(notificationChannel);
        databaseRequests.createNotificationChannel(notificationChannel);

        createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " registered channel " + targetTextChannelID);
        replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "CHANNEL-REGISTERED"), targetTextChannel.getAsMention()));
    }

    private boolean isChannelAlreadyRegistered(String guildID, String textChannelID) {
        return guildsCache.getClientGuildByID(guildID).isChannelRegistered(textChannelID);
    }

}
