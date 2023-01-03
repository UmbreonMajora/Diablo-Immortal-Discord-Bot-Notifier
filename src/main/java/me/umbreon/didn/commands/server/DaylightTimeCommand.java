package me.umbreon.didn.commands.server;

import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.commands.IClientCommand;
import me.umbreon.didn.data.ClientGuild;
import me.umbreon.didn.database.DatabaseRequests;
import me.umbreon.didn.enums.Language;
import me.umbreon.didn.languages.LanguageController;
import me.umbreon.didn.utils.StringUtil;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @author Umbreon Majora
 * Allow's users to toggle daylight time.
 * Command: /daylightime off/on
 */
public class DaylightTimeCommand implements IClientCommand {

    private final Logger logger = LoggerFactory.getLogger(DaylightTimeCommand.class);

    private final DatabaseRequests databaseRequests;
    private final GuildsCache guildsCache;

    public DaylightTimeCommand(DatabaseRequests databaseRequests, GuildsCache guildsCache) {
        this.databaseRequests = databaseRequests;
        this.guildsCache = guildsCache;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        String guildID = Objects.requireNonNull(event.getGuild()).getId();
        Language language = guildsCache.getGuildLanguage(guildID);
        String executingUser = getFullUsernameWithDiscriminator(event.getUser());
        boolean commandValue = getCommandBooleanValue(event);
        ClientGuild clientGuild = guildsCache.getClientGuildByID(guildID);

        if (clientGuild.isDaylightTimeEnabled() && commandValue) {
            replyEphemeralToUser(event, LanguageController.getMessage(language, "DAYLIGHT-TIME-ALREADY-ENABLED"));
            createLog(logger, guildID, executingUser + " tried to enable daylight saving time for " + guildID +
                    " but it was already enabled.");
            return;
        }

        if (!clientGuild.isDaylightTimeEnabled() && !commandValue) {
            replyEphemeralToUser(event, LanguageController.getMessage(language, "DAYLIGHT-TIME-ALREADY-DISABLED"));
            createLog(logger, guildID, executingUser + " tried to enable daylight saving time for " + guildID +
                    " but it was already disabled.");
            return;
        }

        clientGuild.setDaylightTimeEnabled(commandValue);

        String timeZone = clientGuild.getGuildTimeZone();
        int clearTimeZone;
        try {
            clearTimeZone = Integer.parseInt(StringUtil.removeAllNonNumericCharacters(timeZone));
        } catch (NumberFormatException e) {
            clearTimeZone = 0;
        }

        if (commandValue) {
            clearTimeZone += 1;
        } else {
            clearTimeZone -= 1;
        }

        String newTimeZone;
        if (clearTimeZone == 0) {
            newTimeZone = "GMT";
        } else if (clearTimeZone > 0) {
            newTimeZone = "GMT+" + clearTimeZone;
        } else {
            newTimeZone = "GMT" + clearTimeZone;
        }

        clientGuild.setGuildTimeZone(newTimeZone);
        databaseRequests.updateGuild(clientGuild);
        if (commandValue) {
            replyEphemeralToUser(event, LanguageController.getMessage(language, "DAYLIGHT-TIME-NOW-ENABLED"));
            createLog(logger, guildID, executingUser + " enabled daylight saving time for " + guildID);
        } else {
            replyEphemeralToUser(event, LanguageController.getMessage(language, "DAYLIGHT-TIME-NOW-DISABLED"));
            createLog(logger, guildID, executingUser + " disabled daylight saving time for " + guildID);
        }

    }

    private boolean getCommandBooleanValue(SlashCommandInteractionEvent event) {
        OptionMapping optionMapping = event.getOption("boolean");
        return optionMapping != null && optionMapping.getAsBoolean();
    }
}
