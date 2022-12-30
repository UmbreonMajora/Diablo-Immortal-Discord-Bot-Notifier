package me.umbreon.didn.commands.custom_notifications;

import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.commands.IClientCommand;
import me.umbreon.didn.data.CustomNotification;
import me.umbreon.didn.database.DatabaseRequests;
import me.umbreon.didn.enums.Language;
import me.umbreon.didn.enums.Weekday;
import me.umbreon.didn.languages.LanguageController;
import me.umbreon.didn.utils.StringUtil;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static me.umbreon.didn.utils.CommandsUtil.*;

/**
 * @author Umbreon Majora
 * Allow's user to create custom notification messages.
 * Command: /createcustommessage [Required: weekday] [Required: time] [Required: custommessagerepeating] [Required: custommessagemessage]
 */
public class CreateCustomNotificationCommand implements IClientCommand {

    private final Logger LOGGER = LoggerFactory.getLogger(CreateCustomNotificationCommand.class);

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    public CreateCustomNotificationCommand(GuildsCache guildsCache, DatabaseRequests databaseRequests) {
        this.guildsCache = guildsCache;
        this.databaseRequests = databaseRequests;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        TextChannel targetTextChannel = getTargetTextChannel(event);
        String guildID = event.getGuild().getId(); //Can't be null since it's caught in SlashCommandInteraction.java
        String targetTextChannelID = targetTextChannel.getId();
        User user = event.getUser();
        Language language = guildsCache.getGuildLanguage(guildID);

        String weekdayByRawName = getWeekday(event);
        if (weekdayByRawName == null) {
            createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " tried to use " +
                    "/createcustommessage but it failed because weekday was null.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CREATE-CUSTOM-MESSAGE-FAILED-WEEKDAY-NULL"));
            return;
        }

        Weekday weekday = Weekday.findWeekdayByRawName(weekdayByRawName);
        if (weekday == null) {
            createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " tried to use " +
                    "/createcustommessage but it failed because weekday was invalid.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CREATE-CUSTOM-MESSAGE-FAILED-WEEKDAY-INVALID"));
            return;
        }

        String time = getTime(event);
        if (time == null) {
            createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " tried to use " +
                    "/createcustommessage but it failed because time was null.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CREATE-CUSTOM-MESSAGE-FAILED-TIME-NULL"));
            return;
        }

        if (!StringUtil.isStringInTimePattern(time)) {
            createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " tried to use " +
                    "/createcustommessage but it failed because time was invalid.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CREATE-CUSTOM-MESSAGE-FAILED-TIME-INVALID"));
            return;
        }

        boolean repeating = getRepeating(event);
        String message = getMessage(event);
        if (message == null) {
            createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " tried to use " +
                    "/createcustommessage but it failed because message was null.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CREATE-CUSTOM-MESSAGE-FAILED-MESSAGE-NULL"));
        }

        int nextAutoIncrementNumber = databaseRequests.getGetCustomMessageNextAutoIncrementValue();
        CustomNotification customNotification = new CustomNotification(targetTextChannelID, guildID, message, weekday.rawName, time, repeating, true);
        databaseRequests.createCustomNotification(customNotification);
        customNotification.setCustomMessageID(nextAutoIncrementNumber);
        guildsCache.getClientGuildByID(guildID).addCustomNotification(customNotification);

        createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " created a new custom notification " +
                "with id " + nextAutoIncrementNumber);
        replyEphemeralToUser(event, LanguageController.getMessage(language, "CREATED-CUSTOM-MESSAGE"));
    }

    private String getWeekday(SlashCommandInteractionEvent event) {
        OptionMapping weekdayOption = event.getOption(WEEKDAY_OPTION_NAME);
        return weekdayOption != null ? weekdayOption.getAsString() : null;
    }

    private String getTime(SlashCommandInteractionEvent event) {
        OptionMapping timeOption = event.getOption(TIME_OPTION_NAME);
        return timeOption != null ? timeOption.getAsString() : null;
    }

    private boolean getRepeating(SlashCommandInteractionEvent event) {
        OptionMapping repeatingOption = event.getOption(REPEATING_OPTION_NAME);
        return repeatingOption != null && repeatingOption.getAsBoolean();
    }

    private String getMessage(SlashCommandInteractionEvent event) {
        OptionMapping messageOption = event.getOption(MESSAGE_OPTION_NAME);
        return messageOption != null ? messageOption.getAsString() : null;
    }
}
