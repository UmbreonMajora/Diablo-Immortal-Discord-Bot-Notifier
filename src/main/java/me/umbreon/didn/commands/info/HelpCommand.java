package me.umbreon.didn.commands.info;

import me.umbreon.didn.commands.IClientCommand;
import me.umbreon.didn.utils.ImagesUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Umbreon Majora
 * Show's a list with all commands.
 * Command: /help
 */
public class HelpCommand implements IClientCommand {

    private static final Properties commandsProperties = new Properties();

    static {
        try (InputStream inputStream = InstructionsCommand.class.getClassLoader().getResourceAsStream("commands.properties")) {
            if (inputStream != null) {
                commandsProperties.load(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        replyEphemeralToUser(event, buildHelpEmbed());
    }

    private MessageEmbed buildHelpEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Diablo Immortal Notifier Commands");
        embedBuilder.setThumbnail(ImagesUtil.DIABLO_IMMORTAL_LOGO);
        embedBuilder.setColor(Color.RED);
        commandsProperties.forEach((command, description) ->
                embedBuilder.addField("/" + command, String.valueOf(description), false));
        embedBuilder.addBlankField(false);
        embedBuilder.setFooter("Diablo Immortal Discord Notifier created by Umbreon.");
        return embedBuilder.build();
    }
}
