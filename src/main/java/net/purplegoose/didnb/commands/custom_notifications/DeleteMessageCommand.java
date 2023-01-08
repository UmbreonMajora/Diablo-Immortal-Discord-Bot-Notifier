package net.purplegoose.didnb.commands.custom_notifications;

import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.CustomNotification;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static net.purplegoose.didnb.utils.CommandsUtil.CUSTOM_MESSAGE_ID_OPTION_NAME;

/**
 * @author Umbreon Majora
 * Allow's user to delete their custom notification by the given id.
 * Command: /deletemessage [Required: custommessageid]
 */
public class DeleteMessageCommand implements IClientCommand {

    private final Logger LOGGER = LoggerFactory.getLogger(DeleteMessageCommand.class);

    private final DatabaseRequests databaseRequests;
    private final GuildsCache guildsCache;

    public DeleteMessageCommand(DatabaseRequests databaseRequests, GuildsCache guildsCache) {
        this.databaseRequests = databaseRequests;
        this.guildsCache = guildsCache;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        String guildID = Objects.requireNonNull(event.getGuild()).getId();
        Language language = guildsCache.getGuildLanguage(guildID);
        String executingUser = getFullUsernameWithDiscriminator(event.getUser());

        int customMessageID = getCustomMessageID(event);
        if (customMessageID == -1) {
            createLog(LOGGER, executingUser + " tried to delete a custom notification but it failed because the id was invalid.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "INFO-CUSTOM-MESSAGE-FAILED-ID-NULL"));
            return;
        }

        if (!isCustomMessageGuildIdCurrentGuildId(guildID, customMessageID)) {
            createLog(LOGGER, executingUser + " tried to delete a custom notification which wasn't theirs.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "DELETE-CUSTOM-MESSAGE-FAILED-NOT-PERMITTED"));
            return;
        }

        databaseRequests.deleteCustomNotificationByID(customMessageID);
        guildsCache.getClientGuildByID(guildID).deleteCustomNotificationByID(customMessageID);
        createLog(LOGGER, executingUser + " deleted custom notification with id " + customMessageID);
        replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "DELETED-CUSTOM-MESSAGE"), customMessageID));
    }

    private int getCustomMessageID(SlashCommandInteractionEvent event) {
        OptionMapping customMessageIdOption = event.getOption(CUSTOM_MESSAGE_ID_OPTION_NAME);
        return customMessageIdOption != null ? customMessageIdOption.getAsInt() : -1;
    }

    private boolean isCustomMessageGuildIdCurrentGuildId(String guildID, int customMessageID) {
        CustomNotification customNotification = guildsCache.getClientGuildByID(guildID).getCustomNotificationByID(customMessageID);
        //Todo: Add a sperate error message, like "Custom Message with ID <ID> does not exists.
        if (customNotification == null) {
            return false;
        }
        String targetGuildID = customNotification.getGuildID();
        return Objects.equals(guildID, targetGuildID);
    }
}
