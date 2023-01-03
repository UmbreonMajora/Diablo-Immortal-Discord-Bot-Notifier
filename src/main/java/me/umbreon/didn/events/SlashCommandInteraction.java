package me.umbreon.didn.events;

import me.umbreon.didn.cache.GameDataCache;
import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.commands.channel.*;
import me.umbreon.didn.commands.custom_notifications.*;
import me.umbreon.didn.commands.info.*;
import me.umbreon.didn.commands.reaction_roles.CreateReactionRoleCommand;
import me.umbreon.didn.commands.reaction_roles.ListReactionRolesCommand;
import me.umbreon.didn.commands.reaction_roles.RemoveReactionRoleCommand;
import me.umbreon.didn.commands.server.*;
import me.umbreon.didn.data.ClientGuild;
import me.umbreon.didn.database.DatabaseRequests;
import me.umbreon.didn.logger.FileLogger;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static me.umbreon.didn.utils.CommandsUtil.*;

public class SlashCommandInteraction extends ListenerAdapter {

    private final Logger LOGGER = LoggerFactory.getLogger(SlashCommandInteraction.class);

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    // Channel commands
    private final EventCommand eventCommand;
    private final InfoCommand infoCommand;
    private final ListEventsCommand listEventsCommand;
    private final MentionRoleCommand mentionRoleCommand;
    private final PresetCommand presetCommand;
    private final RegisterCommand registerCommand;
    private final UnregisterCommand unregisterCommand;

    // Custom notification commands
    private final CreateMessageCommand createMessageCommand;
    private final MessageInfoCommand messageInfoCommand;
    private final DeleteMessageCommand deleteMessageCommand;
    private final ListMessagesCommand listMessagesCommand;
    private final MessageCommand messageCommand;
    private final EditMessageCommand editMessageCommand;

    // Info commands
    private final HelpCommand helpCommand;
    private final InstructionsCommand instructionsCommand;
    private final LanguagesCommand languagesCommand;
    private final TimeZonesCommand timeZonesCommand;
    private final TodayCommand todayCommand;
    private final UpComingCommand upComingCommand;

    // Reaction roles commands
    private final CreateReactionRoleCommand createReactionRoleCommand;
    private final RemoveReactionRoleCommand removeReactionRoleCommand;
    private final ListReactionRolesCommand listReactionRolesCommand;

    // Server commands
    private final AdminRoleCommand adminRoleCommand;
    private final ConfigCommand configCommand;
    private final LanguageCommand languageCommand;
    private final ServerCommand serverCommand;
    private final TimeZoneCommand timeZoneCommand;
    private final WarnTimeCommand warnTimeCommand;
    private final DaylightTimeCommand daylightTimeCommand;

    public SlashCommandInteraction(GuildsCache guildsCache, DatabaseRequests databaseRequests, GameDataCache gameDataCache) {
        this.guildsCache = guildsCache;
        this.databaseRequests = databaseRequests;

        this.eventCommand = new EventCommand(guildsCache, databaseRequests);
        this.infoCommand = new InfoCommand(guildsCache);
        this.listEventsCommand = new ListEventsCommand();
        this.mentionRoleCommand = new MentionRoleCommand(guildsCache, databaseRequests);
        this.presetCommand = new PresetCommand(guildsCache, databaseRequests);
        this.registerCommand = new RegisterCommand(guildsCache, databaseRequests);
        this.unregisterCommand = new UnregisterCommand(guildsCache, databaseRequests);

        this.createMessageCommand = new CreateMessageCommand(guildsCache, databaseRequests);
        this.messageInfoCommand = new MessageInfoCommand(guildsCache);
        this.deleteMessageCommand = new DeleteMessageCommand(databaseRequests, guildsCache);
        this.listMessagesCommand = new ListMessagesCommand(guildsCache);
        this.editMessageCommand = new EditMessageCommand(databaseRequests, guildsCache);

        this.helpCommand = new HelpCommand();
        this.instructionsCommand = new InstructionsCommand();
        this.languagesCommand = new LanguagesCommand();
        this.timeZonesCommand = new TimeZonesCommand(guildsCache);
        this.todayCommand = new TodayCommand(guildsCache, gameDataCache);
        this.upComingCommand = new UpComingCommand(guildsCache, gameDataCache);

        this.createReactionRoleCommand = new CreateReactionRoleCommand(databaseRequests, guildsCache);
        this.removeReactionRoleCommand = new RemoveReactionRoleCommand(guildsCache);
        this.listReactionRolesCommand = new ListReactionRolesCommand(guildsCache);

        this.adminRoleCommand = new AdminRoleCommand(guildsCache, databaseRequests);
        this.configCommand = new ConfigCommand(guildsCache);
        this.languageCommand = new LanguageCommand(guildsCache, databaseRequests);
        this.serverCommand = new ServerCommand(guildsCache, databaseRequests);
        this.timeZoneCommand = new TimeZoneCommand(guildsCache, databaseRequests);
        this.warnTimeCommand = new WarnTimeCommand(databaseRequests, guildsCache);

        this.daylightTimeCommand = new DaylightTimeCommand(databaseRequests, guildsCache);
        this.messageCommand = new MessageCommand(databaseRequests, guildsCache);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        Guild guild = event.getGuild();
        String fullUsername = event.getUser().getName() + "#" + event.getUser().getDiscriminator();
        if (isEventInPrivatChat(guild, member)) {
            LOGGER.info(fullUsername + " executed a command in privat chat.");
            FileLogger.createClientFileLog(fullUsername + " executed a command in privat chat.");
            return;
        }

        if (!isChannelTypeTextChannelType(event.getChannelType())) {
            LOGGER.info(fullUsername + " executed a command in a non text channel.");
            FileLogger.createClientFileLog(fullUsername + " executed a command in a non text channel.");
            return;
        }

        String guildID = guild.getId();

        if (!guildsCache.isGuildRegistered(guildID)) {
            LOGGER.info(guild + " is not registered. Registering...");
            FileLogger.createClientFileLog(guild + " is not registered. Registering...");
            ClientGuild clientGuild = new ClientGuild(guildID);
            guildsCache.addGuild(clientGuild);
            databaseRequests.createGuild(clientGuild);
        }

        String guildAdminRoleID = guildsCache.getGuildAdminRoleID(guildID);
        if (!isUserPermitted(member, guildAdminRoleID)) {
            return;
        }

        event.deferReply().queue();

        executeCommand(event);
    }

    private void executeCommand(SlashCommandInteractionEvent event) {
        String command = event.getName().toLowerCase();
        switch (command) {
            case COMMAND_TODAY:
                todayCommand.runCommand(event);
                break;
            case COMMAND_UPCOMING:
                upComingCommand.runCommand(event);
                break;
            case COMMAND_CREATE_MESSAGE:
                createMessageCommand.runCommand(event);
                break;
            case COMMAND_DELETE_CUSTOM_MESSAGE:
                deleteMessageCommand.runCommand(event);
                break;
            case COMMAND_CUSTOM_MESSAGE_INFO:
                messageInfoCommand.runCommand(event);
                break;
            case COMMAND_LIST_MESSAGES:
                listMessagesCommand.runCommand(event);
                break;
            case COMMAND_REGISTER:
                registerCommand.runCommand(event);
                break;
            case COMMAND_UNREGISTER:
                unregisterCommand.runCommand(event);
                break;
            case COMMAND_MENTION_ROLE:
                mentionRoleCommand.runCommand(event);
                break;
            case COMMAND_INFO:
                infoCommand.runCommand(event);
                break;
            case COMMAND_INSTALL:
                instructionsCommand.runCommand(event);
                break;
            case COMMAND_LANGUAGES:
                languagesCommand.runCommand(event);
                break;
            case COMMAND_TIMEZONES:
                timeZonesCommand.runCommand(event);
                break;
            case COMMAND_EVENT:
                eventCommand.runCommand(event);
                break;
            case COMMAND_LIST_EVENTS:
                listEventsCommand.runCommand(event);
                break;
            case COMMAND_TIMEZONE:
                timeZoneCommand.runCommand(event);
                break;
            case COMMAND_LANGUAGE:
                languageCommand.runCommand(event);
                break;
            case COMMAND_SERVER:
                serverCommand.runCommand(event);
                break;
            case COMMAND_CONFIG:
                configCommand.runCommand(event);
                break;
            case COMMAND_CREATE_REACTION_ROLE:
                createReactionRoleCommand.runCommand(event);
                break;
            case COMMAND_REMOVE_REACTION_ROLE:
                removeReactionRoleCommand.runCommand(event);
                break;
            case COMMAND_ADMIN_ROLE:
                adminRoleCommand.runCommand(event);
                break;
            case COMMAND_HELP:
                helpCommand.runCommand(event);
                break;
            case COMMAND_PRESET:
                presetCommand.runCommand(event);
                break;
            case COMMAND_LIST_REACTION_ROLES:
                listReactionRolesCommand.runCommand(event);
                break;
            case COMMAND_WARN_TIME:
                warnTimeCommand.runCommand(event);
                break;
            case COMMAND_MESSAGE:
                messageCommand.runCommand(event);
                break;
            case COMMAND_EDIT_MESSAGE:
                editMessageCommand.runCommand(event);
                break;
            case COMMAND_DAYLIGHTTIME:
                daylightTimeCommand.runCommand(event);
                break;
        }
    }

    private boolean isChannelTypeTextChannelType(ChannelType channelType) {
        return channelType.getId() == 0;
    }

    //javadoc says: This is null if the interaction is not from a guild.
    private boolean isEventInPrivatChat(Guild guild, Member member) {
        return guild == null || member == null;
    }

    private boolean isUserPermitted(Member member, String guildAdminRoleID) {
        if (isServerOwner(member)) {
            return true;
        }

        List<Role> roles = member.getRoles();
        if (guildAdminRoleID == null) {
            return doMemberHasDefaultAdminRole(roles);
        }

        return doMemberHasCustomAdminRole(roles, guildAdminRoleID);
    }

    private boolean isServerOwner(final Member member) {
        return member.isOwner();
    }

    private boolean doMemberHasDefaultAdminRole(List<Role> roles) {
        Role tempRole = roles.stream()
                .filter(role -> role.getName().equalsIgnoreCase("Bot Admin"))
                .findFirst()
                .orElse(null);
        return tempRole != null;
    }

    private boolean doMemberHasCustomAdminRole(List<Role> roles, String guildAdminRoleID) {
        Role tempRole = roles.stream()
                .filter(role -> role.getId().equals(guildAdminRoleID))
                .findFirst()
                .orElse(null);
        return tempRole != null;
    }

}
