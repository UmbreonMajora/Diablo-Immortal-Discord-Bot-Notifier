package net.purplegoose.didnb.commands.custom_notifications;

import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.CustomNotification;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Map;
import java.util.Objects;

/**
 * @author Umbreon Majora
 * Allow's user to see all their custom notifications.
 * Command: /listmessages
 */
public class ListMessagesCommand implements IClientCommand {

    private final Logger LOGGER = LoggerFactory.getLogger(ListMessagesCommand.class);

    private final GuildsCache guildsCache;

    public ListMessagesCommand(GuildsCache guildsCache) {
        this.guildsCache = guildsCache;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        String guildID = Objects.requireNonNull(event.getGuild()).getId();
        Language language = guildsCache.getGuildLanguage(guildID);
        String executingUser = getFullUsernameWithDiscriminator(event.getUser());

        Map<Integer, CustomNotification> customNotificationList = guildsCache.getClientGuildByID(guildID).getCustomNotifications();

        if (customNotificationList.size() == 0) {
            createLog(LOGGER, executingUser + " tried to list their custom notifications but the list is empty.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "LIST-CUSTOM-MESSAGES-FAILED-LIST-EMPTY"));
            return;
        }

        replyEphemeralToUser(event, buildCustomNotificationsList(customNotificationList, language));
        createLog(LOGGER, executingUser + " listed their custom notifications.");
    }

    private MessageEmbed buildCustomNotificationsList(Map<Integer, CustomNotification> customNotificationList, Language language) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.GRAY);
        LanguageController.getMessage(language, "TEXT-YOUR-CUSTOM-NOTIFICATIONS");
        for (CustomNotification customNotification : customNotificationList.values()) {
            String customMessageID = "ID: " + customNotification.getCustomMessageID();
            String s = "Channel: <#" + customNotification.getChannelID() + ">\n" +
                    "Message: " + customNotification.getMessage() + "\n" +
                    "Time: " + customNotification.getWeekday() + " " + customNotification.getTime() + "\n" +
                    "Enabled: " + customNotification.isEnabled();
            embedBuilder.addField(customMessageID, s, true);
        }
        return embedBuilder.build();
    }
}
