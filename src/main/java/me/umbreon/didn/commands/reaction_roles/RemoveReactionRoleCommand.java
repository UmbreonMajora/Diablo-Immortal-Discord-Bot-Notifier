package me.umbreon.didn.commands.reaction_roles;

import emoji4j.EmojiUtils;
import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.commands.IClientCommand;
import me.umbreon.didn.commands.channel.PresetCommand;
import me.umbreon.didn.data.ReactionRole;
import me.umbreon.didn.enums.Language;
import me.umbreon.didn.languages.LanguageController;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static me.umbreon.didn.utils.CommandsUtil.EMOTE_OPTION_NAME;
import static me.umbreon.didn.utils.CommandsUtil.MESSAGE_ID_OPTION_NAME;

/**
 * @author Umbreon Majora
 * Removes a reaction role from a message.
 * Command: /removereactionrole [Required: MessageID] [Required: Emoji]
 */
public class RemoveReactionRoleCommand implements IClientCommand {

    private final Logger LOGGER = LoggerFactory.getLogger(PresetCommand.class);

    private final GuildsCache guildsCache;

    public RemoveReactionRoleCommand(GuildsCache guildsCache) {
        this.guildsCache = guildsCache;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        String guildID = event.getGuild().getId(); //Can't be null since it's caught in SlashCommandInteraction.java
        User user = event.getUser();
        Language language = guildsCache.getGuildLanguage(guildID);

        String messageID = getMessageID(event);
        if (messageID == null) {
            createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " tried to use /removereactionrole " +
                    "but it failed because message id was null.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "REMOVE-REACTION-ROLE-FAILED-MESSAGE-ID-NULL"));
            return;
        }

        String emote = getEmote(event);
        if (emote == null) {
            createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " tried to use /removereactionrole " +
                    "but it failed because emote was null.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "REMOVE-REACTION-ROLE-FAILED-EMOTE-NULL"));
        }

        String codifiedEmote = EmojiUtils.shortCodify(EmojiUtils.emojify(emote));
        ReactionRole reactionRole = guildsCache.getClientGuildByID(guildID).getReactionRoleByMessageIDAndEmojiID(messageID, codifiedEmote);
        if (reactionRole == null) {
            //This is an error which should never happen.
            createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " tried to remove a reaction role " +
                    "but it failed because the object does not event exist.");
            replyEphemeralToUser(event, "You tried to remove a reaction role which do not exist. Please report " +
                    "this message to an developer.");
            /*
             Added: 07.11.2022
             Bug Reported: -
             */
            return;
        }

        event.getChannel().retrieveMessageById(messageID).queue(message -> {
            for (MessageReaction messageReaction : message.getReactions()) {
                String codifiedEmote1 = EmojiUtils.shortCodify(messageID);
                if (codifiedEmote1.equalsIgnoreCase(codifiedEmote)) {

                    message.getReactions().remove(messageReaction);
                    createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " added a new reaction " +
                            "to " + messageID);
                    guildsCache.getClientGuildByID(guildID).removeReactionRoleByMessageIDAndEmojiID(messageID, codifiedEmote);
                    replyEphemeralToUser(event, LanguageController.getMessage(language, "REACTION-ROLE-REMOVED"));
                    return;
                }
            }
        });
    }

    private String getMessageID(SlashCommandInteractionEvent event) {
        OptionMapping messageIdOption = event.getOption(MESSAGE_ID_OPTION_NAME);
        return messageIdOption != null ? messageIdOption.getAsString() : null;
    }

    private String getEmote(SlashCommandInteractionEvent event) {
        OptionMapping emoteOption = event.getOption(EMOTE_OPTION_NAME);
        return emoteOption != null ? emoteOption.getAsString() : null;
    }
}
