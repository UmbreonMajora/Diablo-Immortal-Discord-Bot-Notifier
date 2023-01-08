package net.purplegoose.didnb.commands.server;

import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.ClientGuild;
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
            createLog(LOGGER, getFullUsernameWithDiscriminator(user) + " tried to use /language but it " +
                    "failed because language was invalid.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "LANGUAGE-INVALID"));
            return;
        }

        ClientGuild clientGuild = guildsCache.getClientGuildByID(guildID);
        clientGuild.setGuildLanguage(newGuildLanguage);
        databaseRequests.updateGuild(clientGuild);

        createLog(LOGGER, getFullUsernameWithDiscriminator(user) + " changed language from " + guildID
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
