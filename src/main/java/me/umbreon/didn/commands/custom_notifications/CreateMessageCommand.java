package me.umbreon.didn.commands.custom_notifications;

import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.commands.IClientCommand;
import me.umbreon.didn.data.ClientGuild;
import me.umbreon.didn.data.CustomNotification;
import me.umbreon.didn.database.DatabaseRequests;
import me.umbreon.didn.enums.Language;
import me.umbreon.didn.enums.Weekday;
import me.umbreon.didn.languages.LanguageController;
import me.umbreon.didn.utils.ConfigUtil;
import me.umbreon.didn.utils.StringUtil;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static me.umbreon.didn.utils.CommandsUtil.*;

/**
 * @author Umbreon Majora
 * Allow's user to create custom notification messages.
 * Command: /createmessage [Required: weekday] [Required: time] [Required: custommessagerepeating] [Required: custommessagemessage]
 */
public class CreateMessageCommand implements IClientCommand {

    private final Logger logger = LoggerFactory.getLogger(CreateMessageCommand.class);

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    public CreateMessageCommand(GuildsCache guildsCache, DatabaseRequests databaseRequests) {
        this.guildsCache = guildsCache;
        this.databaseRequests = databaseRequests;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        String guildID = Objects.requireNonNull(event.getGuild()).getId();
        Language language = guildsCache.getGuildLanguage(guildID);
        String executingUser = getFullUsernameWithDiscriminator(event.getUser());

        String weekdayRawName = getWeekday(event);
        if (weekdayRawName == null) {
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CREATE-CUSTOM-MESSAGE-FAILED-WEEKDAY-NULL"));
            createLog(logger, guildID, executingUser + " tried to create a message on " + guildID + " but the weekday was null.");
            return;
        }

        Weekday weekday = Weekday.findWeekdayByRawName(weekdayRawName);
        if (weekday == null) {
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CREATE-CUSTOM-MESSAGE-FAILED-WEEKDAY-INVALID"));
            createLog(logger, guildID, executingUser + " tried to create a message on " + guildID + " but the weekday was invalid.");
            return;
        }

        String time = getTime(event);
        if (time == null) {
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CREATE-CUSTOM-MESSAGE-FAILED-TIME-NULL"));
            createLog(logger, guildID, executingUser + " tried to create a message on " + guildID + " but the time was null.");
            return;
        }

        if (!StringUtil.isStringInTimePattern(time)) {
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CREATE-CUSTOM-MESSAGE-FAILED-TIME-INVALID"));
            createLog(logger, guildID, executingUser + " tried to create a message on " + guildID + " but the time was invalid.");
            return;
        }

        String message = getMessage(event);
        if (message == null) {
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CREATE-CUSTOM-MESSAGE-FAILED-MESSAGE-NULL"));
            createLog(logger, guildID, executingUser + " tried to create a message on " + guildID + " but the time was invalid.");
            return;
        }

        ClientGuild clientGuild = guildsCache.getClientGuildByID(guildID);
        if (clientGuild.getCustomNotificationSize() >= ConfigUtil.getCustomNotificationMax() && !clientGuild.isPremiumServer()) {
            replyEphemeralToUser(event, LanguageController.getMessage(language, "MESSAGE-MAX-REACHED"));
            createLog(logger, guildID, executingUser + " tried to create a message on " + guildID + " but the max was reached.");
            return;
        }

        String targetTextChannelID = getTargetTextChannel(event).getId();
        boolean repeating = getRepeating(event);
        int nextAutoIncrementNumber = databaseRequests.getGetCustomMessageNextAutoIncrementValue();
        CustomNotification customNotification = new CustomNotification(targetTextChannelID, guildID, message, weekday.rawName, time, repeating, true);
        databaseRequests.createCustomNotification(customNotification);
        customNotification.setCustomMessageID(nextAutoIncrementNumber);
        guildsCache.getClientGuildByID(guildID).addCustomNotification(customNotification);
        createLog(logger, guildID, executingUser + " created a new message on " + guildID + " with id " + nextAutoIncrementNumber);
        replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "CREATED-CUSTOM-MESSAGE"), nextAutoIncrementNumber));
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
