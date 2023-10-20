package net.purplegoose.bot.di.commands;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.purplegoose.bot.di.cache.ClientCache;
import net.purplegoose.bot.di.data.ClientChannel;
import net.purplegoose.bot.di.data.ClientGuild;
import net.purplegoose.bot.di.database.DatabaseRequests;
import net.purplegoose.didnb.enums.GameEvent;

import java.util.Objects;

@AllArgsConstructor
@Slf4j
public class NotificationCommand extends Command {
    public static final String COMMAND = "notification";
    public static final SlashCommandData slashCommandData = Commands.slash(COMMAND, "Enables or disables the receiving of a specific notification in that channel.");
    private static final String GAME_EVENT_OPTION = "game_event_option";
    static {
        OptionData optionData = new OptionData(OptionType.STRING, GAME_EVENT_OPTION, "Select the event for that you like to get notifications.");
        for (GameEvent value : GameEvent.values()) {
            if (value.equals(GameEvent.HEAD_UP) || value.equals(GameEvent.MESSAGE)) {
                continue;
            }
            optionData.addChoice(value.rawName, value.languageKey);
        }
        slashCommandData.addOptions(optionData);
    }

    private final ClientCache cache;
    private final DatabaseRequests databaseRequests;

    @Override
    public void performCommand(SlashCommandInteractionEvent event) {
        GameEvent gameEvent = getGameEvent(event);
        String channelID = event.getChannel().getId();
        String guildID = Objects.requireNonNull(event.getGuild()).getId();
        ClientChannel clientChannel = new ClientChannel(guildID, channelID, gameEvent);
        ClientGuild clientGuild = cache.getClientGuildByID(guildID);
        if (clientGuild.doChannelExists(clientChannel)) {
            //todo: add new message 'channel is already receiving that notification'
            replyWithMessageToUser(event, "'channel is already receiving that notification'", true);
            log.error("{} used /{} in {}. Channel already receiving that notification.",
                    event.getUser().getName(), event.getFullCommandName(), channelID);
            return;
        }

        clientGuild.addChannel(clientChannel);
        databaseRequests.insertClientGuild(clientGuild);
        //todo: add new message 'channel receiving that notification now'
        replyWithMessageToUser(event, "'channel receiving that notification now'", true);
        log.info("{} used /{} in {}.", event.getUser().getName(), event.getFullCommandName(), channelID);
    }

    private GameEvent getGameEvent(SlashCommandInteractionEvent event) {
        OptionMapping optionMapping = event.getOption(GAME_EVENT_OPTION);
        if (optionMapping == null) return null;
        String gameEventString = optionMapping.getAsString();
        return GameEvent.findGameEventByRawName(gameEventString);
    }
}