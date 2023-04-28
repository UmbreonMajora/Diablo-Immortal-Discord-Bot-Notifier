package net.purplegoose.didnb.commands;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.utils.CommandsUtil;
public interface IClientCommand {

    void runCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo);

    default String getFullUsernameWithDiscriminator(User user) {
        return user.getName() + "#" + user.getDiscriminator();
    }

    default void replyEphemeralToUser(SlashCommandInteractionEvent event, String message) {
        event.getHook().sendMessage(message).setEphemeral(true).queue();
    }

    default void replyEphemeralToUser(SlashCommandInteractionEvent event, MessageEmbed messageEmbed) {
        event.getHook().sendMessageEmbeds(messageEmbed).setEphemeral(true).queue();
    }

    default TextChannel getTargetTextChannel(SlashCommandInteraction event) {
        OptionMapping targetChannelOption = event.getOption(CommandsUtil.TARGET_CHANNEL_OPTION_NAME);

        if (targetChannelOption == null) {
            return event.getChannel().asTextChannel();
        } else {
            return targetChannelOption.getAsChannel().asTextChannel();
        }
    }

}
