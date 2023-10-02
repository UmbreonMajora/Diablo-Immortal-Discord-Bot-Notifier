package net.purplegoose.didnb.notifier;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.purplegoose.didnb.data.NotificationChannel;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.exeption.InvalidMentionException;

import java.util.concurrent.TimeUnit;

import static net.purplegoose.didnb.utils.StringUtil.NEW_LINE;

@Slf4j
public class NotifierHelper {
    private static final String MENTION_HERE = "HERE";
    private static final String MENTION_EVERYONE = "EVERYONE";

    void addMessageMention(StringBuilder sb, NotificationChannel channel, Guild guild, Language language) throws InvalidMentionException {
        if (channel == null) {
            throw new InvalidMentionException(String.format("Failed to append mention for notification. Guild%s(%s)", guild.getId(), guild.getName()));
        }

        String mentionID = channel.getMentionRoleID();

        // If the mention id is null, the user does not want a mention.
        if (mentionID != null) {
            switch (mentionID.toUpperCase()) {
                case MENTION_HERE -> sb.append(NEW_LINE).append("@here");
                case MENTION_EVERYONE -> sb.append(NEW_LINE).append("@everyone");
                default -> {
                    Role mentionRole = guild.getRoleById(mentionID);
                    if (mentionRole != null) {
                        sb.append(NEW_LINE).append(mentionRole.getAsMention());
                    }
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
}