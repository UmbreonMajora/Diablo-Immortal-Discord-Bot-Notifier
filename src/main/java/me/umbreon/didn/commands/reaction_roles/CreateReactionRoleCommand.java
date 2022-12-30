package me.umbreon.didn.commands.reaction_roles;

import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.commands.IClientCommand;
import me.umbreon.didn.data.ReactionRole;
import me.umbreon.didn.database.DatabaseRequests;
import me.umbreon.didn.enums.Language;
import me.umbreon.didn.languages.LanguageController;
import me.umbreon.didn.utils.StringUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.exceptions.ContextException;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.requests.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

import static me.umbreon.didn.utils.CommandsUtil.*;

/**
 * @author Umbreon Majora
 * Allow's user to create reaction roles.
 * Command: /createreactionrole [Required: messageid] [Required: emote] [Required: role]
 */
public class CreateReactionRoleCommand implements IClientCommand {

    private final Logger LOGGER = LoggerFactory.getLogger(CreateReactionRoleCommand.class);
    private final DatabaseRequests databaseRequests;

    private final GuildsCache guildsCache;

    public CreateReactionRoleCommand(DatabaseRequests databaseRequests, GuildsCache guildsCache) {
        this.databaseRequests = databaseRequests;
        this.guildsCache = guildsCache;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        String guildID = event.getGuild().getId(); //Can't be null since it's caught in SlashCommandInteraction.java
        User user = event.getUser();
        Language language = guildsCache.getGuildLanguage(guildID);

        String messageID = getMessageID(event);
        if (messageID == null) {
            createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " tried to use " +
                    "/createreactionrole but the message id was null.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CREATE-REACTION-ROLE-FAILED-MESSAGE-ID"));
            return;
        }

        String emote = getEmote(event);
        if (emote == null) {
            createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " tried to use " +
                    "/createreactionrole but the emote was null.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CREATE-REACTION-ROLE-FAILED-EMOTE"));
            return;
        }

        Role role = getRole(event);
        if (role == null) {
            createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " tried to use " +
                    "/createreactionrole but the role was null.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CREATE-REACTION-ROLE-FAILED-ROLE"));
            return;
        }

        if (hasRoleAdminPermissions(role)) {
            createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " tried to use " +
                    "/createreactionrole but the role as admin permissions.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CREATE-REACTION-ROLE-FAILED-ROLE-TO-HIGH"));
            return;
        }

        MessageChannelUnion channel = event.getChannel();
        if (doMessageNotExist(channel, messageID)) {
            createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " tried to use " +
                    "/createreactionrole but the message could not be found.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CREATE-REACTION-ROLE-FAILED-MESSAGE-NOT-FOUND"));
            return;
        }

        if (isMaxReactionRolesReached(guildID, messageID)) {
            createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " tried to use " +
                    "/createreactionrole but the max amount of reaction roles was reached.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CREATE-REACTION-ROLE-FAILED-MAX-EMOTES-REACHED"));
            return;
        }

        Emoji emoji = Emoji.fromFormatted(emote);
        if (isEmoteAlreadyInUse(channel, messageID, emoji)) {
            createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " tried to use " +
                    "/createreactionrole but the emote was already in use.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CREATE-REACTION-ROLE-FAILED-EMOTE-IN-USE"));
            return;
        }

        String emojiCode = getEmojiCode(emoji);
        if (emojiCode == null) {
            createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " tried to use " +
                    "/createreactionrole but the emoji code was null. (This should not happen!)");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CREATE-REACTION-ROLE-FAILED-EMOJI-CODE-NULL"));
            return;
        }

        String emojiType = emoji.getType().name();
        ReactionRole reactionRole = new ReactionRole(messageID, guildID, emojiCode, emojiType, role.getId());

        AtomicBoolean unknownEmoji = new AtomicBoolean(false);
        channel.retrieveMessageById(messageID).queue(message -> {
            message.addReaction(emoji).queue();
        }, (failure) -> {
            if (failure instanceof ContextException) unknownEmoji.set(true);
        });
        if (unknownEmoji.get()) return;

        guildsCache.getClientGuildByID(guildID).addReactionRole(reactionRole);
        databaseRequests.createReactionRole(reactionRole);
        createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) +
                "added reaction role on message " + messageID + " with emote " + emojiCode + ", type " + emojiType +
                " and role " + role.getName());
        replyEphemeralToUser(event, LanguageController.getMessage(language, "REACTION-ROLE-CREATED"));
    }

    private String getMessageID(SlashCommandInteractionEvent event) {
        OptionMapping messageIdOption = event.getOption(MESSAGE_ID_OPTION_NAME);
        return messageIdOption != null ? messageIdOption.getAsString() : null;
    }

    private String getEmote(SlashCommandInteractionEvent event) {
        OptionMapping emoteOption = event.getOption(EMOTE_OPTION_NAME);
        return emoteOption != null ? emoteOption.getAsString() : null;
    }

    private Role getRole(SlashCommandInteractionEvent event) {
        OptionMapping roleOption = event.getOption(ROLE_OPTION_NAME);
        return roleOption != null ? roleOption.getAsRole() : null;
    }

    private boolean hasRoleAdminPermissions(Role role) {
        return role.hasPermission(Permission.ADMINISTRATOR);
    }

    //Todo: Is there a better option to handle this?
    private boolean doMessageNotExist(MessageChannelUnion channel, String messageID) {
        AtomicBoolean result = new AtomicBoolean(false);

        channel.retrieveMessageById(messageID).queue((message) -> {
        }, (failure) -> {
            if (failure instanceof ErrorResponseException) {
                ErrorResponseException ex = (ErrorResponseException) failure;
                if (ex.getErrorResponse() == ErrorResponse.UNKNOWN_MESSAGE) {
                    result.set(true);
                }
            }
        });

        return result.get();
    }

    private boolean isMaxReactionRolesReached(String guildID, String messageID) {
        return guildsCache.getClientGuildByID(guildID).getReactionRolesAmount(messageID) >= 10;
    }

    private boolean isEmoteAlreadyInUse(MessageChannelUnion channel, String messageID, Emoji emoji) {
        AtomicBoolean result = new AtomicBoolean(false);

        channel.retrieveMessageById(messageID).queue((message) -> {
            for (MessageReaction reaction : message.getReactions()) {

                String emojiAsReactionCode = emoji.getAsReactionCode();
                String messageReactionAsReactionCode = reaction.getEmoji().getAsReactionCode();

                if (emojiAsReactionCode.equals(messageReactionAsReactionCode)) {
                    result.set(true);
                }
            }
        });

        return result.get();
    }

    private String getEmojiType(Emoji emoji) {
        return emoji.getType().name().toLowerCase();
    }

    private String getEmojiCode(Emoji emoji) {
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
}
