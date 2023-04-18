package net.purplegoose.didnb.commands.channel;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.enums.GameEvent;

/**
 * @author Umbreon Majora
 * Show's a list of all available game events.
 * Command: /listNotifications
 */
@Slf4j
public class ListNotificationsCommand implements IClientCommand {

    private static final MessageEmbed notificationListEmbed;

    static {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Available Notifications:");
        for (GameEvent gameEvent : GameEvent.values())
            embedBuilder.addField(gameEvent.rawName, gameEvent.description, false);
        embedBuilder.setFooter("purplegoose.net");
        notificationListEmbed = embedBuilder.build();
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        log.info("{} used /listnotifications. Guild: {}({}). Channel: {}({})",
                logInfo.getExecutor(), logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
        replyEphemeralToUser(event, notificationListEmbed);
    }

}
