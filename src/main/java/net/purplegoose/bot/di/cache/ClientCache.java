package net.purplegoose.bot.di.cache;

import net.purplegoose.bot.di.data.ClientGuild;

import java.util.HashMap;
import java.util.Map;

public class ClientCache {

    private final Map<String, ClientGuild> clientGuildsMap = new HashMap<>(750);

    public void addClientGuild(ClientGuild clientGuild) {
        clientGuildsMap.put(clientGuild.getGuildID(), clientGuild);
    }

    public void removeClientGuild(String guildID) {
        clientGuildsMap.remove(guildID);
    }

    public boolean doClientGuildExist(String guildID) {
        return clientGuildsMap.containsKey(guildID);
    }

    public ClientGuild getClientGuildByID(String guildID) {
        return clientGuildsMap.get(guildID);
    }
}
