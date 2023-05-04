package net.purplegoose.didnb.gameevents.embedded;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.purplegoose.didnb.cache.GameDataCache;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.data.EventGameData;
import net.purplegoose.didnb.data.NotificationChannel;
import net.purplegoose.didnb.gameevents.IGameEvent;
import net.purplegoose.didnb.utils.TimeUtil;

import java.util.Set;

import static net.purplegoose.didnb.utils.TimeUtil.HOURS_IN_SECONDS;

@Slf4j
@AllArgsConstructor
public class DemonGatesEmbed implements IGameEvent {

    private final GameDataCache gameDataCache;

    public void sendDemonGatesEmbedIfHappening(ClientGuild guild, NotificationChannel channel, JDA client) {
        if (!channel.isDemonGatesMessageEmbedEnabled()) return;

        Set<EventGameData> eventGameDataSet = gameDataCache.getDemonGatesDataSet();
        String timeZone = guild.getTimeZone();
        long embedLeadTime = guild.getEmbedLeadTime();
        String time = TimeUtil.getCurrentTimeMinusGivenHours(timeZone, embedLeadTime);
        String weekday = TimeUtil.getCurrentWeekday(timeZone);
        if (isEventAboutToStart(eventGameDataSet, time, weekday)) {
            TextChannel textChannel = client.getTextChannelById(channel.getTextChannelID());
            if (textChannel == null) {
                log.error("Failed to send embed. Text channel null. Guild: {}. Channel: {}",
                        guild.getGuildID(), channel.getTextChannelID());
                return;
            }
            textChannel.sendMessageEmbeds(createDemonGatesEmbed(timeZone, embedLeadTime)).queue();
        }
    }

    private MessageEmbed createDemonGatesEmbed(String timeZone, long leadTime) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        long unix = TimeUtil.getCurrentUnixTime(timeZone) + (HOURS_IN_SECONDS * leadTime);

        embedBuilder.setTitle("Demon Gates | World Event");
        embedBuilder.addField("Start time:", "<t:" + unix + ">", true);
        embedBuilder.addField("Time till start:", "<t:" + unix + ":R>", true);
        embedBuilder.addField("World Realm of Damnation", "Realm of Damnation", false);

        embedBuilder.setImage("https://media.diablofans.com/attachments/23/560/screenshot-2022-05-28-204043.jpg");

        return embedBuilder.build();
    }

}
