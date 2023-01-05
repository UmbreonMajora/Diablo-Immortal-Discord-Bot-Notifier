package me.umbreon.didn.events;

import me.umbreon.didn.logger.FileLogger;
import me.umbreon.didn.utils.StringUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.slf4j.Logger;

public interface IClientEvent {

    default boolean isChannelTypeNotTextChannel(Channel channel) {
        return channel.getType().getId() != 0;
    }

    default boolean isUserBot(User user) {
        if (user == null) {
            //Small workaround, user can be null. If so just return true so the event will be cancelled.
            return true;
        }

        return user.isBot();
    }

    default String getEmojiCode(Emoji emoji) {
        String emojiReactionCode = emoji.getAsReactionCode();
        String emojiCode;

        switch (getEmojiType(emoji)) {
            case "unicode":
                emojiCode = StringUtil.convertEmojiToUnicode(emojiReactionCode);
                break;
            case "custom":
                emojiCode = emojiReactionCode;
                break;
            default:
                emojiCode = null;
                break;
        }

        return emojiCode;
    }

    default String getEmojiType(Emoji emoji) {
        return emoji.getType().name().toLowerCase();
    }

    default boolean doRoleStillExists(Guild guild, String roleID) {
        return guild.getRoleById(roleID) != null;
    }

    default Role getRoleByID(Guild guild, String roleID) {
        return guild.getRoleById(roleID);
    }

    default void createLog(Logger LOGGER, String guildID, String message) {
        LOGGER.info(message);
        FileLogger.createClientFileLog(message);

        if (guildID != null) {
            FileLogger.createGuildFileLog(guildID, message);
        }
    }

    default void replyToUserInPrivate(User user, String message) {
        user.openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(message).queue();
        });
    }

}
