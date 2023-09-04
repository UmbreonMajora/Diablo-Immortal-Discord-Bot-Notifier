package net.purplegoose.didnb.commands;

import lombok.extern.slf4j.Slf4j;
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

}
