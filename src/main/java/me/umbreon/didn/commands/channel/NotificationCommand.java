package me.umbreon.didn.commands.channel;

import lombok.extern.slf4j.Slf4j;
import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.commands.IClientCommand;
import me.umbreon.didn.data.NotificationChannel;
import me.umbreon.didn.database.DatabaseRequests;
import me.umbreon.didn.enums.GameEvent;
import me.umbreon.didn.enums.Language;
import me.umbreon.didn.languages.LanguageController;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Objects;

import static me.umbreon.didn.utils.CommandsUtil.EVENT_NAME_OPTION_NAME;
import static me.umbreon.didn.utils.CommandsUtil.EVENT_VALUE_OPTION_NAME;

/**
 * @author Umbreon Majora
 * Allow's user to specify which event notification a channel should get.
 * Command: /notification [Required: event] [Required: eventvalue]
 */
@Slf4j
public class NotificationCommand implements IClientCommand {

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    public NotificationCommand(GuildsCache guildsCache, DatabaseRequests databaseRequests) {
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

        if (!isTextChannelRegistered(guildID, targetTextChannelID)) {
            log.info("{} executed /info in an unregistered channel. Guild: {}({}), Channel: {}({})",
                    executingUser, guildName, guildID, targetTextChannelName, targetTextChannelID);
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CHANNEL-NOT-REGISTERED"));
            return;
        }

        String selectedEvent = getSelectedEvent(event);
        GameEvent gameEvent = GameEvent.findGameEventByRawName(selectedEvent);
        if (gameEvent == null) {
            log.info("{} executed /notification with invalid gameevent. Guild: {}({}), Channel: {}({})",
                    executingUser, guildName, guildID, targetTextChannelName, targetTextChannelID);
            replyEphemeralToUser(event, LanguageController.getMessage(language, "INVALID-EVENT"));
            return;
        }

        boolean eventValue = getEventValue(event);
        updateNotificationChannel(gameEvent, eventValue, guildID, targetTextChannelID);
        log.info("{} executed /notification. Changed {} to {}. Guild: {}({}), Channel: {}({})",
                executingUser, gameEvent.rawName, eventValue, guildName, guildID, targetTextChannelName, targetTextChannelID);
        String replyMessage = getReplyMessage(gameEvent, eventValue, language);
        replyEphemeralToUser(event, replyMessage);
    }

    private String getReplyMessage(GameEvent gameEvent, boolean eventValue, Language language) {
        if (eventValue) {
            return String.format(LanguageController.getMessage(language, "ENABLED-EVENT"), gameEvent.rawName);
        } else {
            return String.format(LanguageController.getMessage(language, "DISABLED-EVENT"), gameEvent.rawName);
        }
    }

    private void updateNotificationChannel(GameEvent gameEvent, boolean gameEventValue, String guildID, String textChannelID) {
        NotificationChannel notificationChannel = guildsCache.getClientGuildByID(guildID).getNotificationChannel(textChannelID);
        switch (gameEvent) {
            case ANCIENT_ARENA:
                notificationChannel.setAncientArenaMessageEnabled(gameEventValue);
                break;
            case VAULT:
                notificationChannel.setVaultMessageEnabled(gameEventValue);
                break;
            case HEAD_UP:
                notificationChannel.setEventHeadUpEnabled(gameEventValue);
                break;
            case MESSAGE:
                notificationChannel.setEventMessageEnabled(gameEventValue);
                break;
            case ASSEMBLY:
                notificationChannel.setAssemblyMessageEnabled(gameEventValue);
                break;
            case DEMON_GATES:
                notificationChannel.setDemonGatesMessageEnabled(gameEventValue);
                break;
            case BATTLEGROUNDS:
                notificationChannel.setBattlegroundsMessageEnabled(gameEventValue);
                break;
            case SHADOW_LOTTERY:
                notificationChannel.setShadowLotteryMessageEnabled(gameEventValue);
                break;
            case HAUNTED_CARRIAGE:
                notificationChannel.setHauntedCarriageMessageEnabled(gameEventValue);
                break;
            case ANCIENT_NIGHTMARE:
                notificationChannel.setAncientNightmareMessageEnabled(gameEventValue);
                break;
            case WRATHBORNE_INVASION:
                notificationChannel.setWrathborneInvasionEnabled(gameEventValue);
                break;
            case ON_SLAUGHT:
                notificationChannel.setOnSlaughtMessagesEnabled(gameEventValue);
                break;
        }
        databaseRequests.updateNotificationChannel(notificationChannel);
    }

    private boolean isTextChannelRegistered(String guildID, String textChannelID) {
        return guildsCache.getClientGuildByID(guildID).isChannelRegistered(textChannelID);
    }

    private String getSelectedEvent(SlashCommandInteractionEvent event) {
        OptionMapping selectedEventOption = event.getOption(EVENT_NAME_OPTION_NAME);
        return selectedEventOption != null ? selectedEventOption.getAsString() : null;
    }

    private boolean getEventValue(SlashCommandInteractionEvent event) {
        OptionMapping eventValueOption = event.getOption(EVENT_VALUE_OPTION_NAME);
        return eventValueOption != null && eventValueOption.getAsBoolean();
    }
}
