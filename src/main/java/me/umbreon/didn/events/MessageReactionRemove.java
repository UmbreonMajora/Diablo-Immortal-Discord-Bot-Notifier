package me.umbreon.didn.events;

import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.data.ReactionRole;
import me.umbreon.didn.enums.Language;
import me.umbreon.didn.languages.LanguageController;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageReactionRemove extends ListenerAdapter implements IClientEvent {

    private final Logger LOGGER = LoggerFactory.getLogger(MessageReactionRemove.class);
    private final GuildsCache guildsCache;

    public MessageReactionRemove(GuildsCache guildsCache) {
        this.guildsCache = guildsCache;
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        if (isChannelTypeNotTextChannel(event.getChannel())) {
            return;
        }

        User user = event.getUser();
        if (isUserBot(user)) {
            return;
        }

        String messageID = event.getMessageId();
        String guildID = event.getGuild().getId();
        Emoji emoji = event.getEmoji();
        String emojiCode = getEmojiCode(emoji);

        if (!doReactionRoleExists(messageID, emojiCode, guildID)) {
            return;
        }

        ReactionRole reactionRole = guildsCache.getClientGuildByID(guildID).getReactionRoleByMessageIDAndEmojiID(messageID, emojiCode);
        if (reactionRole == null) {
            createLog(LOGGER, guildID, "Failed to get reaction role.");
        }

        String roleID = reactionRole.getRoleID();
        if (!doRoleStillExists(event.getGuild(), roleID)) {
            createLog(LOGGER, guildID, "Role no longer exists.");
            return;
        }

        Language language = guildsCache.getGuildLanguage(guildID);

        try {
            Role role = getRoleByID(event.getGuild(), roleID);
            event.getGuild().removeRoleFromMember(user, role).queue();
            String roleName = role.getName();
            createLog(LOGGER, guildID, "Removed role " + roleName + " from " + user.getName() + " on " + guildID);
            replyToUserInPrivate(user, String.format(LanguageController.getMessage(language, "REMOVED-ROLE"), roleName));

        } catch (HierarchyException e) {
            createLog(LOGGER, guildID, "Failed to take role because of " + e.getCause());
            replyToUserInPrivate(user, LanguageController.getMessage(language, "FAILED-TO-REMOVE-ROLE"));
        } catch (InsufficientPermissionException e) {
            if (e.getMessage().equals("Cannot perform action due to a lack of Permission. Missing permission: MANAGE_ROLES")) {
                createLog(LOGGER, guildID, "Failed to take role because of " + e.getCause());
                replyToUserInPrivate(user, LanguageController.getMessage(language, "FAILED-TO-REMOVE-ROLE"));
            }
        }
    }

    private boolean doReactionRoleExists(String messageID, String emojiCode, String guildID) {
        return guildsCache.getClientGuildByID(guildID).doReactionRoleExist(messageID, emojiCode);
    }

}
