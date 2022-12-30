package me.umbreon.didn.commands.channel;

import me.umbreon.didn.commands.IClientCommand;
import me.umbreon.didn.enums.GameEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * @author Umbreon Majora
 * Show's a list of all aviable game events.
 * Command: /listevents
 */
public class ListEventsCommand implements IClientCommand {

    private final String gameEventList;

    public ListEventsCommand() {
        this.gameEventList = buildGameEventList();
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        replyEphemeralToUser(event, gameEventList);
    }

    private String buildGameEventList() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("```\n");
        for (GameEvent gameEvent : GameEvent.values()) {
            stringBuilder.append(gameEvent.rawName)
                    .append("\n");
        }
        stringBuilder.append("```");
        return stringBuilder.toString();
    }
}
