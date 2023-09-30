package net.purplegoose.didnb.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.purplegoose.didnb.commands.channel.RegisterCommand;
import net.purplegoose.didnb.commands.channel.UnregisterCommand;
import net.purplegoose.didnb.commands.scheduled_events.DeleteAllEvents;
import net.purplegoose.didnb.commands.scheduled_events.ListEvents;
import net.purplegoose.didnb.enums.*;
import net.purplegoose.didnb.news.commands.RegisterNewsChannelCommand;
import net.purplegoose.didnb.news.commands.ToggleNewsCommand;
import net.purplegoose.didnb.news.commands.UnregisterNewsChannelCommand;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
public class CommandsUtil {

    // -> /createcustommessage <WEEKDAY> <TIME> <BOOL_REPEATING> <MESSAGE>
    public static final String COMMAND_CREATE_MESSAGE = "createmessage";
    public static final String WEEKDAY_OPTION_NAME = "weekday";
    public static final String TIME_OPTION_NAME = "time";
    public static final String MESSAGE_OPTION_NAME = "message";
    public static final String MESSAGE_REPEATING_BOOL = "repeating";
    // -> /deletecustommessage <ID>
    public static final String COMMAND_DELETE_CUSTOM_MESSAGE = "deletecustommessage";
    public static final String CUSTOM_MESSAGE_ID_OPTION_NAME = "custommessageid";
    // -> /editmessage [Required: custommessageid] [Required: what2change] [Required: newvalue]
    public static final String COMMAND_EDIT_MESSAGE = "editmessage";
    public static final String EDIT_MESSAGE_OPTION_NAME = "editmessage";
    public static final String EDIT_MESSAGE_VALUE_OPTION_NAME = "editmessagevalue";
    // -> /listcustommessages
    public static final String COMMAND_LIST_MESSAGES = "listcustommessages";
    // -> Commmand: /message [Required: messagevalue] [Required: MessageID]
    public static final String COMMAND_MESSAGE = "message";
    public static final String MESSAGE_ID_OPTION_NAME = "messageid";
    public static final String ACTIVATION_BOOL = "activation";
    // -> /custommessageinfo <ID>
    public static final String COMMAND_CUSTOM_MESSAGE_INFO = "messageinfo";
    // -> /info <CHANNEL>
    public static final String COMMAND_INFO = "info";
    public static final String TARGET_CHANNEL_OPTION_NAME = "targetchannel";
    // -> /listevents
    public static final String COMMAND_LIST_NOTIFICATIONS = "listnotifications";
    // -> /mentionrole <ROLE> <CHANNEL>
    public static final String COMMAND_MENTION_ROLE = "mentionrole";
    public static final String MENTION_ROLE_OPTION_NAME = "mentionrole";
    // -> /notification <GAME_EVENT> <ON/OFF>
    public static final String COMMAND_NOTIFICATION = "notification";
    public static final String EVENT_NAME_OPTION_NAME = "eventname";
    public static final String EVENT_TOGGLE_OPTION_NAME = "eventtoggle";
    // -> /preset <PRESET>
    public static final String COMMAND_PRESET = "preset";
    public static final String PRESET_OPTION_NAME = "preset";
    // -> /register <CHANNEL>
    public static final String COMMAND_REGISTER = "register";
    // -> /unregister <CHANNEL>
    public static final String COMMAND_UNREGISTER = "unregister";
    // -> /help
    public static final String COMMAND_HELP = "help";
    // -> /install
    public static final String COMMAND_INSTALL = "install";
    // -> /languages
    public static final String COMMAND_LANGUAGES = "languages";
    // -> /timezones
    public static final String COMMAND_TIMEZONES = "timezones";
    // -> /today
    public static final String COMMAND_TODAY = "today";
    // -> /upcoming
    public static final String COMMAND_UPCOMING = "upcoming";
    // -> Command: /adminrole [Required: role]
    public static final String COMMAND_ADMIN_ROLE = "adminrole";
    public static final String ADMIN_ROLE_OPTION_NAME = "adminrole";
    // -> Command: /autodelete [Not Required: autoDeleteTimeInHours]
    public static final String COMMAND_AUTODELETE = "autodelete";
    public static final String AUTO_DELETE_TIME_OPTION_NAME = "autodeletevalue";
    // -> Command: /config
    public static final String COMMAND_CONFIG = "config";
    // -> Command: /language [Required: language]
    public static final String COMMAND_LANGUAGE = "language";
    public static final String LANGUAGE_OPTION_NAME = "language";
    // -> Command: /server [Required: serversetting] [Required: servervalue]
    public static final String COMMAND_SERVER = "server";
    public static final String SERVER_SETTING_OPTION_NAME = "serversetting";
    public static final String SERVER_SETTING_BOOL = "servervalue";
    // -> Command: /timezone [Required: timezone]
    public static final String COMMAND_TIMEZONE = "timezone";
    public static final String TIMEZONE_OPTION_NAME = "timezone";
    // -> Command: /warntime [Required: warntime]
    public static final String COMMAND_WARN_TIME = "warntime";
    public static final String WARN_TIME_OPTION_NAME = "warntime";
    @Getter
    protected static final List<CommandData> commandDataList = new ArrayList<>();
    @Getter
    private static final Properties commandsProperties = new Properties();
    private static final String COMMAND_CREATE_MESSAGE_DESC;
    private static final OptionData CREATE_CUSTOM_MESSAGE_WEEKDAY_OPTION =
            new OptionData(OptionType.STRING, WEEKDAY_OPTION_NAME, "At what day would you like to get the message?", true);
    private static final OptionData CREATE_CUSTOM_MESSAGE_TIME_OPTION =
            new OptionData(OptionType.STRING, TIME_OPTION_NAME, "At what time would you like to get the message?", true);
    private static final OptionData CREATE_CUSTOM_MESSAGE_MESSAGE_OPTION =
            new OptionData(OptionType.STRING, MESSAGE_OPTION_NAME, "Enter here your message.", true);
    private static final OptionData REQUIRED_MESSAGE_BOOL_OPTION =
            new OptionData(OptionType.BOOLEAN, MESSAGE_REPEATING_BOOL,
                    "Should this messages enabled(true) or disabled(false)", true);
    private static final String COMMAND_DELETE_CUSTOM_MESSAGE_DESC;
    private static final OptionData REQUIRED_CUSTOM_MESSAGE_ID_OPTION =
            new OptionData(OptionType.STRING, CUSTOM_MESSAGE_ID_OPTION_NAME, "Enter your custom message ID here.", true);
    private static final String COMMAND_EDIT_MESSAGE_DESC;
    private static final OptionData REQUIRED_EDIT_MESSAGE_OPTION =
            new OptionData(OptionType.STRING, EDIT_MESSAGE_OPTION_NAME, "What would you like to change?", true);
    private static final OptionData REQUIRED_EDIT_MESSAGE_VALUE_OPTION =
            new OptionData(OptionType.STRING, EDIT_MESSAGE_VALUE_OPTION_NAME, "Enter here your new value.", true);
    private static final String COMMAND_LIST_MESSAGES_DESC;
    private static final String COMMAND_MESSAGE_DESC;
    private static final OptionData REQUIRED_MESSAGE_ID_OPTION =
            new OptionData(OptionType.STRING, MESSAGE_ID_OPTION_NAME, "Enter here your message ID.", true);
    private static final OptionData ACTIVATION_BOOL_OPTION =
            new OptionData(OptionType.BOOLEAN, ACTIVATION_BOOL,
                    "Should this message be enabled (true) or disabled (false)?", true);
    private static final String COMMAND_CUSTOM_MESSAGE_INFO_DESC;
    private static final String COMMAND_INFO_DESC;
    private static final OptionData NOT_REQUIRED_CHANNEL_OPTION =
            new OptionData(OptionType.CHANNEL, TARGET_CHANNEL_OPTION_NAME, "Enter here your channel.", false);
    private static final String COMMAND_LIST_NOTIFICATIONS_DESC;
    private static final String COMMAND_MENTION_ROLE_DESC;
    private static final OptionData MENTION_ROLE_OPTION =
            new OptionData(OptionType.ROLE, MENTION_ROLE_OPTION_NAME,
                    "Enter here your role that should get mentioned.", false);
    private static final String COMMAND_NOTIFICATION_DESC;
    private static final OptionData REQUIRED_EVENT_NAME_OPTION =
            new OptionData(OptionType.STRING, EVENT_NAME_OPTION_NAME,
                    "Allows you to enable or disable a specific event notification for that channel.", true);
    private static final OptionData REQUIRED_EVENT_BOOL_OPTION =
            new OptionData(OptionType.BOOLEAN, EVENT_TOGGLE_OPTION_NAME,
                    "Should this event notification be enabled(true) or disabled(false)", true);
    private static final String COMMAND_PRESET_DESC;
    private static final OptionData REQUIRED_PRESET_OPTION =
            new OptionData(OptionType.STRING, PRESET_OPTION_NAME, "Apply a preset on your notification channel.", true);
    private static final String COMMAND_REGISTER_DESC;
    private static final String COMMAND_UNREGISTER_DESC;
    private static final String COMMAND_HELP_DESC;
    private static final String COMMAND_INSTALL_DESC;
    private static final String COMMAND_LANGUAGES_DESC;
    private static final String COMMAND_TIMEZONES_DESC;
    private static final String COMMAND_TODAY_DESC;
    private static final String COMMAND_UPCOMING_DESC;
    private static final String COMMAND_ADMIN_ROLE_DESC;
    private static final OptionData ADMIN_ROLE_OPTION =
            new OptionData(OptionType.ROLE, ADMIN_ROLE_OPTION_NAME,
                    "Enter here your new admin role.", true);
    private static final String COMMAND_AUTODELETE_DESC;
    private static final OptionData AUTO_DELETE_OPTION =
            new OptionData(OptionType.INTEGER, AUTO_DELETE_TIME_OPTION_NAME, "Enter your time in hours!", false);
    private static final String COMMAND_CONFIG_DESC;
    private static final String COMMAND_LANGUAGE_DESC;
    private static final OptionData REQUIRED_LANGUAGE_OPTION =
            new OptionData(OptionType.STRING, LANGUAGE_OPTION_NAME, "Enter your language here.", true);
    private static final String COMMAND_SERVER_DESC;
    private static final OptionData SERVER_SETTING_OPTION =
            new OptionData(OptionType.STRING, SERVER_SETTING_OPTION_NAME, "Select your server setting here.", true);
    private static final OptionData SERVER_SETTING_BOOL_OPTION =
            new OptionData(OptionType.BOOLEAN, SERVER_SETTING_BOOL,
                    "Should this setting enabled(true) or disabled(false)", true);
    private static final String COMMAND_TIMEZONE_DESC;
    private static final OptionData REQUIRED_TIMEZONE_OPTION =
            new OptionData(OptionType.STRING, TIMEZONE_OPTION_NAME, "Select here your matching GMT timezone. " +
                    "Use /timzones to see what timezone would match you.", true);
    private static final String COMMAND_WARN_TIME_DESC;
    private static final OptionData REQUIRED_WARN_TIME_OPTION =
            new OptionData(OptionType.INTEGER, WARN_TIME_OPTION_NAME, "Change your warn message time", true);

    static {
        try (InputStream inputStream = CommandsUtil.class.getClassLoader().getResourceAsStream("commands.properties")) {
            if (inputStream != null) {
                commandsProperties.load(inputStream);
                log.info("Loaded command.properties file.");
            } else {
                log.warn("Failed to load command.properties! Input stream for command.properties was null.");
            }
        } catch (IOException e) {
            log.warn("Failed to load command.properties!", e);
            e.printStackTrace();
        }
    }

    static {
        COMMAND_CREATE_MESSAGE_DESC = commandsProperties.get(COMMAND_CREATE_MESSAGE).toString();
        for (Weekday weekday : Weekday.values()) {
            CREATE_CUSTOM_MESSAGE_WEEKDAY_OPTION.addChoice(weekday.name, weekday.rawName);
        }
        commandDataList.add(Commands.slash(COMMAND_CREATE_MESSAGE, COMMAND_CREATE_MESSAGE_DESC)
                .addOptions(CREATE_CUSTOM_MESSAGE_WEEKDAY_OPTION, CREATE_CUSTOM_MESSAGE_TIME_OPTION,
                        REQUIRED_MESSAGE_BOOL_OPTION, CREATE_CUSTOM_MESSAGE_MESSAGE_OPTION));
    }

    static {
        commandDataList.add(ToggleNewsCommand.commandData);
        commandDataList.add(RegisterNewsChannelCommand.commandData);
        commandDataList.add(UnregisterNewsChannelCommand.commandData);
    }

    static {
        COMMAND_DELETE_CUSTOM_MESSAGE_DESC = commandsProperties.get(COMMAND_DELETE_CUSTOM_MESSAGE).toString();
        commandDataList.add(Commands.slash(COMMAND_DELETE_CUSTOM_MESSAGE, COMMAND_DELETE_CUSTOM_MESSAGE_DESC)
                .addOptions(REQUIRED_CUSTOM_MESSAGE_ID_OPTION));
    }

    static {
        COMMAND_EDIT_MESSAGE_DESC = commandsProperties.get(COMMAND_EDIT_MESSAGE).toString();

        REQUIRED_EDIT_MESSAGE_OPTION.addChoice("time", "time");
        REQUIRED_EDIT_MESSAGE_OPTION.addChoice(MESSAGE_OPTION_NAME, MESSAGE_OPTION_NAME);
        REQUIRED_EDIT_MESSAGE_OPTION.addChoice(WEEKDAY_OPTION_NAME, WEEKDAY_OPTION_NAME);

        commandDataList.add(Commands.slash(COMMAND_EDIT_MESSAGE, COMMAND_EDIT_MESSAGE_DESC)
                .addOptions(REQUIRED_CUSTOM_MESSAGE_ID_OPTION, REQUIRED_EDIT_MESSAGE_OPTION, REQUIRED_EDIT_MESSAGE_VALUE_OPTION));
        // REQUIRED_CUSTOM_MESSAGE_ID_OPTION -> from deletemessage.
    }

    static {
        COMMAND_LIST_MESSAGES_DESC = commandsProperties.get(COMMAND_LIST_MESSAGES).toString();
        commandDataList.add(Commands.slash(COMMAND_LIST_MESSAGES, COMMAND_LIST_MESSAGES_DESC));
    }

    static {
        COMMAND_MESSAGE_DESC = commandsProperties.get(COMMAND_MESSAGE).toString();
        commandDataList.add(Commands.slash(COMMAND_MESSAGE, COMMAND_MESSAGE_DESC)
                .addOptions(ACTIVATION_BOOL_OPTION, REQUIRED_MESSAGE_ID_OPTION));
    }

    static {
        COMMAND_CUSTOM_MESSAGE_INFO_DESC = commandsProperties.get(COMMAND_CUSTOM_MESSAGE_INFO).toString();
        commandDataList.add(Commands.slash(COMMAND_CUSTOM_MESSAGE_INFO, COMMAND_CUSTOM_MESSAGE_INFO_DESC)
                .addOptions(REQUIRED_CUSTOM_MESSAGE_ID_OPTION));
        // REQUIRED_CUSTOM_MESSAGE_ID_OPTION -> from deletemessage.
    }

    static {
        COMMAND_INFO_DESC = commandsProperties.get(COMMAND_INFO).toString();
        commandDataList.add(Commands.slash(COMMAND_INFO, COMMAND_INFO_DESC)
                .addOptions(NOT_REQUIRED_CHANNEL_OPTION));
    }

    static {
        COMMAND_LIST_NOTIFICATIONS_DESC = commandsProperties.get(COMMAND_LIST_NOTIFICATIONS).toString();
        commandDataList.add(Commands.slash(COMMAND_LIST_NOTIFICATIONS, COMMAND_LIST_NOTIFICATIONS_DESC));
    }

    static {
        COMMAND_MENTION_ROLE_DESC = commandsProperties.get(COMMAND_MENTION_ROLE).toString();
        commandDataList.add(Commands.slash(COMMAND_MENTION_ROLE, COMMAND_MENTION_ROLE_DESC)
                .addOptions(MENTION_ROLE_OPTION, NOT_REQUIRED_CHANNEL_OPTION));
        // NOT_REQUIRED_CHANNEL_OPTION -> from info
    }

    static {
        COMMAND_NOTIFICATION_DESC = commandsProperties.get(COMMAND_NOTIFICATION).toString();
        for (GameEvent gameEvent : GameEvent.values()) {
            REQUIRED_EVENT_NAME_OPTION.addChoice(gameEvent.rawName, gameEvent.rawName);
        }
        commandDataList.add(Commands.slash(COMMAND_NOTIFICATION, COMMAND_NOTIFICATION_DESC)
                .addOptions(REQUIRED_EVENT_NAME_OPTION, REQUIRED_EVENT_BOOL_OPTION));
    }

    static {
        for (Preset preset : Preset.values()) {
            REQUIRED_PRESET_OPTION.addChoice(preset.presetDescription, preset.getAsChoiceName);
        }
        COMMAND_PRESET_DESC = commandsProperties.get(COMMAND_PRESET).toString();
        commandDataList.add(Commands.slash(COMMAND_PRESET, COMMAND_PRESET_DESC)
                .addOptions(REQUIRED_PRESET_OPTION));
    }

    static {
        COMMAND_REGISTER_DESC = commandsProperties.get(COMMAND_REGISTER).toString();
        commandDataList.add(Commands.slash(COMMAND_REGISTER, COMMAND_REGISTER_DESC)
                .addOptions(NOT_REQUIRED_CHANNEL_OPTION));
        // NOT_REQUIRED_CHANNEL_OPTION -> from info command.
    }

    static {
        COMMAND_UNREGISTER_DESC = commandsProperties.get(COMMAND_UNREGISTER).toString();
        commandDataList.add(Commands.slash(COMMAND_UNREGISTER, COMMAND_UNREGISTER_DESC)
                .addOptions(NOT_REQUIRED_CHANNEL_OPTION));
        // NOT_REQUIRED_CHANNEL_OPTION -> from info command.
    }

    static {
        COMMAND_HELP_DESC = commandsProperties.get(COMMAND_HELP).toString();
        commandDataList.add(Commands.slash(COMMAND_HELP, COMMAND_HELP_DESC));
    }

    static {
        COMMAND_INSTALL_DESC = commandsProperties.get(COMMAND_INSTALL).toString();
        commandDataList.add(Commands.slash(COMMAND_INSTALL, COMMAND_INSTALL_DESC));
    }

    static {
        COMMAND_LANGUAGES_DESC = commandsProperties.get(COMMAND_LANGUAGES).toString();
        commandDataList.add(Commands.slash(COMMAND_LANGUAGES, COMMAND_LANGUAGES_DESC));
    }

    static {
        COMMAND_TIMEZONES_DESC = commandsProperties.get(COMMAND_TIMEZONES).toString();
        commandDataList.add(Commands.slash(COMMAND_TIMEZONES, COMMAND_TIMEZONES_DESC));
    }

    static {
        commandDataList.add(Commands.slash(ListEvents.COMMAND, "This commands creates a dropdown menu where you can toggle the creation of scheduled events."));
        commandDataList.add(Commands.slash(DeleteAllEvents.COMMAND, "This command deletes all scheduled events from a server."));
    }

    static {
        COMMAND_TODAY_DESC = commandsProperties.get(COMMAND_TODAY).toString();
        commandDataList.add(Commands.slash(COMMAND_TODAY, COMMAND_TODAY_DESC));
    }

    static {
        COMMAND_UPCOMING_DESC = commandsProperties.get(COMMAND_UPCOMING).toString();
        commandDataList.add(Commands.slash(COMMAND_UPCOMING, COMMAND_UPCOMING_DESC));
    }

    static {
        COMMAND_ADMIN_ROLE_DESC = commandsProperties.get(COMMAND_ADMIN_ROLE).toString();
        commandDataList.add(Commands.slash(COMMAND_ADMIN_ROLE, COMMAND_ADMIN_ROLE_DESC)
                .addOptions(ADMIN_ROLE_OPTION));
    }

    static {
        COMMAND_AUTODELETE_DESC = commandsProperties.get(COMMAND_AUTODELETE).toString();
        commandDataList.add(Commands.slash(COMMAND_AUTODELETE, COMMAND_AUTODELETE_DESC)
                .addOptions(AUTO_DELETE_OPTION));
    }

    static {
        COMMAND_CONFIG_DESC = commandsProperties.get(COMMAND_CONFIG).toString();
        commandDataList.add(Commands.slash(COMMAND_CONFIG, COMMAND_CONFIG_DESC));
    }

    static {
        COMMAND_LANGUAGE_DESC = commandsProperties.get(COMMAND_LANGUAGE).toString();
        for (Language language : Language.values()) {
            REQUIRED_LANGUAGE_OPTION.addChoice(language.rawName, language.shortName);
        }
        commandDataList.add(Commands.slash(COMMAND_LANGUAGE, COMMAND_LANGUAGE_DESC)
                .addOptions(REQUIRED_LANGUAGE_OPTION));
    }

    static {
        COMMAND_SERVER_DESC = commandsProperties.get(COMMAND_SERVER).toString();
        for (ServerSetting serverSetting : ServerSetting.values()) {
            SERVER_SETTING_OPTION.addChoice(serverSetting.rawName, serverSetting.rawName);
        }
        commandDataList.add(Commands.slash(COMMAND_SERVER, COMMAND_SERVER_DESC)
                .addOptions(SERVER_SETTING_OPTION, SERVER_SETTING_BOOL_OPTION));
    }

    static {
        COMMAND_TIMEZONE_DESC = commandsProperties.get(COMMAND_TIMEZONE).toString();
        for (int i = 12; i > -12; i--) {
            String timezoneMessage;
            if (i > 0) {
                timezoneMessage = "GMT+" + i;
            } else if (i == 0) {
                timezoneMessage = "GMT";
            } else {
                timezoneMessage = "GMT" + i;
            }
            REQUIRED_TIMEZONE_OPTION.addChoice(timezoneMessage, timezoneMessage);
        }
        commandDataList.add(Commands.slash(COMMAND_TIMEZONE, COMMAND_TIMEZONE_DESC)
                .addOptions(REQUIRED_TIMEZONE_OPTION));
    }

    static {
        COMMAND_WARN_TIME_DESC = commandsProperties.get(COMMAND_WARN_TIME).toString();
        for (int i = 1; i < 16; i++) {
            REQUIRED_WARN_TIME_OPTION.addChoice(String.valueOf(i), i);
        }
        commandDataList.add(Commands.slash(COMMAND_WARN_TIME, COMMAND_WARN_TIME_DESC)
                .addOptions(REQUIRED_WARN_TIME_OPTION));
    }

    private CommandsUtil() {
        // static use only
    }
}
