package net.purplegoose.didnb.commands.server;

import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;
import net.purplegoose.didnb.utils.StringUtil;
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
            createLog(LOGGER, getFullUsernameWithDiscriminator(user) + " tried to use /timezone but " +
                    "failed because timezone was null.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CHANGE-TIMEZONE-FAILED-INVALID"));
            return;
        }

        if (!isTimeZoneValid(timeZone)) {
            createLog(LOGGER, getFullUsernameWithDiscriminator(user) + " tried to use /timezone but " +
                    "failed because timezone was invalid.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CHANGE-TIMEZONE-FAILED-NULL"));
            return;
        }

        ClientGuild clientGuild = guildsCache.getClientGuildByID(guildID);
        clientGuild.setGuildTimeZone(timeZone);
        databaseRequests.updateGuild(clientGuild);

        createLog(LOGGER, getFullUsernameWithDiscriminator(user) + " changed timezone of " + guildID +
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
