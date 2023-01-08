package net.purplegoose.didnb.commands.info;

import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.utils.CommandsUtil;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import static net.purplegoose.didnb.utils.StringUtil.FORMATTED_MESSAGE;
import static net.purplegoose.didnb.utils.StringUtil.NEW_LINE;

/**
 * @author Umbreon Majora
 * Show's a list with all commands.
 * Command: /help
 */
public class HelpCommand implements IClientCommand {

    private final String helpMessage;

    public HelpCommand() {
        StringBuilder helpMessageBuilder = new StringBuilder();
        helpMessageBuilder.append(FORMATTED_MESSAGE)
                .append("Diablo Immortal Discord Notifier Bot:")
                .append(NEW_LINE)
                .append(NEW_LINE);
        CommandsUtil.getCommandsProperties().forEach((command, description) ->
                helpMessageBuilder.append("/").append(command)
                        .append(NEW_LINE)
                        .append(description.toString())
                        .append(NEW_LINE)
                        .append(NEW_LINE));
        helpMessageBuilder.append("Diablo Immortal Discord Notifier Bot created by Umbreon.")
                .append(FORMATTED_MESSAGE);
        helpMessage = helpMessageBuilder.toString();
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        replyEphemeralToUser(event, helpMessage);
    }

}
