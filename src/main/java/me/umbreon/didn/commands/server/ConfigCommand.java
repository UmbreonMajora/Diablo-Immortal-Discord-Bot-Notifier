package me.umbreon.didn.commands.server;

import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.commands.IClientCommand;
import me.umbreon.didn.data.ClientGuild;
import me.umbreon.didn.enums.Language;
import me.umbreon.didn.languages.LanguageController;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static me.umbreon.didn.utils.StringUtil.NEW_LINE;

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
        String guildID = event.getGuild().getId(); //Can't be null since it's caught in SlashCommandInteraction.java
        Member member = event.getMember();
        createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(event.getUser()) +
                " used /config on " + guildID);
        replyEphemeralToUser(event, buildServerInfoMessage(guildID, member, event.getGuild()));
    }

    private String buildServerInfoMessage(String guildID, Member member, Guild guild) {
        ClientGuild clientGuild = guildsCache.getClientGuildByID(guildID);
        Language language = guildsCache.getGuildLanguage(guildID);
        Role adminRole = guild.getRoleById(guildsCache.getGuildAdminRoleID(guildID));

        return "```" + NEW_LINE +
                "Server Configurations:" + NEW_LINE +
                "GuildID: " + clientGuild.getGuildID() + NEW_LINE +
                "Guild Owner: " + Objects.requireNonNull(guild.getOwner()).getEffectiveName() + NEW_LINE +
                "Language: " + language.rawName + NEW_LINE +
                "TimeZone: " + clientGuild.getGuildTimeZone() + NEW_LINE +
                "Admin Role: " + adminRole.getName() + " - " + adminRole.getId() + NEW_LINE +
                "Event Messages: " + (clientGuild.isEventMessageEnabled() ? "Enabled" : "Disabled") + NEW_LINE +
                "Warn Messages: " + (clientGuild.isWarnMessagesEnabled() ? "Enabled" : "Disabled") + NEW_LINE +
                "Is user admin?: " + (isUserPermitted(member, adminRole.getId()) ? "Yes" : "No") + NEW_LINE +
                "```";
    }
}
