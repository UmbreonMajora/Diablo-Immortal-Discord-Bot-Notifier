package net.purplegoose.didnb.commands.channel;

import lombok.extern.slf4j.Slf4j;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.NotificationChannel;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.enums.Preset;
import net.purplegoose.didnb.languages.LanguageController;
import net.purplegoose.didnb.utils.CommandsUtil;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.Objects;

/**
 * @author Umbreon Majora
 * Allow's user to quickly apply changes to a notification channel. This command also provides a list with all presets.
 * Command: /preset [Required: preset] [Not required: targetchannel]
 */
@Slf4j
public class PresetCommand implements IClientCommand {

    private final String presetInfoMessage;

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    private final int RETURN_LIST = 0;
    private final int RETURN_INVALID = 1;
    private final int RETURN_SUCCESS = 2;

    public PresetCommand(GuildsCache guildsCache, DatabaseRequests databaseRequests) {
        this.guildsCache = guildsCache;
        this.databaseRequests = databaseRequests;
        presetInfoMessage = getPresetInfoMessage();
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
            log.info("{} executed /preset in an unregistered channel. Guild: {}({}), Channel: {}({})",
                    executingUser, guildName, guildID, targetTextChannelName, targetTextChannelID);
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CHANNEL-NOT-REGISTERED"));
            return;
        }

        String preset = getPreset(event);

        if (preset == null) {
            log.info("{} executed /preset. It failed because the preset was null. Guild: {}({}), Channel: {}({})",
                    executingUser, guildName, guildID, targetTextChannelName, targetTextChannelID);
            replyEphemeralToUser(event, LanguageController.getMessage(language, "PRESET-OPTION-NULL"));
            return;
        }

        NotificationChannel notificationChannel = guildsCache.getClientGuildByID(guildID).getNotificationChannel(targetTextChannelID);
        Preset selectedPreset = Preset.findPresetByChoiceName(preset);

        if (selectedPreset == null) {
            log.info("{} executed /preset. It failed because the preset was null. Guild: {}({}), Channel: {}({})",
                    executingUser, guildName, guildID, targetTextChannelName, targetTextChannelID);
            replyEphemeralToUser(event, LanguageController.getMessage(language, "PRESET-OPTION-NULL"));
            return;
        }

        switch (updateChannel(selectedPreset, notificationChannel)) {
            case RETURN_LIST:
                replyEphemeralToUser(event, presetInfoMessage);
                return;
            case RETURN_INVALID:
                replyEphemeralToUser(event, LanguageController.getMessage(language, "PRESET-OPTION-INVALID"));
                log.info("{} executed /preset. It failed because the preset was invalid. Guild: {}({}), Channel: {}({})",
                        executingUser, guildName, guildID, targetTextChannelName, targetTextChannelID);
                return;
            case RETURN_SUCCESS:
                String rawPresetString = preset.replace("preset", "");
                log.info("{} executed /preset. Applied preset {}. Guild: {}({}), Channel: {}({})",
                        executingUser, rawPresetString, guildName, guildID, targetTextChannelName, targetTextChannelID);
                replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "APPLIED-PRESET-TO-CHANNEL"), rawPresetString, targetTextChannel.getAsMention()));
        }

    }

    private int updateChannel(Preset selectedPreset, NotificationChannel notificationChannel) {
        switch (selectedPreset) {
            case PRESET_SHADOW:
                setShadowPreset(notificationChannel);
                break;
            case PRESET_IMMORTAL:
                setImmortalPreset(notificationChannel);
                break;
            case PRESET_DAILY_ON:
                setDailyEventOnPreset(notificationChannel);
                break;
            case PRESET_DAILY_OFF:
                setDailyEventOffPreset(notificationChannel);
                break;
            case PRESET_OVERWORLD_ON:
                setOverworldOnPreset(notificationChannel);
                break;
            case PRESET_OVERWORLD_OFF:
                setOverworldOffPreset(notificationChannel);
                break;
            case PRESET_LIST:
                return RETURN_LIST;
            default:
                return RETURN_INVALID;
        }
        databaseRequests.updateNotificationChannel(notificationChannel);
        return RETURN_SUCCESS;
    }

    private boolean isTextChannelRegistered(String guildID, String textChannelID) {
        return guildsCache.getClientGuildByID(guildID).isChannelRegistered(textChannelID);
    }

    private String getPreset(SlashCommandInteraction event) {
        OptionMapping presetOption = event.getOption(CommandsUtil.PRESET_OPTION_NAME);
        return presetOption != null ? presetOption.getAsString() : null;
    }

    // Assembly - OFF, ShadowLottery - OFF, Vault - ON
    private void setImmortalPreset(NotificationChannel notificationChannel) {
        notificationChannel.setAssemblyMessageEnabled(false);
        notificationChannel.setVaultMessageEnabled(true);
        notificationChannel.setShadowLotteryMessageEnabled(false);
    }

    // Assembly - ON, Vault - On, ShadowLottery - ON
    private void setShadowPreset(NotificationChannel notificationChannel) {
        notificationChannel.setAssemblyMessageEnabled(true);
        notificationChannel.setVaultMessageEnabled(true);
        notificationChannel.setShadowLotteryMessageEnabled(true);
    }

    // Ancient Arena - ON, Ancient Nightmare - ON, Demon Gates - ON, Haunted Carriage - ON
    private void setOverworldOnPreset(NotificationChannel notificationChannel) {
        notificationChannel.setAncientArenaMessageEnabled(true);
        notificationChannel.setAncientNightmareMessageEnabled(true);
        notificationChannel.setDemonGatesMessageEnabled(true);
        notificationChannel.setHauntedCarriageMessageEnabled(true);
    }

    // Ancient Arena - OFF, Ancient Nightmare - OFF, Demon Gates - OFF, Haunted Carriage - OFF
    private void setOverworldOffPreset(NotificationChannel notificationChannel) {
        notificationChannel.setAncientArenaMessageEnabled(false);
        notificationChannel.setAncientNightmareMessageEnabled(false);
        notificationChannel.setDemonGatesMessageEnabled(false);
        notificationChannel.setHauntedCarriageMessageEnabled(false);
    }

    // Battleground - ON, Wrathborne Invasion - ON
    private void setDailyEventOnPreset(NotificationChannel notificationChannel) {
        notificationChannel.setBattlegroundsMessageEnabled(true);
        notificationChannel.setWrathborneInvasionEnabled(true);
    }

    // Battleground - OFF, Wrathborne Invasion - OFF
    private void setDailyEventOffPreset(NotificationChannel notificationChannel) {
        notificationChannel.setBattlegroundsMessageEnabled(false);
        notificationChannel.setWrathborneInvasionEnabled(false);
    }

    private String getPresetInfoMessage() {
        return "```" + "\n" +
                "Immortal preset" + "\n" +
                "Assembly - OFF, DefendVault - ON, ShadowLottery - OFF, RaidVault - OFF" + "\n" +
                " " + "\n" +
                "Shadow preset" + "\n" +
                "Assembly - ON, DefendVault - OFF, ShadowLottery - ON, RaidVault - ON" + "\n" +
                " " + "\n" +
                "Overworld on preset" + "\n" +
                "Ancient Arena - ON, Ancient Nightmare - ON, Demon Gates - ON, Haunted Carriage - ON" + "\n" +
                " " + "\n" +
                "Overworld off preset" + "\n" +
                "Ancient Arena - OFF, Ancient Nightmare - OFF, Demon Gates - OFF, Haunted Carriage - OFF" + "\n" +
                " " + "\n" +
                "Daily event on preset" + "\n" +
                "Battleground - ON, Wrathborne Invasion - ON" + "\n" +
                " " + "\n" +
                "Daily event off preset" + "\n" +
                "Battleground - off, Wrathborne Invasion - off" + "\n" +
                "```";
    }


}
