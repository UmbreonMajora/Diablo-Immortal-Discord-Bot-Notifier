package net.purplegoose.didnb.cache;

import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.enums.Language;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GuildsCache {

    private Map<String, ClientGuild> guilds = new ConcurrentHashMap<>();

    public void addGuild(ClientGuild clientGuild) {
        guilds.put(clientGuild.getGuildID(), clientGuild);
    }

    public String getGuildTimeZone(String guildID) {
        return guilds.get(guildID).getTimeZone();
    }

    public String getGuildAdminRoleID(String guildID) {
        return guilds.get(guildID).getGuildAdminRoleID();
    }

    public boolean isGuildRegistered(String guildID) {
        return guilds.containsKey(guildID);
    }

    public ClientGuild getClientGuildByID(String guildID) {
        return guilds.get(guildID);
    }

    public Language getLanguageByGuildID(String guildID) {
        return guilds.get(guildID).getLanguage();
    }

    public Map<String, ClientGuild> getAllGuilds() {
        return guilds;
    }

    public void setGuilds(Map<String, ClientGuild> guilds) {
        this.guilds = guilds;
    }

}
