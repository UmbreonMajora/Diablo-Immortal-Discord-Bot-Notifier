package me.umbreon.didn.commands.custom_notifications;

import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.commands.IClientCommand;
import me.umbreon.didn.data.CustomNotification;
import me.umbreon.didn.enums.Language;
import me.umbreon.didn.languages.LanguageController;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Objects;

import static me.umbreon.didn.utils.CommandsUtil.CUSTOM_MESSAGE_ID_OPTION_NAME;

/**
 * @author Umbreon Majora
 * Allow's user to see all information about a custom notification.
 * /messageinfo [Required: custommessageid]
 */
public class MessageInfoCommand implements IClientCommand {

    private final Logger LOGGER = LoggerFactory.getLogger(MessageInfoCommand.class);

    private final GuildsCache guildsCache;

    public MessageInfoCommand(GuildsCache guildsCache) {
        this.guildsCache = guildsCache;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        String guildID = Objects.requireNonNull(event.getGuild()).getId();
        Language language = guildsCache.getGuildLanguage(guildID);
        String executingUser = getFullUsernameWithDiscriminator(event.getUser());

        int customMessageID = getCustomMessageID(event);
        if (customMessageID == -1) {
            createLog(LOGGER, guildID, executingUser + " to get info about a custom notification but the id was invalid.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "INFO-CUSTOM-MESSAGE-FAILED-ID-NULL"));
            return;
        }

        replyEphemeralToUser(event, buildCustomMessageInfoEmbed(guildID, customMessageID));
    }

    private int getCustomMessageID(SlashCommandInteractionEvent event) {
        OptionMapping customMessageIdOption = event.getOption(CUSTOM_MESSAGE_ID_OPTION_NAME);
        return customMessageIdOption != null ? customMessageIdOption.getAsInt() : -1;
    }

    private MessageEmbed buildCustomMessageInfoEmbed(String guildID, final int customMessageID) {
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
