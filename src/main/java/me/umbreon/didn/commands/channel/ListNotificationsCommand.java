package me.umbreon.didn.commands.channel;

import me.umbreon.didn.commands.IClientCommand;
import me.umbreon.didn.enums.GameEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @author Umbreon Majora
 * Show's a list of all available game events.
 * Command: /listNotifications
 */
public class ListNotificationsCommand implements IClientCommand {

    private final Logger logger = LoggerFactory.getLogger(ListNotificationsCommand.class);

    private final MessageEmbed notificationListEmbed;

    public ListNotificationsCommand() {
        this.notificationListEmbed = buildNotificationListEmbed();
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        String guildID = Objects.requireNonNull(event.getGuild()).getId();
        String guildName = event.getGuild().getName();
        String executingUser = getFullUsernameWithDiscriminator(event.getUser());
        String textChannelName = event.getChannel().getName();
        String textChannelID = event.getChannel().getName();

        createLog(logger, guildID, executingUser + " used /listnotifications in " + guildName + "(" +
                guildID + ") on " + textChannelName + "(" + textChannelID + ").");
        replyEphemeralToUser(event, notificationListEmbed);
    }

    private MessageEmbed buildNotificationListEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Available Notifications:");
        for (GameEvent gameEvent : GameEvent.values())
           embedBuilder.addField(gameEvent.rawName, gameEvent.description, false);
        embedBuilder.setFooter("purplegoose.net");
        return embedBuilder.build();
    }

}
