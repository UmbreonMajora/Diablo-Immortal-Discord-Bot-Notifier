package net.purplegoose.didnb.commands.server;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;
import net.purplegoose.didnb.utils.StringUtil;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.zone.ZoneRulesException;

/**
 * @author Umbreon Majora
 * <p>
 * @Description:  Allow's user to change the timezone for their server.
 * <p>
 * @Command: /timezone [Required: timezone]
 */
@Slf4j
@AllArgsConstructor
public class TimeZoneCommand implements IClientCommand {

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    @Override
    public void runCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        String guildID = logInfo.getGuildID();
        Language language = guildsCache.getLanguageByGuildID(guildID);

        String newTimeZone = getTimeZone(event);
        if (newTimeZone == null)  {
            log.error("{} used /timezone. Error: Timezone is null. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), logInfo.getGuildName(), guildID, logInfo.getChannelName(),
                    logInfo.getChannelID());
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CHANGE-TIMEZONE-FAILED-INVALID"));
            return;
        }

        if (!isTimeZoneValid(newTimeZone)) {
            log.error("{} used /timezone. Error: Timezone is invalid. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), logInfo.getGuildName(), guildID, logInfo.getChannelName(),
                    logInfo.getChannelID());
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CHANGE-TIMEZONE-FAILED-NULL"));
            return;
        }

        ClientGuild clientGuild = guildsCache.getClientGuildByID(guildID);
        String oldTimeZone = clientGuild.getTimeZone();
        updateTimeZone(clientGuild, newTimeZone);

        log.info("{} used /timezone. Old timezone: {}. New timezone {}. Guild: {}({}). Channel: {}({})",
                logInfo.getExecutor(), oldTimeZone, newTimeZone, logInfo.getGuildName(), guildID,
                logInfo.getChannelName(), logInfo.getChannelID());

        replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "CHANGE-TIMEZONE-SUCCESS"), newTimeZone));
    }

    private void updateTimeZone(ClientGuild clientGuild, String timeZone) {
        clientGuild.setTimeZone(timeZone);
        databaseRequests.updateGuild(clientGuild);
    }

    private String getTimeZone(SlashCommandInteractionEvent event) {
        OptionMapping timeZoneOption = event.getOption("timezone");
        return timeZoneOption != null ? timeZoneOption.getAsString() : null;
    }

    private boolean isTimeZoneValid(String timezone) {
        if (StringUtil.isStringSingleDashWithDigits(timezone)) {
            return false;
        }

        try {
            Instant timeStamp = Instant.now();
            ZonedDateTime dateTime = timeStamp.atZone(ZoneId.of(timezone));
            return true;
        } catch (ZoneRulesException e) {
            return false;
        }
    }
}
