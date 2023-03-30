package net.purplegoose.didnb.commands.server;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.ClientGuild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.Nullable;

import static net.purplegoose.didnb.utils.StringUtil.*;

/**
 * @author Umbreon Majora
 * <p>
 * @Description: Show's all server settings.
 * <p>
 * @Command: /config
 */
@Slf4j
public class ConfigCommand implements IClientCommand {

    private final GuildsCache guildsCache;

    public ConfigCommand(GuildsCache guildsCache) {
        this.guildsCache = guildsCache;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();
        User user = member.getUser();

        String guildID = guild.getId();
        String guildName = guild.getName();

        String channelID = event.getChannel().getId();
        String channelName = event.getChannel().getName();

        log.info("{} used /config. Guild: {}({}) Channel: {}({})",
                getFullUsernameWithDiscriminator(user), guildName, guildID, channelName, channelID);

        replyEphemeralToUser(event, buildConfigEmbed(guild, member, user));
    }

    private MessageEmbed buildConfigEmbed(Guild guild, Member member, User user) {
        String guildID = guild.getId();
        ClientGuild clientGuild = guildsCache.getClientGuildByID(guildID);

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Configurations of " + guild.getName());
        embed.setDescription("ID: " + guildID);
        embed.setColor(800080);
        embed.setThumbnail(guild.getIconUrl());
        embed.addField("Owner:", getFullUsernameWithDiscriminator(user), true);

        String languageRawName = clientGuild.getGuildLanguage().rawName;
        embed.addField("Language:", languageRawName, true);

        String timeZone = clientGuild.getGuildTimeZone();
        embed.addField("TimeZone:", timeZone, true);

        String adminRoleID = clientGuild.getGuildAdminRoleID();
        embed.addField("Admin Role:", getAdminRole(guild, adminRoleID), true);
        embed.addField("Is user admin?", isUserPermitted(member, adminRoleID) ? "Yes" : "No", true);
        embed.addField("Event-Messages:", clientGuild.isEventMessageEnabled() ? "Enabled" : "Disabled", true);
        embed.addField("Warn-Messages:", clientGuild.isWarnMessagesEnabled() ? "Enabled" : "Disabled", true);
        embed.addField("Warn-Time:", String.valueOf(clientGuild.getWarnTimeInMinutes()), true);
        embed.addField("Custom messages count:", String.valueOf(clientGuild.getCustomNotificationSize()), true);
        embed.addField("Registered channels:", String.valueOf(clientGuild.getNotificationChannelCount()), true);
        return embed.build();
    }

    private String getAdminRole(Guild guild, String roleID) {
        if (roleID == null) {
            return "DEFAULT";
        }

        Role adminRole = guild.getRoleById(roleID);
        return adminRole == null ? FAILED_MESSAGE : adminRole.getName();
    }
    
}
