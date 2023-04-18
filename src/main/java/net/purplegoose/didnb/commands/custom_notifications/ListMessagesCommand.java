package net.purplegoose.didnb.commands.custom_notifications;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.CustomNotification;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;

import java.awt.*;
import java.util.Map;
import java.util.Objects;

import static net.purplegoose.didnb.utils.StringUtil.NEW_LINE;

/**
 * @author Umbreon Majora
 * Allow's user to see all their custom notifications.
 * Command: /listmessages
 */
@Slf4j
@AllArgsConstructor
public class ListMessagesCommand implements IClientCommand {

    private final GuildsCache guildsCache;

    @Override
    public void runCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        String guildID = Objects.requireNonNull(event.getGuild()).getId();
        Language language = guildsCache.getGuildLanguage(guildID);

        Map<String, CustomNotification> customNotifications = guildsCache.getClientGuildByID(guildID).getCustomNotifications();

        if (customNotifications.size() == 0) {
            log.error("{} used /listmessages. Error: Empty list. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
            replyEphemeralToUser(event, LanguageController.getMessage(language, "LIST-CUSTOM-MESSAGES-FAILED-LIST-EMPTY"));
            return;
        }

        replyEphemeralToUser(event, buildCustomNotificationsList(customNotifications, language));
        log.info("{} used /listmessages  Guild: {}({}). Channel: {}({})",
                logInfo.getExecutor(), logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
    }

    private MessageEmbed buildCustomNotificationsList(Map<String, CustomNotification> customNotificationList, Language language) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.GRAY);
        LanguageController.getMessage(language, "TEXT-YOUR-CUSTOM-NOTIFICATIONS");
        for (CustomNotification customNotification : customNotificationList.values()) {
            String s = "Weekday: " + customNotification.getWeekday() + NEW_LINE +
                    "Time: " + customNotification.getTime() + NEW_LINE +
                    "Repeating: " + customNotification.isRepeating() + NEW_LINE +
                    "Enabled: " + customNotification.isEnabled() + NEW_LINE +
                    "Channel: " + customNotification.getChannelID() + NEW_LINE +
                    "Message: " + customNotification.getMessage();
            embedBuilder.addField("ID: " + customNotification.getCustomMessageID(), s, true);
        }
        return embedBuilder.build();
    }
}
