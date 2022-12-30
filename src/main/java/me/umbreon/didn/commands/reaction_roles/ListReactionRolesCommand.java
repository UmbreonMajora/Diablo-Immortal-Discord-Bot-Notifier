package me.umbreon.didn.commands.reaction_roles;

import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.commands.IClientCommand;
import me.umbreon.didn.data.ClientGuild;
import me.umbreon.didn.data.ReactionRole;
import me.umbreon.didn.utils.StringUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.util.List;

import static me.umbreon.didn.utils.StringUtil.NEW_LINE;

/**
 * @author Umbreon Majora
 * Allow's user to see all their reaction roles.
 * Command: /listreactionroles
 */
public class ListReactionRolesCommand implements IClientCommand {

    private final GuildsCache guildsCache;

    public ListReactionRolesCommand(GuildsCache guildsCache) {
        this.guildsCache = guildsCache;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        replyEphemeralToUser(event, buildReactionRolesListEmbed(event.getGuild()));
    }

    private MessageEmbed buildReactionRolesListEmbed(Guild guild) {
        String guildID = guild.getId();
        String guildName = guild.getName();
        ClientGuild clientGuild = guildsCache.getClientGuildByID(guildID);

        EmbedBuilder embedBuilder = new EmbedBuilder(); //todo: add title to config
        embedBuilder.setTitle("Reaction roles of " + guildName);
        embedBuilder.setColor(Color.green);

        for (ReactionRole rR : clientGuild.getReactionRoles()) {
            if (rR.getReactionType().equalsIgnoreCase("custom")) {
                String emojiID = StringUtil.removeAllNonNumericCharacters(rR.getReactionID());
                if (doEmojiStillExists(guild.getEmojis(), emojiID)) {
                    embedBuilder.addField(rR.getMessageID(), guild.getEmojiById(emojiID).getFormatted(), false);
                } //todo remove emojiid from database if it no longer exists.
            } else {
                embedBuilder.addField(rR.getMessageID(), Emoji.fromUnicode(rR.getReactionID()).getFormatted(), false);
            }
        }
        return embedBuilder.build();
    }

    private boolean doEmojiStillExists(List<RichCustomEmoji> emojiList, String targetEmojiID) {
        for (RichCustomEmoji richCustomEmoji : emojiList) {
            if (richCustomEmoji.getId().equals(targetEmojiID)) {
                return true;
            }
        }
        return false;
    }

}
