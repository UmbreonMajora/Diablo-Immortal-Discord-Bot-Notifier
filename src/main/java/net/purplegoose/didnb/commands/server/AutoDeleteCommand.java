package net.purplegoose.didnb.commands.server;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.data.LoggingInformation;

import static net.purplegoose.didnb.utils.CommandsUtil.AUTO_DELETE_TIME_OPTION_NAME;

/**
 * @author Umbreon Majora
 * <p>
 * @Command: /autodelete [Not Required: TimeInHours]
 * <p>
 * @Description Allow's the user to enable or disable the auto-deletion of their notification messages.
 * The command without any following parameters will disable or enable the auto-deletion. If the command is used
 * while auto-deletion deactivated, it will activate and vice versa.
 * The command with a following numeric parameter will set a new value of when the message should get deleted.
 */
@Slf4j
@AllArgsConstructor
public class AutoDeleteCommand implements IClientCommand {

    private static final int MAX_AUTO_DELETE_TIME = 24;
    private static final int AUTO_DELETE_TIME_EMPTY = -59;
    private final GuildsCache guildsCache;

    @Override
    public void runCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        int autoDeleteTimeInHours = getAutoDeleteTimeInHours(event);
        String guildID = logInfo.getGuildID();
        ClientGuild clientGuild = guildsCache.getClientGuildByID(guildID);
        if (autoDeleteTimeInHours == AUTO_DELETE_TIME_EMPTY) {
            toggleAutoDeletion(clientGuild);
            replyEphemeralToUser(event, "Toggled auto-deletion. New value is " + clientGuild.isAutoDeleteEnabled() + ".");
            log.info("{} used /autodelete. Toggled auto-deletion. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), logInfo.getGuildName(), guildID, logInfo.getChannelName(), logInfo.getChannelID());
            return;
        }

        if (isNewAutoDeletionTimeGreaterThanMaximum(autoDeleteTimeInHours)) {
            log.error("{} used /autodelete. Error: Time was greater than the maximum. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), logInfo.getGuildName(), guildID, logInfo.getChannelName(), logInfo.getChannelID());
            replyEphemeralToUser(event, "The auto-deletion time cant be greater than " + MAX_AUTO_DELETE_TIME + ".");
            return;
        }

        int oldAutoDeleteTimeInHours = clientGuild.getAutoDeleteTimeInHours();
        changeAutoDeleteTime(clientGuild, autoDeleteTimeInHours);
        log.info("{} used /autodelete. Changed value from {} to {}. Changed Guild: {}({}). Channel: {}({})",
                logInfo.getExecutor(), oldAutoDeleteTimeInHours, autoDeleteTimeInHours, logInfo.getGuildName(),
                guildID, logInfo.getChannelName(), logInfo.getChannelID());
        replyEphemeralToUser(event, "Changed auto-deletion value from " + oldAutoDeleteTimeInHours + " to " + autoDeleteTimeInHours);
    }

    private boolean isNewAutoDeletionTimeGreaterThanMaximum(int newAutoDeleteTimeInHours) {
        return newAutoDeleteTimeInHours > MAX_AUTO_DELETE_TIME;
    }

    private void toggleAutoDeletion(ClientGuild clientGuild) {
        clientGuild.setAutoDeleteEnabled(!clientGuild.isAutoDeleteEnabled());
    }

    private void changeAutoDeleteTime(ClientGuild clientGuild, int autoDeleteTimeInHours) {
        clientGuild.setAutoDeleteTimeInHours(autoDeleteTimeInHours);
    }

    private int getAutoDeleteTimeInHours(SlashCommandInteractionEvent event) {
        OptionMapping autoDeleteTimeOption = event.getOption(AUTO_DELETE_TIME_OPTION_NAME);
        return autoDeleteTimeOption != null ? autoDeleteTimeOption.getAsInt() : AUTO_DELETE_TIME_EMPTY;
    }
}
