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

import java.util.Objects;

/**
 * @author Umbreon Majora
 * Allow's users to toggle daylight time.
 * Command: /daylightime off/on
 */
@Slf4j
@AllArgsConstructor
public class DaylightTimeCommand implements IClientCommand {

    private final DatabaseRequests databaseRequests;
    private final GuildsCache guildsCache;

    @Override
    public void runCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        String guildID = Objects.requireNonNull(event.getGuild()).getId();
        Language language = guildsCache.getGuildLanguage(guildID);
        String executingUser = getFullUsernameWithDiscriminator(event.getUser());
        boolean commandValue = getCommandBooleanValue(event);
        ClientGuild clientGuild = guildsCache.getClientGuildByID(guildID);

        if (clientGuild.isDaylightTimeEnabled() && commandValue) {
            replyEphemeralToUser(event, LanguageController.getMessage(language, "DAYLIGHT-TIME-ALREADY-ENABLED"));
            //createLog(logger, executingUser + " tried to enable daylight saving time for " + guildID +
            //        " but it was already enabled.");
            return;
        }

        if (!clientGuild.isDaylightTimeEnabled() && !commandValue) {
            replyEphemeralToUser(event, LanguageController.getMessage(language, "DAYLIGHT-TIME-ALREADY-DISABLED"));
            //createLog(logger, executingUser + " tried to enable daylight saving time for " + guildID +
            //        " but it was already disabled.");
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
            //createLog(logger, executingUser + " enabled daylight saving time for " + guildID);
        } else {
            replyEphemeralToUser(event, LanguageController.getMessage(language, "DAYLIGHT-TIME-NOW-DISABLED"));
            //createLog(logger, executingUser + " disabled daylight saving time for " + guildID);
        }

    }

    private boolean getCommandBooleanValue(SlashCommandInteractionEvent event) {
        OptionMapping optionMapping = event.getOption("boolean");
        return optionMapping != null && optionMapping.getAsBoolean();
    }
}
