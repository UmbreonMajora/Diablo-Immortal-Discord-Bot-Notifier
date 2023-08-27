package net.purplegoose.didnb.notifier;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.purplegoose.didnb.data.NotificationChannel;

import java.util.concurrent.TimeUnit;

import static net.purplegoose.didnb.utils.StringUtil.NEW_LINE;

@Slf4j
public class NotifierHelper {

    private static final String MENTION_HERE = "HERE";
    private static final String MENTION_EVERYONE = "EVERYONE";
    // todo: add this message to language file.
    private static final String FAILED_TO_APPEND_MENTION_ROLE = "Failed to append mention role. Please re-set your mention role.";

    void addMessageMention(StringBuilder sb, NotificationChannel channel, Guild guild) {
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
                    handleMentionRoleError(sb, guild, channel);
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

    private void handleMentionRoleError(StringBuilder sb, Guild guild, NotificationChannel channel) {
        String guildID = guild.getId();
        String guildName = guild.getName();
        String textChannelID = channel.getTextChannelID();
        sb.append(NEW_LINE).append(FAILED_TO_APPEND_MENTION_ROLE);
        log.error("Failed to append mention role for guild {} ({}) in {}", guildName, guildID, textChannelID);
    }

}
