package net.purplegoose.didnb.commands.custom_notifications;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.CustomNotification;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;
import net.purplegoose.didnb.utils.StringUtil;

import java.awt.*;
import java.util.Objects;

import static net.purplegoose.didnb.utils.CommandsUtil.CUSTOM_MESSAGE_ID_OPTION_NAME;

/**
 * @author Umbreon Majora
 * Allow's user to see all information about a custom notification.
 * /messageinfo [Required: custommessageid]
 */
@Slf4j
@AllArgsConstructor
public class MessageInfoCommand implements IClientCommand {

    private final GuildsCache guildsCache;

    @Override
    public void runCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        String guildID = logInfo.getGuildID();
        Language language = guildsCache.getGuildLanguage(guildID);

        String customMessageID = getCustomMessageID(event);
        if (Objects.equals(customMessageID, StringUtil.FAILED_MESSAGE)) {
            log.error("{} used /editmessage. Error: ID invalid. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
            replyEphemeralToUser(event, LanguageController.getMessage(language, "INFO-CUSTOM-MESSAGE-FAILED-ID-NULL"));
            return;
        }

        replyEphemeralToUser(event, buildCustomMessageInfoEmbed(guildID, customMessageID));
    }

    private String getCustomMessageID(SlashCommandInteractionEvent event) {
        OptionMapping customMessageIdOption = event.getOption(CUSTOM_MESSAGE_ID_OPTION_NAME);
        return customMessageIdOption != null ? customMessageIdOption.getAsString() : StringUtil.FAILED_MESSAGE;
    }

    private MessageEmbed buildCustomMessageInfoEmbed(String guildID, final String customMessageID) {
        CustomNotification customNotification = guildsCache.getClientGuildByID(guildID).getCustomNotificationByID(customMessageID);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.ORANGE);
        embedBuilder.setTitle(LanguageController.getMessage(Language.ENGLISH, "TEXT-YOUR-CUSTOM-NOTIFICATIONS"));
        String textChannelMention = "<#" + customNotification.getChannelID() + ">";
        String time = customNotification.getWeekday() + " " + customNotification.getTime();
        embedBuilder.addField("TextChannel:", textChannelMention, true);
        embedBuilder.addField("Message:", customNotification.getMessage(), true);
        embedBuilder.addField("Time:", time, true);
        embedBuilder.addField("Repeating:", String.valueOf(customNotification.isRepeating()), true);
        return embedBuilder.build();
    }
}
