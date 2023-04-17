package net.purplegoose.didnb.commands.info;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;
import net.purplegoose.didnb.utils.TimeUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

/**
 * @author Umbreon Majora
 * <p>
 * Show's the user a list with GMT-11 to GMT+12 timezones & it's current time in 24hrs format.
 * <p>
 * Command: /timezones
 */
@Slf4j
@AllArgsConstructor
public class TimeZonesCommand implements IClientCommand {

    private final GuildsCache guildsCache;

    @Override
    public void runCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        String guildID = logInfo.getGuildID();
        Language language = guildsCache.getGuildLanguage(guildID);
        log.info("{} used /timezones. Guild: {}({}). Channel: {}({})",
                logInfo.getExecutor(), logInfo.getGuildName(), guildID, logInfo.getChannelName(), logInfo.getChannelID());
        replyEphemeralToUser(event, buildTimeZonesEmbed(language));
    }

    private MessageEmbed buildTimeZonesEmbed(Language language) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(LanguageController.getMessage(language, "TEXT-TIMEZONES"));
        embedBuilder.setColor(Color.RED);

        for (int i = 12; i > -12; i--) {
            String timezoneMessage;
            if (i > 0) {
                timezoneMessage = "GMT+" + i;
            } else if (i == 0) {
                timezoneMessage = "GMT";
            } else {
                timezoneMessage = "GMT" + i;
            }

            String currentTimeAsString = TimeUtil.getTimeWithWeekday(timezoneMessage);
            embedBuilder.addField(timezoneMessage, currentTimeAsString, true);
        }
        embedBuilder.setFooter(LanguageController.getMessage(language, "TIMES-IN-24-HOURS-FORMAT"));
        return embedBuilder.build();
    }
}
