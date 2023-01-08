package net.purplegoose.didnb.commands.channel;

import lombok.extern.slf4j.Slf4j;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.NotificationChannel;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Objects;

/**
 * @author Umbreon Majora
 * Allow's user to register a text channel as notification channel.
 * Command: /register [Not required: targetchannel]
 */
@Slf4j
public class RegisterCommand implements IClientCommand {

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    public RegisterCommand(GuildsCache guildsCache, DatabaseRequests databaseRequests) {
        this.guildsCache = guildsCache;
        this.databaseRequests = databaseRequests;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        String executingUser = getFullUsernameWithDiscriminator(event.getUser());

        String guildID = Objects.requireNonNull(event.getGuild()).getId();
        String guildName = event.getGuild().getName();

        TextChannel targetTextChannel = getTargetTextChannel(event);
        String targetTextChannelID = targetTextChannel.getId();
        String targetTextChannelName = targetTextChannel.getName();

        Language language = guildsCache.getGuildLanguage(guildID);

        if (isChannelAlreadyRegistered(guildID, targetTextChannelID)) {
            log.info("{} executed /register in an registered channel. Guild: {}({}), Channel: {}({})",
                    executingUser, guildName, guildID, targetTextChannelName, targetTextChannelID);
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CHANNEL-ALREADY-REGISTERED"));
            return;
        }

        NotificationChannel notificationChannel = new NotificationChannel(targetTextChannelID, guildID);
        guildsCache.getClientGuildByID(guildID).addNewNotificationChannel(notificationChannel);
        databaseRequests.createNotificationChannel(notificationChannel);

        log.info("{} executed /register. Registering new channel... Guild: {}({}), Channel: {}({})",
                executingUser, guildName, guildID, targetTextChannelName, targetTextChannelID);
        replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "CHANNEL-REGISTERED"), targetTextChannel.getAsMention()));
    }

    private boolean isChannelAlreadyRegistered(String guildID, String textChannelID) {
        return guildsCache.getClientGuildByID(guildID).isChannelRegistered(textChannelID);
    }

}
