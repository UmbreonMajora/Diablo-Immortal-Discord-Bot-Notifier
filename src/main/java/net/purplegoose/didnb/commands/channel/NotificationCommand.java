package net.purplegoose.didnb.commands.channel;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.data.NotificationChannel;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.enums.GameEvent;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;

import static net.purplegoose.didnb.utils.CommandsUtil.EVENT_NAME_OPTION_NAME;
import static net.purplegoose.didnb.utils.CommandsUtil.EVENT_TOGGLE_OPTION_NAME;

/**
 * @author Umbreon Majora
 * Allow's user to specify which event notification a channel should get.
 * Command: /notification [Required: event] [Required: eventvalue]
 */
@Slf4j
@AllArgsConstructor
public class NotificationCommand implements IClientCommand {

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    @Override
    public void runCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        String guildID = logInfo.getGuildID();

        TextChannel targetTextChannel = getTargetTextChannel(event);
        String targetTextChannelID = targetTextChannel.getId();
        String targetTextChannelName = targetTextChannel.getName();

        Language language = guildsCache.getLanguageByGuildID(guildID);

        if (!isTextChannelRegistered(guildID, targetTextChannelID)) {
            log.error("{} used /notification. Error: Channel not registered. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), logInfo.getGuildName(), guildID, targetTextChannelName, targetTextChannelID);
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CHANNEL-NOT-REGISTERED"));
            return;
        }

        String selectedEvent = getSelectedEvent(event);
        GameEvent gameEvent = GameEvent.findGameEventByRawName(selectedEvent);
        if (gameEvent == null) {
            log.error("{} used /notification. Error: GameEvent invalid. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), logInfo.getGuildName(), guildID, targetTextChannelName, targetTextChannelID);
            replyEphemeralToUser(event, LanguageController.getMessage(language, "INVALID-EVENT"));
            return;
        }

        boolean eventValue = getEventValue(event);
        updateNotificationChannel(gameEvent, eventValue, guildID, targetTextChannelID);
        log.error("{} used /notification. {} is now {}. Guild: {}({}). Channel: {}({})",
                logInfo.getExecutor(), gameEvent.rawName, eventValue, logInfo.getGuildName(), guildID,
                targetTextChannelName, targetTextChannelID);
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
            case ANCIENT_ARENA -> notificationChannel.setAncientArenaMessageEnabled(gameEventValue);
            case VAULT -> notificationChannel.setVaultMessageEnabled(gameEventValue);
            case HEAD_UP -> notificationChannel.setEventWarnMessage(gameEventValue);
            case MESSAGE -> notificationChannel.setEventMessageEnabled(gameEventValue);
            case ASSEMBLY -> notificationChannel.setAssemblyMessageEnabled(gameEventValue);
            case DEMON_GATES -> notificationChannel.setDemonGatesMessageEnabled(gameEventValue);
            case BATTLEGROUNDS -> notificationChannel.setBattlegroundsMessageEnabled(gameEventValue);
            case SHADOW_LOTTERY -> notificationChannel.setShadowLotteryMessageEnabled(gameEventValue);
            case HAUNTED_CARRIAGE -> notificationChannel.setHauntedCarriageMessageEnabled(gameEventValue);
            case ANCIENT_NIGHTMARE -> notificationChannel.setAncientNightmareMessageEnabled(gameEventValue);
            case WRATHBORNE_INVASION -> notificationChannel.setWrathborneInvasionEnabled(gameEventValue);
            case ON_SLAUGHT -> notificationChannel.setOnSlaughtMessagesEnabled(gameEventValue);
            case TOWER_OF_VICTORY -> notificationChannel.setTowerOfVictoryMessagesEnabled(gameEventValue);
            case SHADOW_WAR -> notificationChannel.setShadowWarMessagesEnabled(gameEventValue);
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
        OptionMapping eventValueOption = event.getOption(EVENT_TOGGLE_OPTION_NAME);
        return eventValueOption != null && eventValueOption.getAsBoolean();
    }
}
