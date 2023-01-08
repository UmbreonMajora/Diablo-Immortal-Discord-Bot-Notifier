package net.purplegoose.didnb.commands.server;

import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.enums.Language;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static net.purplegoose.didnb.utils.StringUtil.FORMATTED_MESSAGE;
import static net.purplegoose.didnb.utils.StringUtil.NEW_LINE;

/**
 * @author Umbreon Majora
 * Show's all server settings.
 * Command: /config
 */
public class ConfigCommand implements IClientCommand {

    private final Logger LOGGER = LoggerFactory.getLogger(ConfigCommand.class);

    private final GuildsCache guildsCache;

    public ConfigCommand(GuildsCache guildsCache) {
        this.guildsCache = guildsCache;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        String guildID = Objects.requireNonNull(event.getGuild()).getId();
        Member member = event.getMember();
        createLog(LOGGER, getFullUsernameWithDiscriminator(event.getUser()) +
                " used /config on " + guildID);
        replyEphemeralToUser(event, buildServerInfoMessage(guildID, member, event.getGuild()));
    }

    private String buildServerInfoMessage(String guildID, Member member, Guild guild) {
        ClientGuild clientGuild = guildsCache.getClientGuildByID(guildID);
        Language language = guildsCache.getGuildLanguage(guildID);
        String guildOwnerName;
        try {
            guildOwnerName = Objects.requireNonNull(guild.getOwner()).getEffectiveName();
        } catch (NullPointerException e) {
            guildOwnerName = "?";
        }

        StringBuilder serverConfigMessageBuilder = new StringBuilder();
        serverConfigMessageBuilder.append(FORMATTED_MESSAGE).append(NEW_LINE)
                .append("Server Configurations: ").append(NEW_LINE)
                .append("GuildID: ").append(guildID).append(NEW_LINE)
                .append("Guild Owner: ").append(guildOwnerName).append(NEW_LINE)
                .append("Language: ").append(language.rawName).append(NEW_LINE)
                .append("TimeZone: ").append(clientGuild.getGuildTimeZone()).append(NEW_LINE);

        String isUserBotAdminAsString = "No";
        String adminRoleID = guildsCache.getGuildAdminRoleID(guildID);
        if (adminRoleID == null) {

            serverConfigMessageBuilder.append("Admin Role: @Bot Admin").append(NEW_LINE);
            for (Role role : member.getRoles()) {
                isUserBotAdminAsString = role.getName().equalsIgnoreCase("bot admin") ? "Yes" : "No";
            }

        } else {
            Role adminRole = guild.getRoleById(adminRoleID);
            String adminRoleName = Objects.requireNonNull(adminRole).getName();
            serverConfigMessageBuilder.append("Admin Role: ").append(adminRoleName).append(" - ").append(adminRoleID).append(NEW_LINE);
            isUserBotAdminAsString = isUserPermitted(member, adminRole.getId()) ? "Yes" : "No";
        }

        serverConfigMessageBuilder.append("Is User Bot Admin?: ").append(isUserBotAdminAsString).append(NEW_LINE);

        String isEventMessageEnabledAsStringMessage = clientGuild.isEventMessageEnabled() ? "Enabled" : "Disabled";
        String isWarnMessagesEnabledAsStringMessage = clientGuild.isWarnMessagesEnabled() ? "Enabled" : "Disabled";

        serverConfigMessageBuilder.append("Event Messages: ").append(isEventMessageEnabledAsStringMessage).append(NEW_LINE)
                .append("Warn Messages: ").append(isWarnMessagesEnabledAsStringMessage).append(NEW_LINE)
                .append("Warn Time (In minutes): ").append(clientGuild.getHeadUpTime())
                .append(FORMATTED_MESSAGE);
        return serverConfigMessageBuilder.toString();
    }
}
