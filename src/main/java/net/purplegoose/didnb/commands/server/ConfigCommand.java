package net.purplegoose.didnb.commands.server;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.utils.PermissionUtil;

import static net.purplegoose.didnb.utils.StringUtil.FAILED_MESSAGE;

/**
 * @author Umbreon Majora
 * <p>
 * @Description: Show's all server settings.
 * <p>
 * @Command: /config
 */
@Slf4j
@AllArgsConstructor
public class ConfigCommand implements IClientCommand {

    private final GuildsCache guildsCache;

    @Override
    public void runCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        Guild guild = event.getGuild();
        Member member = event.getMember();
        User user = event.getUser();

        log.info("{} used /config. Guild: {}({}). Channel: {}({})",
                logInfo.getExecutor(), logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());

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

        String timeZone = clientGuild.getTimeZone();
        embed.addField("TimeZone:", timeZone, true);

        String adminRoleID = clientGuild.getGuildAdminRoleID();
        embed.addField("Admin Role:", getAdminRole(guild, adminRoleID), true);
        embed.addField("Is user admin?", PermissionUtil.isUserPermitted(member, adminRoleID) ? "Yes" : "No", true);
        embed.addField("Event-Messages:", clientGuild.isEventMessageEnabled() ? "Enabled" : "Disabled", true);
        embed.addField("Warn-Messages:", clientGuild.isWarnMessagesEnabled() ? "Enabled" : "Disabled", true);
        embed.addField("Warn-Time:", String.valueOf(clientGuild.getWarnTimeInMinutes()), true);
        embed.addField("Custom messages count:", String.valueOf(clientGuild.getCustomNotificationSize()), true);
        embed.addField("Registered channels:", String.valueOf(clientGuild.getNotificationChannelCount()), true);
        embed.addField("Auto Delete:", clientGuild.isAutoDeleteEnabled() ? "Enabled" : "Disabled", true);
        embed.addField("Auto Delete Time:", String.valueOf(clientGuild.getAutoDeleteTimeInHours()), true);
        embed.addField("Embed Lead Time:", String.valueOf(clientGuild.getEmbedLeadTime()), true);
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
