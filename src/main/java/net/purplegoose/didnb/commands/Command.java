package net.purplegoose.didnb.commands;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.purplegoose.didnb.annotations.CommandAnnotation;
import net.purplegoose.didnb.data.LoggingInformation;

@Slf4j
public abstract class Command implements ICommand {

    public void createUseLogEntry(LoggingInformation logInfo, Class<?> clazz) {
        log.info("{} used /{}. Guild: {}({}). Channel: {}({})",
                logInfo.getExecutor(), clazz.getAnnotation(CommandAnnotation.class).name(),
                logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
    }

    public void createExceptionErrorLogEntry(LoggingInformation logInfo, Class<?> clazz, String error, Exception e) {
        log.error("{} used /{} and a error occurred! Error: {} Guild: {}({}). Channel: {}({})",
                logInfo.getExecutor(), clazz.getAnnotation(CommandAnnotation.class).name(), error,
                logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID(), e);
    }

    public void createErrorLogEntry(LoggingInformation logInfo, Class<?> clazz, String error) {
        log.error("{} used /{} and a error occurred! Error: {} Guild: {}({}). Channel: {}({})",
                logInfo.getExecutor(), clazz.getAnnotation(CommandAnnotation.class).name(), error,
                logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
    }

    public void replyWithMessageToUser(SlashCommandInteractionEvent event, String message, boolean ephemeral) {
        event.getHook().sendMessage(message).setEphemeral(ephemeral).queue();
    }

    public void replyWithEmbedToUser(SlashCommandInteractionEvent event, MessageEmbed embed, boolean ephemeral) {
        event.getHook().sendMessageEmbeds(embed).setEphemeral(ephemeral).queue();
    }

}
