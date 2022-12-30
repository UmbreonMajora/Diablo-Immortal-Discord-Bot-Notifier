package me.umbreon.didn.commands;

import me.umbreon.didn.logger.FileLogger;
import me.umbreon.didn.utils.CommandsUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public interface IClientCommand {

    void runCommand(SlashCommandInteractionEvent event);

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

    default void createLog(Logger LOGGER, String guildID, String message) {
        LOGGER.info(message);
        FileLogger.createClientFileLog(message);

        if (guildID != null) {
            FileLogger.createGuildFileLog(guildID, message);
        }
    }

    default boolean isUserPermitted(Member member, String guildAdminRoleID) {
        if (isServerOwner(member)) {
            return true;
        }

        List<Role> roles = member.getRoles();
        if (guildAdminRoleID == null) {
            return doMemberHasDefaultAdminRole(roles);
        }

        return doMemberHasCustomAdminRole(roles, guildAdminRoleID);
    }

    default boolean isServerOwner(final Member member) {
        return member.isOwner();
    }

    default boolean doMemberHasDefaultAdminRole(List<Role> roles) {
        Role tempRole = roles.stream()
                .filter(role -> role.getName().equalsIgnoreCase("Bot Admin"))
                .findFirst()
                .orElse(null);
        return tempRole != null;
    }

    default boolean doMemberHasCustomAdminRole(List<Role> roles, String guildAdminRoleID) {
        Role tempRole = roles.stream()
                .filter(role -> role.getId().equals(guildAdminRoleID))
                .findFirst()
                .orElse(null);
        return tempRole != null;
    }

}
