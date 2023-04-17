package net.purplegoose.didnb.commands.server;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.purplegoose.didnb.utils.CommandsUtil.LANGUAGE_OPTION_NAME;

/**
 * @author Umbreon Majora
 * <p>
 * @Command: Allow's user to change the bot's language for their server.
 * <p>
 * @Command: /language [Required: language]
 */
@Slf4j
@AllArgsConstructor
public class LanguageCommand implements IClientCommand {

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    @Override
    public void runCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        String guildID = logInfo.getGuildID();
        Language language = guildsCache.getGuildLanguage(guildID);

        Language newLanguage = getLanguage(event);
        if (newLanguage == null) {
            log.error("{} used /language. Error: New language null. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), logInfo.getGuildName(), guildID, logInfo.getChannelName(), logInfo.getChannelID());
            replyEphemeralToUser(event, LanguageController.getMessage(language, "LANGUAGE-INVALID"));
            return;
        }

        ClientGuild clientGuild = guildsCache.getClientGuildByID(guildID);
        Language oldLanguage = guildsCache.getGuildLanguage(guildID);
        updateLanguage(clientGuild, newLanguage);

        log.error("{} used /language. Old language: {}. New language {}. Guild: {}({}). Channel: {}({})",
                logInfo.getExecutor(), oldLanguage.rawName, newLanguage.rawName, logInfo.getGuildName(), guildID,
                logInfo.getChannelName(), logInfo.getChannelID());
        replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "LANGUAGE-CHANGED"), newLanguage.rawName));
    }

    private void updateLanguage(ClientGuild clientGuild, Language language) {
        clientGuild.setGuildLanguage(language);
        databaseRequests.updateGuild(clientGuild);
    }

    private Language getLanguage(SlashCommandInteractionEvent event) {
        OptionMapping languageOption = event.getOption(LANGUAGE_OPTION_NAME);
        if (languageOption != null) {
            String languageStringValue = languageOption.getAsString();
            return Language.findLanguageByShortName(languageStringValue);
        }
        return null;
    }
}
