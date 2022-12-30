package me.umbreon.didn.commands.custom_notifications;

import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.commands.IClientCommand;
import me.umbreon.didn.data.CustomNotification;
import me.umbreon.didn.database.DatabaseRequests;
import me.umbreon.didn.enums.Language;
import me.umbreon.didn.languages.LanguageController;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Umbreon Majora
 * Allow's user to enable or disable custom notification messages.
 * Command: /message [Required: messagevalue] [Required: messageid]
 */
public class MessageCommand implements IClientCommand {

    private final Logger LOGGER = LoggerFactory.getLogger(MessageCommand.class);

    private final DatabaseRequests databaseRequests;
    private final GuildsCache guildsCache;

    public MessageCommand(DatabaseRequests databaseRequests, GuildsCache guildsCache) {
        this.databaseRequests = databaseRequests;
        this.guildsCache = guildsCache;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        String guildID = event.getGuild().getId(); //Can't be null since it's caught in SlashCommandInteraction.java
        User user = event.getUser();
        Language language = guildsCache.getGuildLanguage(guildID);

        int messageID = getMessageID(event);
        if (messageID == -1) {
            replyEphemeralToUser(event, LanguageController.getMessage(language, "INVALID-ID"));
            createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " tried to update a custom message" +
                    "with an invalid id.");
            return;
        }

        CustomNotification customNotification = guildsCache.getClientGuildByID(guildID).getCustomNotificationByID(messageID);
        if (customNotification == null) {
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CM-NOT-EXISTS"));
            createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " tried to update a custom message" +
                    "which does not exists.");
            return;
        }

        boolean messageValue = getMessageValue(event);
        customNotification.setEnabled(messageValue);
        databaseRequests.updateCustomNotification(customNotification);
        replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "CM-ENABLED-DISABLED"), customNotification.getCustomMessageID(),
                messageValue ? "enabled" : "disabled"));
    }

    private boolean getMessageValue(SlashCommandInteractionEvent event) {
        OptionMapping messageValueOption = event.getOption("messagevalue");
        return messageValueOption != null && messageValueOption.getAsBoolean();
    }

    private int getMessageID(SlashCommandInteractionEvent event) {
        OptionMapping messageIdOption = event.getOption("messageid");
        return messageIdOption != null ? messageIdOption.getAsInt() : -1;
    }

}
