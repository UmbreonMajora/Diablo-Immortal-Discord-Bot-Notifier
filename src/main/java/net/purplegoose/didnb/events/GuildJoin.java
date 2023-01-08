package net.purplegoose.didnb.events;

import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.utils.CommandsUtil;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuildJoin extends ListenerAdapter {

    private final Logger logger = LoggerFactory.getLogger(GuildJoin.class);

    private final DatabaseRequests databaseRequests;
    private final GuildsCache guildsCache;

    public GuildJoin(DatabaseRequests databaseRequests, GuildsCache guildsCache) {
        this.databaseRequests = databaseRequests;
        this.guildsCache = guildsCache;
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        String guildID = event.getGuild().getId();

        if (!guildsCache.isGuildRegistered(guildID)) {
            ClientGuild clientGuild = new ClientGuild(guildID);
            guildsCache.addGuild(clientGuild);
            databaseRequests.createGuild(clientGuild);
            logger.info("Joined new guild. Registering " + guildID);
        }

        event.getGuild().updateCommands().addCommands(CommandsUtil.getCommandDataList()).queue();
    }
}
