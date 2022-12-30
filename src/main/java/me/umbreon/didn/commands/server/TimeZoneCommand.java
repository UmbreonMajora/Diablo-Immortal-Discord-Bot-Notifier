package me.umbreon.didn.commands.server;

import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.commands.IClientCommand;
import me.umbreon.didn.data.ClientGuild;
import me.umbreon.didn.database.DatabaseRequests;
import me.umbreon.didn.enums.Language;
import me.umbreon.didn.languages.LanguageController;
import me.umbreon.didn.utils.StringUtil;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.zone.ZoneRulesException;

/**
 * @author Umbreon Majora
 * Allow's user to change the timezone for their server.
 * Command: /timezone [Required: timezone]
 */
public class TimeZoneCommand implements IClientCommand {

    private final Logger LOGGER = LoggerFactory.getLogger(TimeZoneCommand.class);

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    public TimeZoneCommand(GuildsCache guildsCache, DatabaseRequests databaseRequests) {
        this.guildsCache = guildsCache;
        this.databaseRequests = databaseRequests;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        String guildID = event.getGuild().getId(); //Can't be null since it's caught in SlashCommandInteraction.java
        User user = event.getUser();
        Language language = guildsCache.getGuildLanguage(guildID);

        String timeZone = getTimeZone(event);
        if (timeZone == null)  {
            createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " tried to use /timezone but " +
                    "failed because timezone was null.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CHANGE-TIMEZONE-FAILED-INVALID"));
            return;
        }

        if (!isTimeZoneValid(timeZone)) {
            createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " tried to use /timezone but " +
                    "failed because timezone was invalid.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CHANGE-TIMEZONE-FAILED-NULL"));
            return;
        }

        ClientGuild clientGuild = guildsCache.getClientGuildByID(guildID);
        clientGuild.setGuildTimeZone(timeZone);
        databaseRequests.updateGuild(clientGuild);

        createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " changed timezone of " + guildID +
                " to " + timeZone);
        replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "CHANGE-TIMEZONE-SUCCESS"), timeZone));
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
