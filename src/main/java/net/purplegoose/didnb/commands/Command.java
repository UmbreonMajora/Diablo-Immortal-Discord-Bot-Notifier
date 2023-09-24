package net.purplegoose.didnb.commands;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
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

    public void replyToUser(SlashCommandInteractionEvent event, String message, boolean ephermeral) {
        event.reply(message).setEphemeral(ephermeral).queue();
    }

    public boolean isExecutedInTextChannel(SlashCommandInteractionEvent event, LoggingInformation logInfo, Class<?> clazz) {
        if (event.getChannel() instanceof TextChannel && event.getGuild() != null) {
            return true;
        }
        createErrorLogEntry(logInfo, clazz, "Command wasn't executed in a text channel or in a guild.");
        return false;
    }

}
