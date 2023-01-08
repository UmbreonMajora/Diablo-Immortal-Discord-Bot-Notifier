package net.purplegoose.didnb.commands.channel;

import lombok.extern.slf4j.Slf4j;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.enums.GameEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Objects;

/**
 * @author Umbreon Majora
 * Show's a list of all available game events.
 * Command: /listNotifications
 */
@Slf4j
public class ListNotificationsCommand implements IClientCommand {

    private final MessageEmbed notificationListEmbed;

    public ListNotificationsCommand() {
        this.notificationListEmbed = buildNotificationListEmbed();
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        String executingUser = getFullUsernameWithDiscriminator(event.getUser());

        String guildID = Objects.requireNonNull(event.getGuild()).getId();
        String guildName = event.getGuild().getName();

        String textChannelName = event.getChannel().getName();
        String textChannelID = event.getChannel().getName();

        log.info("{} executed /listnotifications. Guild: {}({}), Channel: {}({})",
                executingUser, guildName, guildID, textChannelName, textChannelID);
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
