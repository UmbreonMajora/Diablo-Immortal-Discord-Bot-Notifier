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

    void addMessageMention(StringBuilder sb, NotificationChannel channel, Guild guild) {
        if (channel == null) {
            return;
        }

        String mentionID = channel.getMentionRoleID();

        if (mentionID == null) {
            return; // add no mention role if null.
        }

        switch (mentionID.toUpperCase()) {
            case "HERE" -> sb.append(NEW_LINE).append("@here");
            case "EVERYONE" -> sb.append(NEW_LINE).append("@everyone");
            default -> {
                Role mentionRole = guild.getRoleById(mentionID);
                if (mentionRole != null) {
                    sb.append(NEW_LINE).append(mentionRole.getAsMention());
                } else {
                    sb.append(NEW_LINE).append("Failed to append mention role. Please re-set your mention role.");
                    String guildID = guild.getId();
                    String guildName = guild.getName();
                    String textChannelID = channel.getTextChannelID();
                    log.info("Failed to append mention role for guild " +
                            guildName + "(" + guildID + ") in " + textChannelID);
                }
            }
        }
    }

    void sendMessageWithAutoDelete(TextChannel textChannel, String messageContext, int deleteTimeInHours) {
        textChannel.sendMessage(messageContext).queue(message ->
                message.delete().queueAfter(deleteTimeInHours, TimeUnit.HOURS));
    }

    void sendMessageWithoutAutoDelete(TextChannel textChannel, String messageContext) {
        textChannel.sendMessage(messageContext).queue();
    }

}
