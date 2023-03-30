package net.purplegoose.didnb.commands.server;

import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import static net.purplegoose.didnb.utils.CommandsUtil.WARN_TIME_OPTION_NAME;

/**
 * @author Umbreon Majora
 * Allow's the user to change their time of the warn message
 * Command: /warntime [Required: warntime]
 */
public class WarnTimeCommand  implements IClientCommand {

    private final DatabaseRequests databaseRequests;
    private final GuildsCache guildsCache;

    public WarnTimeCommand(DatabaseRequests databaseRequests, GuildsCache guildsCache) {
        this.databaseRequests = databaseRequests;
        this.guildsCache = guildsCache;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        String guildID = event.getGuild().getId();//Can't be null since it's caught in SlashCommandInteraction.java
        int warntime = getWarnTime(event);
        if (warntime == -1) {
            return; //invalid
        }

        ClientGuild clientGuild = guildsCache.getClientGuildByID(guildID);
        clientGuild.setWarnTimeInMinutes(warntime);
        databaseRequests.updateGuild(clientGuild);
        replyEphemeralToUser(event, "Warn time changed to " + warntime);
    }

    private int getWarnTime(SlashCommandInteractionEvent event) {
        OptionMapping warnTimeOption = event.getOption(WARN_TIME_OPTION_NAME);
        if (warnTimeOption != null) {
            int warntime = warnTimeOption.getAsInt();
            if (warntime < 0 || warntime > 15) {
                return -1;
            } else {
                return warntime;
            }
        }
        return -1;
    }
}
