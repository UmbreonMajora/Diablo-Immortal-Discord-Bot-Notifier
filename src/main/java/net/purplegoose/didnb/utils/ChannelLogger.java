package net.purplegoose.didnb.utils;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.purplegoose.didnb.DiabloImmortalDiscordNotifier;

@Slf4j
public class ChannelLogger {
    private static final long CHANNEL_LOGGER_ID = 1150798272886227087L;
    private static final TextChannel CHANNEL_LOGGER_CHANNEL;

    static {
        CHANNEL_LOGGER_CHANNEL = DiabloImmortalDiscordNotifier.getJdaClient().getTextChannelById(CHANNEL_LOGGER_ID);
    }

    private ChannelLogger() {/* static use only */}

    public static void sendChannelLog(String msg) {
        if (CHANNEL_LOGGER_CHANNEL != null) {
            CHANNEL_LOGGER_CHANNEL.sendMessage(msg).queue();
        } else {
            log.error("Channel logger is null!");
        }
    }
}
