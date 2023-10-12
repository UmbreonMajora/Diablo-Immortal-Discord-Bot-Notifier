package net.purplegoose.didnb.utils;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class ChannelLogger {
    private final TextChannel channel;

    public ChannelLogger(TextChannel channel) {
        this.channel = channel;
    }

    public void sendChannelLog(String msg) {
        //channel.sendMessage(msg).queue();
    }
}
