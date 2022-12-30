package me.umbreon.didn.commands.custom_notifications;

import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.commands.IClientCommand;
import me.umbreon.didn.data.CustomNotification;
import me.umbreon.didn.database.DatabaseRequests;
import me.umbreon.didn.enums.Language;
import me.umbreon.didn.languages.LanguageController;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Map;

/**
 * @author Umbreon Majora
 * Allow's user to see all their custom notifications.
 * Command: /listcustommessages
 */
public class ListCustomMessagesCommand implements IClientCommand {

    private final Logger LOGGER = LoggerFactory.getLogger(ListCustomMessagesCommand.class);

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    public ListCustomMessagesCommand(GuildsCache guildsCache, DatabaseRequests databaseRequests) {
        this.guildsCache = guildsCache;
        this.databaseRequests = databaseRequests;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        String guildID = event.getGuild().getId(); //Can't be null since it's caught in SlashCommandInteraction.java
        Map<Integer, CustomNotification> customNotificationList = guildsCache.getClientGuildByID(guildID).getCustomNotifications();
        User user = event.getUser();
        Language language = guildsCache.getGuildLanguage(guildID);

        if (customNotificationList.size() == 0) {
            createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " tried to list their custom notification but it failed because the list was empty.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "LIST-CUSTOM-MESSAGES-FAILED-LIST-EMPTY"));
            return;
        }

        replyEphemeralToUser(event, buildCustomNotificationsList(customNotificationList, language));
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
