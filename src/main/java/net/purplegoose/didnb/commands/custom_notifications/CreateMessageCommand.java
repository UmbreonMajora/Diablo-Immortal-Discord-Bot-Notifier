package net.purplegoose.didnb.commands.custom_notifications;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.purplegoose.didnb.cache.CustomMessagesCache;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.data.CustomNotification;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.enums.Weekday;
import net.purplegoose.didnb.languages.LanguageController;
import net.purplegoose.didnb.utils.ConfigUtil;
import net.purplegoose.didnb.utils.StringUtil;

import static net.purplegoose.didnb.utils.CommandsUtil.*;

/**
 * @author Umbreon Majora
 * Allow's user to create custom notification messages.
 * Command: /createmessage [Required: weekday] [Required: time] [Required: custommessagerepeating] [Required: custommessagemessage]
 */
@Slf4j
@AllArgsConstructor
public class CreateMessageCommand implements IClientCommand {

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;
    private final CustomMessagesCache customMessagesCache;

    @Override
    public void runCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        String guildID = logInfo.getGuildID();
        Language language = guildsCache.getGuildLanguage(guildID);

        if (!isWeekdayValid(getWeekday(event), logInfo)) {
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CREATE-CUSTOM-MESSAGE-FAILED-WEEKDAY-INVALID"));
            return;
        }

        String weekdayRawName = getWeekday(event);
        Weekday weekday = Weekday.findWeekdayByRawName(weekdayRawName);

        if (!isTimeValid(getTime(event), logInfo)) {
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CREATE-CUSTOM-MESSAGE-FAILED-TIME-INVALID"));
            return;
        }

        String time = getTime(event);
        String message = getMessage(event);

        if (message == null) {
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CREATE-CUSTOM-MESSAGE-FAILED-MESSAGE-NULL"));
            log.error("{} used /createmessage. Error: Message invalid. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
            return;
        }

        ClientGuild clientGuild = guildsCache.getClientGuildByID(guildID);
        if (clientGuild.getCustomNotificationSize() >= ConfigUtil.getCustomNotificationMax() && !clientGuild.isPremiumServer()) {
            replyEphemeralToUser(event, LanguageController.getMessage(language, "MESSAGE-MAX-REACHED"));
            log.error("{} used /createmessage. Error: Max reached. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
            return;
        }

        String targetTextChannelID = getTargetTextChannel(event).getId();
        boolean repeating = getRepeating(event);
        String id = getNextFreeId();
        CustomNotification cn = new CustomNotification(targetTextChannelID, guildID, message, weekday.rawName, time, id, repeating, true);
        databaseRequests.createCustomNotification(cn);
        clientGuild.addCustomNotification(cn);
        log.info("{} used /createmessage. ID: {} Guild: {}({}). Channel: {}({})",
                logInfo.getExecutor(), id, logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
        replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "CREATED-CUSTOM-MESSAGE"), id));
    }

    private String getNextFreeId() {
        String id = StringUtil.generateRandomID();

        while (customMessagesCache.isIdentifierInUse(id)) {
            id = StringUtil.generateRandomID();
        }

        return id;
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

    private boolean isWeekdayValid(String weekdayRawName, LoggingInformation logInfo) {
        if (weekdayRawName == null) {
            log.error("{} used /createmessage. Error: Weekday null. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
            return false;
        }

        Weekday weekday = Weekday.findWeekdayByRawName(weekdayRawName);
        if (weekday == null) {
            log.error("{} used /createmessage. Error: Weekday invalid. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
            return false;
        }

        return true;
    }

    private boolean isTimeValid(String time, LoggingInformation logInfo) {
        if (time == null) {
            log.error("{} used /createmessage. Error: Time null. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
            return false;
        }

        if (StringUtil.isStringNotInTimePattern(time)) {
            log.error("{} used /createmessage. Error: Time invalid. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
            return false;
        }

        return true;
    }
}
