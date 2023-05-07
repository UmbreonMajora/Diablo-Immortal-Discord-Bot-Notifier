package net.purplegoose.didnb.events;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.purplegoose.didnb.cache.CustomMessagesCache;
import net.purplegoose.didnb.cache.GameDataCache;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.channel.*;
import net.purplegoose.didnb.commands.custom_notifications.*;
import net.purplegoose.didnb.commands.info.*;
import net.purplegoose.didnb.commands.server.*;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.utils.PermissionUtil;
import org.jetbrains.annotations.NotNull;

import static net.purplegoose.didnb.utils.CommandsUtil.*;
@Slf4j
public class SlashCommandInteraction extends ListenerAdapter {

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    // Channel commands
    private final NotificationCommand notificationCommand;
    private final InfoCommand infoCommand;
    private final ListNotificationsCommand listNotificationsCommand;
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

    // Server commands
    private final AdminRoleCommand adminRoleCommand;
    private final ConfigCommand configCommand;
    private final LanguageCommand languageCommand;
    private final ServerCommand serverCommand;
    private final TimeZoneCommand timeZoneCommand;
    private final WarnTimeCommand warnTimeCommand;
    private final AutoDeleteCommand autoDeleteCommand;

    public SlashCommandInteraction(GuildsCache guildsCache,
                                   DatabaseRequests databaseRequests,
                                   GameDataCache gameDataCache,
                                   CustomMessagesCache customMessagesCache) {
        this.guildsCache = guildsCache;
        this.databaseRequests = databaseRequests;

        this.notificationCommand = new NotificationCommand(guildsCache, databaseRequests);
        this.infoCommand = new InfoCommand(guildsCache);
        this.listNotificationsCommand = new ListNotificationsCommand();
        this.mentionRoleCommand = new MentionRoleCommand(guildsCache, databaseRequests);
        this.presetCommand = new PresetCommand(guildsCache, databaseRequests);
        this.registerCommand = new RegisterCommand(guildsCache, databaseRequests);
        this.unregisterCommand = new UnregisterCommand(guildsCache, databaseRequests);

        this.createMessageCommand = new CreateMessageCommand(guildsCache, databaseRequests, customMessagesCache);
        this.messageInfoCommand = new MessageInfoCommand(guildsCache);
        this.deleteMessageCommand = new DeleteMessageCommand(databaseRequests, guildsCache, customMessagesCache);
        this.listMessagesCommand = new ListMessagesCommand(guildsCache);
        this.editMessageCommand = new EditMessageCommand(databaseRequests, guildsCache);

        this.helpCommand = new HelpCommand();
        this.instructionsCommand = new InstructionsCommand();
        this.languagesCommand = new LanguagesCommand();
        this.timeZonesCommand = new TimeZonesCommand(guildsCache);
        this.todayCommand = new TodayCommand(guildsCache, gameDataCache);
        this.upComingCommand = new UpComingCommand(guildsCache, gameDataCache);

        this.adminRoleCommand = new AdminRoleCommand(guildsCache, databaseRequests);
        this.configCommand = new ConfigCommand(guildsCache);
        this.languageCommand = new LanguageCommand(guildsCache, databaseRequests);
        this.serverCommand = new ServerCommand(guildsCache, databaseRequests);
        this.timeZoneCommand = new TimeZoneCommand(guildsCache, databaseRequests);
        this.warnTimeCommand = new WarnTimeCommand(databaseRequests, guildsCache);
        this.messageCommand = new MessageCommand(databaseRequests, guildsCache);
        this.autoDeleteCommand = new AutoDeleteCommand(guildsCache);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        Guild guild = event.getGuild();
        String fullUsername = event.getUser().getName() + "#" + event.getUser().getDiscriminator();
        if (isEventInPrivateChat(guild, member)) {
            log.error(fullUsername + " used a command in private chat.");
            return;
        }

        if (!isChannelTypeTextChannelType(event.getChannelType())) {
            log.error(fullUsername + " used a command in a non text channel.");
            return;
        }

        String guildID = guild.getId();

        if (!guildsCache.isGuildRegistered(guildID)) {
            log.info(guild.getName() + " is not registered. Registering...");
            ClientGuild clientGuild = new ClientGuild(guildID);
            guildsCache.addGuild(clientGuild);
            databaseRequests.createGuild(clientGuild);
        }

        LoggingInformation logInfo = new LoggingInformation(fullUsername, guild.getName(), guildID,
                event.getChannel().getName(), event.getChannel().getId());

        String guildAdminRoleID = guildsCache.getGuildAdminRoleID(guildID);
        if (!PermissionUtil.isUserPermitted(member, guildAdminRoleID)) {
            log.info("{} tried to use a command without permissions.", logInfo.getExecutor());
            return;
        }

        event.deferReply().setEphemeral(true).queue();
        executeCommand(event, logInfo);
    }

    private void executeCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        String command = event.getName().toLowerCase();
        switch (command) {
            case COMMAND_TODAY -> todayCommand.runCommand(event, logInfo);
            case COMMAND_UPCOMING -> upComingCommand.runCommand(event, logInfo);
            case COMMAND_CREATE_MESSAGE -> createMessageCommand.runCommand(event, logInfo);
            case COMMAND_DELETE_CUSTOM_MESSAGE -> deleteMessageCommand.runCommand(event, logInfo);
            case COMMAND_CUSTOM_MESSAGE_INFO -> messageInfoCommand.runCommand(event, logInfo);
            case COMMAND_LIST_MESSAGES -> listMessagesCommand.runCommand(event, logInfo);
            case COMMAND_REGISTER -> registerCommand.runCommand(event, logInfo);
            case COMMAND_UNREGISTER -> unregisterCommand.runCommand(event, logInfo);
            case COMMAND_MENTION_ROLE -> mentionRoleCommand.runCommand(event, logInfo);
            case COMMAND_INFO -> infoCommand.runCommand(event, logInfo);
            case COMMAND_INSTALL -> instructionsCommand.runCommand(event, logInfo);
            case COMMAND_LANGUAGES -> languagesCommand.runCommand(event, logInfo);
            case COMMAND_TIMEZONES -> timeZonesCommand.runCommand(event, logInfo);
            case COMMAND_NOTIFICATION -> notificationCommand.runCommand(event, logInfo);
            case COMMAND_LIST_NOTIFICATIONS -> listNotificationsCommand.runCommand(event, logInfo);
            case COMMAND_TIMEZONE -> timeZoneCommand.runCommand(event, logInfo);
            case COMMAND_LANGUAGE -> languageCommand.runCommand(event, logInfo);
            case COMMAND_SERVER -> serverCommand.runCommand(event, logInfo);
            case COMMAND_CONFIG -> configCommand.runCommand(event, logInfo);
            case COMMAND_ADMIN_ROLE -> adminRoleCommand.runCommand(event, logInfo);
            case COMMAND_PRESET -> presetCommand.runCommand(event, logInfo);
            case COMMAND_WARN_TIME -> warnTimeCommand.runCommand(event, logInfo);
            case COMMAND_MESSAGE -> messageCommand.runCommand(event, logInfo);
            case COMMAND_EDIT_MESSAGE -> editMessageCommand.runCommand(event, logInfo);
            case COMMAND_AUTODELETE -> autoDeleteCommand.runCommand(event, logInfo);
            default -> helpCommand.runCommand(event, logInfo);
        }
    }

    private boolean isChannelTypeTextChannelType(ChannelType channelType) {
        return channelType.getId() == 0;
    }

    //javadoc says: This is null if the interaction is not from a guild.
    private boolean isEventInPrivateChat(Guild guild, Member member) {
        return guild == null || member == null;
    }

}
