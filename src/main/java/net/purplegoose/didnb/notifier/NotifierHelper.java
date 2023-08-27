package net.purplegoose.didnb.notifier;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.purplegoose.didnb.data.NotificationChannel;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;

import java.util.concurrent.TimeUnit;

import static net.purplegoose.didnb.utils.StringUtil.NEW_LINE;

@Slf4j
public class NotifierHelper {

    private static final String MENTION_HERE = "HERE";
    private static final String MENTION_EVERYONE = "EVERYONE";

    void addMessageMention(StringBuilder sb, NotificationChannel channel, Guild guild, Language language) {
        if (channel == null) {
            log.info("No notification channel specified for guild: {}. Skipping mention.", guild.getId());
            return;
        }

        String mentionID = channel.getMentionRoleID();

        if (mentionID == null) {
            log.info("No mention role specified for channel: {}.", channel.getTextChannelID());
            return;
        }

        switch (mentionID.toUpperCase()) {
            case MENTION_HERE -> sb.append(NEW_LINE).append("@here");
            case MENTION_EVERYONE -> sb.append(NEW_LINE).append("@everyone");
            default -> {
                Role mentionRole = guild.getRoleById(mentionID);
                if (mentionRole != null) {
                    sb.append(NEW_LINE).append(mentionRole.getAsMention());
                } else {
                    handleMentionRoleError(sb, guild, channel, language);
                }
            }
        }
    }

    void sendMessage(TextChannel textChannel, String messageContext) {
        textChannel.sendMessage(messageContext).queue();
    }

    void sendTimedMessage(TextChannel textChannel, String messageContext, int deleteTimeInHours) {
        textChannel.sendMessage(messageContext).queue(message ->
                message.delete().queueAfter(deleteTimeInHours, TimeUnit.HOURS));
    }

    private void handleMentionRoleError(StringBuilder sb, Guild guild, NotificationChannel channel, Language language) {
        String guildID = guild.getId();
        String guildName = guild.getName();
        String textChannelID = channel.getTextChannelID();
        sb.append(NEW_LINE).append(LanguageController.getMessage(language, "FAILED-TO-APPEND-MENTION-ROLE"));
        log.error("Failed to append mention role for guild {} ({}) in {}", guildName, guildID, textChannelID);
    }

}
