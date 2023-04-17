package net.purplegoose.didnb.commands.info;

import lombok.extern.slf4j.Slf4j;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.enums.Language;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import static net.purplegoose.didnb.utils.StringUtil.NEW_LINE;

/**
 * @author Umbreon Majora
 * <p>
 * Send's the user a list with all aviable bot languages.
 * <p>
 * Command: /languages
 */
@Slf4j
public class LanguagesCommand implements IClientCommand {

    @Override
    public void runCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        log.info("{} used /languages. Guild: {}({}). Channel: {}({})",
                logInfo.getExecutor(), logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
        replyEphemeralToUser(event, getAvailableLanguagesList());
    }

    private String getAvailableLanguagesList() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("```").append("Available languages:").append(NEW_LINE).append(NEW_LINE);
        for (Language language : Language.values()) {
            stringBuilder.append(language.rawName).append(" - ").append(language.shortName).append(NEW_LINE);
        }
        stringBuilder.append("```");
        return stringBuilder.toString();
    }
}
