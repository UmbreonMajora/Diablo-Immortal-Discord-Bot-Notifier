package me.umbreon.didn.commands.info;

import me.umbreon.didn.commands.IClientCommand;
import me.umbreon.didn.enums.Language;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

import static me.umbreon.didn.utils.StringUtil.NEW_LINE;

/**
 * @author Umbreon Majora
 * Send's the user a list with all aviable bot languages.
 * Command: /languages
 */
public class LanguagesCommand implements IClientCommand {

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
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
