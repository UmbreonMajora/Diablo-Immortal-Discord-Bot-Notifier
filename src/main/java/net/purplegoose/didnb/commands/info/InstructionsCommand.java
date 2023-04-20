package net.purplegoose.didnb.commands.info;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.LoggingInformation;

import static net.purplegoose.didnb.utils.StringUtil.NEW_LINE;

/**
 * @author Umbreon Majora
 * <p>
 * Show's use an instruction on how to install the bot.
 * <p>
 * Command: /instructions
 */
@Slf4j
public class InstructionsCommand implements IClientCommand {

    @Override
    public void runCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        log.info("{} used /instructions. Guild: {}({}). Channel: {}({})",
                logInfo.getExecutor(), logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
        replyEphemeralToUser(event, buildInstallationMessage());
    }

    private String buildInstallationMessage() {
        return "**Installation:**" + NEW_LINE +
                "```" + NEW_LINE +
                "You can skip step 2 and 3 if you're the server owner." + NEW_LINE +
                "   " + NEW_LINE +
                "1. Add the Bot to your Discord Server" + NEW_LINE +
                "   using: https://discord.com/api/oauth2/authorize?client_id=527511535309029407&permissions=8&scope=bot%20applications.commands" + NEW_LINE +
                "2. Create a role on your discord server called \"Bot Admin\". People with that role can control the bot." + NEW_LINE +
                "3. Assign the created role (Bot Admin) to yourself! The bot checks on that role." + NEW_LINE +
                "4. Create a textchannel you like to get the notifications in." + NEW_LINE +
                "5. Use /timezone <timezone> to set your timezone. You can see what GMT timezone matches your time using" + NEW_LINE +
                "   /timezones." + NEW_LINE +
                "6. Register your created channel as notifier-channel using /register. On default all messages are enabled." + NEW_LINE +
                "7. You can enable or disable an event using /event <event> on/off. See all events using /event list." + NEW_LINE +
                "8. See what events are enabled using /info." + NEW_LINE +
                "9. On default the bot will mention @everyone, change that using /role <Role>." + NEW_LINE +
                "10. That's it. use /help to see all commands. And to unregister a channel use /unregister." + NEW_LINE +
                "```";
    }
}