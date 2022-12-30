package me.umbreon.didn.commands.channel;

import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.commands.IClientCommand;
import me.umbreon.didn.database.DatabaseRequests;
import me.umbreon.didn.enums.Language;
import me.umbreon.didn.languages.LanguageController;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Umbreon Majora
 * Allow's user to unregister a text channel.
 * Command: /unregister [Not required: targetchannel]
 */
public class UnregisterCommand implements IClientCommand {

    private final Logger LOGGER = LoggerFactory.getLogger(UnregisterCommand.class);

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    public UnregisterCommand(GuildsCache guildsCache, DatabaseRequests databaseRequests) {
        this.guildsCache = guildsCache;
        this.databaseRequests = databaseRequests;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        TextChannel targetTextChannel = getTargetTextChannel(event);
        String guildID = event.getGuild().getId(); //Can't be null since it's caught in SlashCommandInteraction.java
        String targetTextChannelID = targetTextChannel.getId();
        User user = event.getUser();
        Language language = guildsCache.getGuildLanguage(guildID);

        if (!isChannelRegistered(guildID, targetTextChannelID)) {
            createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " tried to unregister a channel which is not registered.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CHANNEL-NOT-REGISTERED"));
            return;
        }

        guildsCache.getClientGuildByID(guildID).deleteNotificationChannelByID(targetTextChannelID);
        databaseRequests.deleteNotificationChannelByID(targetTextChannelID);

        createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " unregistered " + targetTextChannelID);
        replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "CHANNEL-UNREGISTERED"), targetTextChannel.getAsMention()));
    }

    private boolean isChannelRegistered(String guildID, String textChannelID) {
        return guildsCache.getClientGuildByID(guildID).isChannelRegistered(textChannelID);
    }
}
