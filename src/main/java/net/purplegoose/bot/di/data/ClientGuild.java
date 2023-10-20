package net.purplegoose.bot.di.data;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClientGuild {
    private final String guildID;
    private String botAdminRoleID = null;
    private List<ClientChannel> channelsList = new ArrayList<>();

    public ClientGuild(String guildID) {
        this.guildID = guildID;
    }

    public void addChannel(ClientChannel clientChannel) {
        if (doChannelExists(clientChannel)) return;
        channelsList.add(clientChannel);
    }

    public boolean doChannelExists(ClientChannel clientChannel) {
        for (ClientChannel channel : channelsList) {
            if (channel.getChannelID().equalsIgnoreCase(clientChannel.getChannelID()) &&
                    channel.getGameEvent().rawName.equalsIgnoreCase(clientChannel.getGameEvent().rawName)) {
                return true;
            }
        }
        return false;
    }
}