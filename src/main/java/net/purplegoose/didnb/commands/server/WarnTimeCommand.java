package net.purplegoose.didnb.commands.server;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;

import static net.purplegoose.didnb.utils.CommandsUtil.WARN_TIME_OPTION_NAME;

/**
 * @author Umbreon Majora
 * <p>
 * @Description: Allow's the user to change their time of the warn message
 * <p>
 * @Command: /warntime [Required: warntime]
 */
@Slf4j
@AllArgsConstructor
public class WarnTimeCommand implements IClientCommand {

    private final int INVALID_NEW_WARNTIME = -1;

    private final DatabaseRequests databaseRequests;
    private final GuildsCache guildsCache;

    @Override
    public void runCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        String guildID = logInfo.getGuildID();
        int newWarnTime = getWarnTime(event);
        ClientGuild clientGuild = guildsCache.getClientGuildByID(guildID);
        Language language = clientGuild.getLanguage();

        if (newWarnTime == INVALID_NEW_WARNTIME) {
            log.error("{} used /warntime. Error: Time was not in the given range. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), logInfo.getGuildName(), guildID, logInfo.getChannelName(),
                    logInfo.getChannelID());
            replyEphemeralToUser(event, LanguageController.getMessage(language, "INVALID-WARN-TIME"));
            return;
        }


        int oldWarnTime = clientGuild.getWarnTimeInMinutes();
        updateWarnTime(clientGuild, newWarnTime);

        log.info("{} used /warntime. Old warn time: {}. New warn time: {}. Guild: {}({}). Channel: {}({})",
                logInfo.getExecutor(), oldWarnTime, newWarnTime, logInfo.getGuildName(), guildID, logInfo.getChannelName(),
                logInfo.getChannelID());
        replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "UPDATED-WARN-TIME"), newWarnTime));
    }

    private void updateWarnTime(ClientGuild clientGuild, int warntime) {
        clientGuild.setWarnTimeInMinutes(warntime);
        databaseRequests.updateGuild(clientGuild);
    }

    private int getWarnTime(SlashCommandInteractionEvent event) {
        OptionMapping warnTimeOption = event.getOption(WARN_TIME_OPTION_NAME);
        if (warnTimeOption != null) {
            int warntime = warnTimeOption.getAsInt();
            if (warntime < 0 || warntime > 15) {
                return INVALID_NEW_WARNTIME;
            } else {
                return warntime;
            }
        }
        return INVALID_NEW_WARNTIME;
    }
}
