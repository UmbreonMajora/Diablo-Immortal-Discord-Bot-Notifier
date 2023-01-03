package me.umbreon.didn.utils;

import me.umbreon.didn.enums.*;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CommandsUtil {

    private static final Properties commandsProperties = new Properties();
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandsUtil.class);

    // -> Command Options

    public static final String EVENT_NAME_OPTION_NAME = "eventname";
    private static final OptionData REQUIRED_EVENT_NAME_OPTION =
            new OptionData(OptionType.STRING, EVENT_NAME_OPTION_NAME, "Allows you to enable or disable a specific event notification for that channel.", true);

    public static final String EVENT_VALUE_OPTION_NAME = "eventvalue";
    private static final OptionData REQUIRED_EVENT_VALUE_OPTION =
            new OptionData(OptionType.BOOLEAN, EVENT_VALUE_OPTION_NAME, "Select that event you want to change.", true);

    public static final String TARGET_CHANNEL_OPTION_NAME = "targetchannel";
    private static final OptionData NOT_REQUIRED_CHANNEL_OPTION =
            new OptionData(OptionType.CHANNEL, TARGET_CHANNEL_OPTION_NAME, "Enter here your channel.", false);

    public static final String ROLE_OPTION_NAME = "role";
    private static final OptionData REQUIRED_ROLE_OPTION =
            new OptionData(OptionType.ROLE, ROLE_OPTION_NAME, "Enter here your role", true);

    public static final String PRESET_OPTION_NAME = "preset";
    private static final OptionData REQUIRED_PRESET_OPTION =
            new OptionData(OptionType.STRING, PRESET_OPTION_NAME, "Apply a preset on your notification channel.", true);

    public static final String WEEKDAY_OPTION_NAME = "weekday";
    private static final OptionData CREATE_CUSTOM_MESSAGE_WEEKDAY_OPTION =
            new OptionData(OptionType.STRING, WEEKDAY_OPTION_NAME, "At what day would you like to get the message?", true);

    public static final String TIME_OPTION_NAME = "time";
    private static final OptionData CREATE_CUSTOM_MESSAGE_TIME_OPTION =
            new OptionData(OptionType.STRING, TIME_OPTION_NAME, "At what time would you like to get the message?", true);

    public static final String REPEATING_OPTION_NAME = "repeating";
    private static final OptionData CREATE_CUSTOM_MESSAGE_REPEATING_OPTION =
            new OptionData(OptionType.BOOLEAN, REPEATING_OPTION_NAME, "Should the message be sent every week or only once?", true);

    public static final String MESSAGE_OPTION_NAME = "message";
    private static final OptionData CREATE_CUSTOM_MESSAGE_MESSAGE_OPTION =
            new OptionData(OptionType.STRING, MESSAGE_OPTION_NAME, "Enter here your message.", true);

    public static final String CUSTOM_MESSAGE_ID_OPTION_NAME = "custommessageid";
    private static final OptionData REQUIRED_CUSTOM_MESSAGE_ID_OPTION =
            new OptionData(OptionType.INTEGER, CUSTOM_MESSAGE_ID_OPTION_NAME, "Enter your custom message ID here.", true);

    public static final String MESSAGE_ID_OPTION_NAME = "messageid";
    private static final OptionData REQUIRED_MESSAGE_ID_OPTION =
            new OptionData(OptionType.STRING, MESSAGE_ID_OPTION_NAME, "Enter here your message ID.", true);

    public static final String EMOTE_OPTION_NAME = "emote";
    private static final OptionData REQUIRED_EMOTE_OPTION =
            new OptionData(OptionType.STRING, EMOTE_OPTION_NAME, "Enter here your emote", true);

    public static final String LANGUAGE_OPTION_NAME = "language";
    private static final OptionData REQUIRED_LANGUAGE_OPTION =
            new OptionData(OptionType.STRING, LANGUAGE_OPTION_NAME, "Enter your language here.", true);

    public static final String SERVER_SETTING_OPTION_NAME = "serversetting";
    private static final OptionData REQUIRED_SERVER_SETTING_OPTION =
            new OptionData(OptionType.STRING, SERVER_SETTING_OPTION_NAME, "Select your server setting here.", true);

    public static final String SERVER_SETTING_VALUE_OPTION_NAME = "servervalue";
    private static final OptionData REQUIRED_SERVER_VALUE_OPTION =
            new OptionData(OptionType.BOOLEAN, SERVER_SETTING_VALUE_OPTION_NAME, "Turn this setting on or off?", true);

    public static final String TIMEZONE_OPTION_NAME = "timezone";
    private static final OptionData REQUIRED_TIMEZONE_OPTION =
            new OptionData(OptionType.STRING, TIMEZONE_OPTION_NAME, "Select here your matching GMT timezone. " +
                    "Use /timzones to see what timezone would match you.", true);

    public static final String WARN_TIME_OPTION_NAME = "warntime";
    private static final OptionData REQUIRED_WARN_TIME_OPTION =
            new OptionData(OptionType.INTEGER, WARN_TIME_OPTION_NAME, "Change your warn message time", true);

    public static final String MESSAGE_VALUE_OPTION_NAME = "messagevalue";
    private static final OptionData REQUIRED_MESSAGE_VALUE_OPTION =
            new OptionData(OptionType.BOOLEAN, MESSAGE_VALUE_OPTION_NAME, "Enable or disable your custom " +
                    "notification message", true);

    public static final String BOOLEAN_OPTION_NAME = "boolean";
    private static final OptionData REQUIRED_BOOLEAN_OPTION =
            new OptionData(OptionType.BOOLEAN, BOOLEAN_OPTION_NAME, "Enable or disable it.", true);

    public static final String EDIT_MESSAGE_OPTION_NAME = "editmessage";
    private static final OptionData REQUIRED_EDIT_MESSAGE_OPTION =
            new OptionData(OptionType.STRING, EDIT_MESSAGE_OPTION_NAME, "What would you like to change?", true);

    public static final String EDIT_MESSAGE_VALUE_OPTION_NAME = "editmessagevalue";
    private static final OptionData REQUIRED_EDIT_MESSAGE_VALUE_OPTION =
            new OptionData(OptionType.STRING, EDIT_MESSAGE_VALUE_OPTION_NAME, "Enter here your new value.", true);

    // -> Commands & Command Descriptions

    public static final String COMMAND_EVENT = "event";
    private static final String COMMAND_EVENT_DESC;

    public static final String COMMAND_INFO = "info";
    private static final String COMMAND_INFO_DESC;

    public static final String COMMAND_LIST_EVENTS = "listevents";
    private static final String COMMAND_LIST_EVENTS_DESC;

    public static final String COMMAND_MENTION_ROLE = "mentionrole";
    private static final String COMMAND_MENTION_ROLE_DESC;

    public static final String COMMAND_PRESET = "preset";
    private static final String COMMAND_PRESET_DESC;

    public static final String COMMAND_REGISTER = "register";
    private static final String COMMAND_REGISTER_DESC;

    public static final String COMMAND_UNREGISTER = "unregister";
    private static final String COMMAND_UNREGISTER_DESC;

    public static final String COMMAND_CREATE_MESSAGE = "createmessage";
    private static final String COMMAND_CREATE_MESSAGE_DESC;

    public static final String COMMAND_MESSAGE = "message";
    private static final String COMMAND_MESSAGE_DESC;

    public static final String COMMAND_CUSTOM_MESSAGE_INFO = "messageinfo";
    private static final String COMMAND_CUSTOM_MESSAGE_INFO_DESC;

    public static final String COMMAND_DELETE_CUSTOM_MESSAGE = "deletecustommessage";
    private static final String COMMAND_DELETE_CUSTOM_MESSAGE_DESC;

    public static final String COMMAND_HELP = "help";
    private static final String COMMAND_HELP_DESC;

    public static final String COMMAND_INSTALL = "install";
    private static final String COMMAND_INSTALL_DESC;

    public static final String COMMAND_LANGUAGES = "languages";
    private static final String COMMAND_LANGUAGES_DESC;

    public static final String COMMAND_LIST_MESSAGES = "listcustommessages";
    private static final String COMMAND_LIST_MESSAGES_DESC;

    public static final String COMMAND_TIMEZONES = "timezones";
    private static final String COMMAND_TIMEZONES_DESC;

    public static final String COMMAND_TODAY = "today";
    private static final String COMMAND_TODAY_DESC;

    public static final String COMMAND_UPCOMING = "upcoming";
    private static final String COMMAND_UPCOMING_DESC;

    public static final String COMMAND_CREATE_REACTION_ROLE = "createreactionrole";
    private static final String COMMAND_CREATE_REACTION_ROLE_DESC;

    public static final String COMMAND_WARN_TIME = "warntime";
    private static final String COMMAND_WARN_TIME_DESC;

    public static final String COMMAND_LIST_REACTION_ROLES = "listreactionrole";
    private static final String COMMAND_LIST_REACTION_ROLES_DESC;

    public static final String COMMAND_REMOVE_REACTION_ROLE = "removereactionrole";
    private static final String COMMAND_REMOVE_REACTION_ROLE_DESC;

    public static final String COMMAND_ADMIN_ROLE = "adminrole";
    private static final String COMMAND_ADMIN_ROLE_DESC;

    public static final String COMMAND_CONFIG = "config";
    private static final String COMMAND_CONFIG_DESC;

    public static final String COMMAND_LANGUAGE = "language";
    private static final String COMMAND_LANGUAGE_DESC;

    public static final String COMMAND_SERVER = "server";
    private static final String COMMAND_SERVER_DESC;

    public static final String COMMAND_TIMEZONE = "timezone";
    private static final String COMMAND_TIMEZONE_DESC;

    public static final String COMMAND_DAYLIGHTTIME = "daylighttime";
    private static final String COMMAND_DAYLIGHTTIME_DESC;

    public static final String COMMAND_EDIT_MESSAGE = "editmessage";
    private static final String COMMAND_EDIT_MESSAGE_DESC;

    static {
        try (InputStream inputStream = CommandsUtil.class.getClassLoader().getResourceAsStream("commands.properties")) {
            if (inputStream != null) {
                commandsProperties.load(inputStream);
                LOGGER.info("Loaded command.properties file.");
            } else {
                LOGGER.warn("Failed to load command.properties! Input stream for command.properties was null.");
            }
        } catch (IOException e) {
            LOGGER.warn("Failed to load command.properties!", e);
            e.printStackTrace();
        }

        for (GameEvent gameEvent : GameEvent.values()) {
            REQUIRED_EVENT_NAME_OPTION.addChoice(gameEvent.rawName, gameEvent.rawName);
        }

        for (ServerSetting serverSetting : ServerSetting.values()) {
            REQUIRED_SERVER_SETTING_OPTION.addChoice(serverSetting.rawName, serverSetting.rawName);
        }

        for (Preset preset : Preset.values()) {
            REQUIRED_PRESET_OPTION.addChoice(preset.presetDescription, preset.getAsChoiceName);
        }

        for (Weekday weekday : Weekday.values()) {
            CREATE_CUSTOM_MESSAGE_WEEKDAY_OPTION.addChoice(weekday.name, weekday.rawName);
        }

        for (Language language : Language.values()) {
            REQUIRED_LANGUAGE_OPTION.addChoice(language.rawName, language.shortName);
        }

        for (int i = 1; i < 16; i++) {
            REQUIRED_WARN_TIME_OPTION.addChoice(String.valueOf(i), i);
        }

        REQUIRED_EDIT_MESSAGE_OPTION.addChoice("time", "time");
        REQUIRED_EDIT_MESSAGE_OPTION.addChoice("message", "message");
        REQUIRED_EDIT_MESSAGE_OPTION.addChoice("weekday", "weekday");

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

        COMMAND_EVENT_DESC = commandsProperties.get(COMMAND_EVENT).toString();
        COMMAND_INFO_DESC = commandsProperties.get(COMMAND_INFO).toString();
        COMMAND_LIST_EVENTS_DESC = commandsProperties.get(COMMAND_LIST_EVENTS).toString();
        COMMAND_MENTION_ROLE_DESC = commandsProperties.get(COMMAND_MENTION_ROLE).toString();
        COMMAND_PRESET_DESC = commandsProperties.get(COMMAND_PRESET).toString();
        COMMAND_REGISTER_DESC = commandsProperties.get(COMMAND_REGISTER).toString();
        COMMAND_UNREGISTER_DESC = commandsProperties.get(COMMAND_UNREGISTER).toString();
        COMMAND_CREATE_MESSAGE_DESC = commandsProperties.get(COMMAND_CREATE_MESSAGE).toString();
        COMMAND_MESSAGE_DESC = commandsProperties.get(COMMAND_MESSAGE).toString();
        COMMAND_CUSTOM_MESSAGE_INFO_DESC = commandsProperties.get(COMMAND_CUSTOM_MESSAGE_INFO).toString();
        COMMAND_DELETE_CUSTOM_MESSAGE_DESC = commandsProperties.get(COMMAND_DELETE_CUSTOM_MESSAGE).toString();
        COMMAND_LIST_MESSAGES_DESC = commandsProperties.get(COMMAND_LIST_MESSAGES).toString();
        COMMAND_HELP_DESC = commandsProperties.get(COMMAND_HELP).toString();
        COMMAND_INSTALL_DESC = commandsProperties.get(COMMAND_INSTALL).toString();
        COMMAND_LANGUAGES_DESC = commandsProperties.get(COMMAND_LANGUAGES).toString();
        COMMAND_TIMEZONES_DESC = commandsProperties.get(COMMAND_TIMEZONES).toString();
        COMMAND_TODAY_DESC = commandsProperties.get(COMMAND_TODAY).toString();
        COMMAND_UPCOMING_DESC = commandsProperties.get(COMMAND_UPCOMING).toString();
        COMMAND_CREATE_REACTION_ROLE_DESC = commandsProperties.get(COMMAND_CREATE_REACTION_ROLE).toString();
        COMMAND_WARN_TIME_DESC = commandsProperties.get(COMMAND_WARN_TIME).toString();
        COMMAND_LIST_REACTION_ROLES_DESC = commandsProperties.get(COMMAND_LIST_REACTION_ROLES).toString();
        COMMAND_REMOVE_REACTION_ROLE_DESC = commandsProperties.get(COMMAND_REMOVE_REACTION_ROLE).toString();
        COMMAND_ADMIN_ROLE_DESC = commandsProperties.get(COMMAND_ADMIN_ROLE).toString();
        COMMAND_CONFIG_DESC = commandsProperties.get(COMMAND_CONFIG).toString();
        COMMAND_LANGUAGE_DESC = commandsProperties.get(COMMAND_LANGUAGE).toString();
        COMMAND_SERVER_DESC = commandsProperties.get(COMMAND_SERVER).toString();
        COMMAND_TIMEZONE_DESC = commandsProperties.get(COMMAND_TIMEZONE).toString();
        COMMAND_DAYLIGHTTIME_DESC = commandsProperties.get(COMMAND_DAYLIGHTTIME).toString();
        COMMAND_EDIT_MESSAGE_DESC = commandsProperties.get(COMMAND_EDIT_MESSAGE).toString();
    }

    public static Properties getCommandsProperties() {
        return commandsProperties;
    }

    public static List<CommandData> getCommandDataList() {
        List<CommandData> commandDataList = new ArrayList<>();

        // -> /event <GAME_EVENT> <ON/OFF>
        commandDataList.add(Commands.slash(COMMAND_EVENT, COMMAND_EVENT_DESC)
                .addOptions(REQUIRED_EVENT_NAME_OPTION, REQUIRED_EVENT_VALUE_OPTION));
        // -> /info <CHANNEL>
        commandDataList.add(Commands.slash(COMMAND_INFO, COMMAND_INFO_DESC)
                .addOptions(NOT_REQUIRED_CHANNEL_OPTION));
        // -> /listevents
        commandDataList.add(Commands.slash(COMMAND_LIST_EVENTS, COMMAND_LIST_EVENTS_DESC));
        // -> /mentionrole <ROLE> <CHANNEL>
        commandDataList.add(Commands.slash(COMMAND_MENTION_ROLE, COMMAND_MENTION_ROLE_DESC)
                .addOptions(REQUIRED_ROLE_OPTION, NOT_REQUIRED_CHANNEL_OPTION));
        // -> /preset <PRESET>
        commandDataList.add(Commands.slash(COMMAND_PRESET, COMMAND_PRESET_DESC)
                .addOptions(REQUIRED_PRESET_OPTION));
        // -> /register <CHANNEL>
        commandDataList.add(Commands.slash(COMMAND_REGISTER, COMMAND_REGISTER_DESC)
                .addOptions(NOT_REQUIRED_CHANNEL_OPTION));
        // -> /unregister <CHANNEL>
        commandDataList.add(Commands.slash(COMMAND_UNREGISTER, COMMAND_UNREGISTER_DESC)
                .addOptions(NOT_REQUIRED_CHANNEL_OPTION));
        // -> /createcustommessage <WEEKDAY> <TIME> <BOOL_REPEATING> <MESSAGE>
        commandDataList.add(Commands.slash(COMMAND_CREATE_MESSAGE, COMMAND_CREATE_MESSAGE_DESC)
                .addOptions(CREATE_CUSTOM_MESSAGE_WEEKDAY_OPTION, CREATE_CUSTOM_MESSAGE_TIME_OPTION, CREATE_CUSTOM_MESSAGE_REPEATING_OPTION, CREATE_CUSTOM_MESSAGE_MESSAGE_OPTION));
        // -> /custommessageinfo <ID>
        commandDataList.add(Commands.slash(COMMAND_CUSTOM_MESSAGE_INFO, COMMAND_CUSTOM_MESSAGE_INFO_DESC)
                .addOptions(REQUIRED_CUSTOM_MESSAGE_ID_OPTION));
        // -> /deletecustommessage <ID>
        commandDataList.add(Commands.slash(COMMAND_DELETE_CUSTOM_MESSAGE, COMMAND_DELETE_CUSTOM_MESSAGE_DESC)
                .addOptions(REQUIRED_CUSTOM_MESSAGE_ID_OPTION));
        // -> /editcustommessage <ID>
        commandDataList.add(Commands.slash(COMMAND_EDIT_MESSAGE, COMMAND_EDIT_MESSAGE_DESC)
                .addOptions(REQUIRED_CUSTOM_MESSAGE_ID_OPTION, REQUIRED_EDIT_MESSAGE_OPTION, REQUIRED_EDIT_MESSAGE_VALUE_OPTION));
        // -> /listcustommessages
        commandDataList.add(Commands.slash(COMMAND_LIST_MESSAGES, COMMAND_LIST_MESSAGES_DESC));
        // -> /help
        commandDataList.add(Commands.slash(COMMAND_HELP, COMMAND_HELP_DESC));
        // -> /install
        commandDataList.add(Commands.slash(COMMAND_INSTALL, COMMAND_INSTALL_DESC));
        // -> /languages
        commandDataList.add(Commands.slash(COMMAND_LANGUAGES, COMMAND_LANGUAGES_DESC));
        // -> /timezones
        commandDataList.add(Commands.slash(COMMAND_TIMEZONES, COMMAND_TIMEZONES_DESC));
        // -> /today
        commandDataList.add(Commands.slash(COMMAND_TODAY, COMMAND_TODAY_DESC));
        // -> /upcoming
        commandDataList.add(Commands.slash(COMMAND_UPCOMING, COMMAND_UPCOMING_DESC));
        // -> Command: /createreactionrole [Required: messageid] [Required: emote] [Required: role]
        commandDataList.add(Commands.slash(COMMAND_CREATE_REACTION_ROLE, COMMAND_CREATE_REACTION_ROLE_DESC)
                .addOptions(REQUIRED_MESSAGE_ID_OPTION, REQUIRED_ROLE_OPTION, REQUIRED_EMOTE_OPTION));
        // -> Command: /listreactionroles
        commandDataList.add(Commands.slash(COMMAND_LIST_REACTION_ROLES, COMMAND_LIST_REACTION_ROLES_DESC));
        // -> Command: /removereactionrole [Required: MessageID] [Required: Emoji]
        commandDataList.add(Commands.slash(COMMAND_REMOVE_REACTION_ROLE, COMMAND_REMOVE_REACTION_ROLE_DESC)
                .addOptions(REQUIRED_MESSAGE_ID_OPTION, REQUIRED_EMOTE_OPTION));
        // -> Command: /adminrole [Required: role]
        commandDataList.add(Commands.slash(COMMAND_ADMIN_ROLE, COMMAND_ADMIN_ROLE_DESC)
                .addOptions(REQUIRED_ROLE_OPTION));
        // -> Command: /config
        commandDataList.add(Commands.slash(COMMAND_CONFIG, COMMAND_CONFIG_DESC));
        // -> Command: /language [Required: language]
        commandDataList.add(Commands.slash(COMMAND_LANGUAGE, COMMAND_LANGUAGE_DESC)
                .addOptions(REQUIRED_LANGUAGE_OPTION));
        // -> Command: /server [Required: serversetting] [Required: servervalue]
        commandDataList.add(Commands.slash(COMMAND_SERVER, COMMAND_SERVER_DESC)
                .addOptions(REQUIRED_SERVER_SETTING_OPTION, REQUIRED_SERVER_VALUE_OPTION));
        // -> Command: /timezone [Required: timezone]
        commandDataList.add(Commands.slash(COMMAND_TIMEZONE, COMMAND_TIMEZONE_DESC)
                .addOptions(REQUIRED_TIMEZONE_OPTION));
        // -> Command: /warntime [Required: warntime]
        commandDataList.add(Commands.slash(COMMAND_WARN_TIME, COMMAND_WARN_TIME_DESC)
                .addOptions(REQUIRED_WARN_TIME_OPTION));
        // -> Commmand: /message [Required: messagevalue] [Required: MessageID]
        commandDataList.add(Commands.slash(COMMAND_MESSAGE, COMMAND_MESSAGE_DESC)
                .addOptions(REQUIRED_MESSAGE_VALUE_OPTION, REQUIRED_MESSAGE_ID_OPTION));
        // -> Command: /daylighttime [Required: boolean]
        //commandDataList.add(Commands.slash(COMMAND_DAYLIGHTTIME, COMMAND_DAYLIGHTTIME_DESC)
        //        .addOptions(REQUIRED_BOOLEAN_OPTION)); todo this command is not working properly?

        return commandDataList;
    }
}
