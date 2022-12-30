package me.umbreon.didn.commands.server;

import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.commands.IClientCommand;
import me.umbreon.didn.data.ClientGuild;
import me.umbreon.didn.database.DatabaseRequests;
import me.umbreon.didn.enums.Language;
import me.umbreon.didn.languages.LanguageController;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static me.umbreon.didn.utils.CommandsUtil.LANGUAGE_OPTION_NAME;

/**
 * @author Umbreon Majora
 * Allow's user to change the bot's language for their server.
 * Command: /language [Required: language]
 */
public class LanguageCommand implements IClientCommand {

    private final Logger LOGGER = LoggerFactory.getLogger(LanguageCommand.class);

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    public LanguageCommand(GuildsCache guildsCache, DatabaseRequests databaseRequests) {
        this.guildsCache = guildsCache;
        this.databaseRequests = databaseRequests;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        String guildID = event.getGuild().getId(); //Can't be null since it's caught in SlashCommandInteraction.java
        User user = event.getUser();
        Language language = guildsCache.getGuildLanguage(guildID);

        Language newGuildLanguage = getLanguage(event);
        if (newGuildLanguage == null) {
            createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " tried to use /language but it " +
                    "failed because language was invalid.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "LANGUAGE-INVALID"));
            return;
        }

        ClientGuild clientGuild = guildsCache.getClientGuildByID(guildID);
        clientGuild.setGuildLanguage(newGuildLanguage);
        databaseRequests.updateGuild(clientGuild);

        createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " changed language from " + guildID
                + " to " + newGuildLanguage.rawName);

        replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "LANGUAGE-CHANGED"), newGuildLanguage.rawName));
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
