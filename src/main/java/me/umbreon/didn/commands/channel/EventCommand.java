package me.umbreon.didn.commands.channel;

import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.commands.IClientCommand;
import me.umbreon.didn.data.NotificationChannel;
import me.umbreon.didn.database.DatabaseRequests;
import me.umbreon.didn.enums.GameEvent;
import me.umbreon.didn.enums.Language;
import me.umbreon.didn.languages.LanguageController;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static me.umbreon.didn.utils.CommandsUtil.EVENT_NAME_OPTION_NAME;
import static me.umbreon.didn.utils.CommandsUtil.EVENT_VALUE_OPTION_NAME;

/**
 * @author Umbreon Majora
 * Allow's user to specify which event notification a channel should get.
 * Command: /event [Required: event] [Required: eventvalue]
 */
public class EventCommand implements IClientCommand {

    private final Logger LOGGER = LoggerFactory.getLogger(EventCommand.class);

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    public EventCommand(GuildsCache guildsCache, DatabaseRequests databaseRequests) {
        this.guildsCache = guildsCache;
        this.databaseRequests = databaseRequests;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        TextChannel targetTextChannel = getTargetTextChannel(event);
        String guildID = Objects.requireNonNull(event.getGuild()).getId();
        String targetTextChannelID = targetTextChannel.getId();
        User user = event.getUser();
        Language language = guildsCache.getGuildLanguage(guildID);

        if (!isTextChannelRegistered(guildID, targetTextChannelID)) {
            createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " tried to use /info on an unregistered channel.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CHANNEL-NOT-REGISTERED"));
            return;
        }

        String selectedEvent = getSelectedEvent(event);
        GameEvent gameEvent = GameEvent.findGameEventByRawName(selectedEvent);

        if (gameEvent == null) {
            createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " tried to use /event on " + targetTextChannelID + " but the given event couldn't be found.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "INVALID-EVENT"));
            return;
        }

        boolean eventValue = getEventValue(event);
        updateNotificationChannel(gameEvent, eventValue, guildID, targetTextChannelID);
        createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " changed the event value of " + gameEvent.rawName + " to " + eventValue);
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

    //todo: returning null may produce null pointer, fix?
    private boolean getEventValue(SlashCommandInteractionEvent event) {
        OptionMapping eventValueOption = event.getOption(EVENT_VALUE_OPTION_NAME);
        return eventValueOption != null && eventValueOption.getAsBoolean();
    }
}
