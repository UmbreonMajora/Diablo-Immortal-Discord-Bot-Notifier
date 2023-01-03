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

import java.util.Objects;

import static me.umbreon.didn.utils.CommandsUtil.CUSTOM_MESSAGE_ID_OPTION_NAME;

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
            createLog(LOGGER, guildID, executingUser + " tried to delete a custom notification but it failed because the id was invalid.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "INFO-CUSTOM-MESSAGE-FAILED-ID-NULL"));
            return;
        }

        if (!isCustomMessageGuildIdCurrentGuildId(guildID, customMessageID)) {
            createLog(LOGGER, guildID, executingUser + " tried to delete a custom notification which wasn't theirs.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "DELETE-CUSTOM-MESSAGE-FAILED-NOT-PERMITTED"));
            return;
        }

        databaseRequests.deleteCustomNotificationByID(customMessageID);
        guildsCache.getClientGuildByID(guildID).deleteCustomNotificationByID(customMessageID);
        createLog(LOGGER, guildID, executingUser + " deleted custom notification with id " + customMessageID);
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
