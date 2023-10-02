package net.purplegoose.didnb.events;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.utils.CommandsUtil;
import org.jetbrains.annotations.NotNull;

@Slf4j
@AllArgsConstructor
public class GuildJoin extends ListenerAdapter {

    private final DatabaseRequests databaseRequests;
    private final GuildsCache guildsCache;

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        String guildID = event.getGuild().getId();

        if (!guildsCache.isGuildRegistered(guildID)) {
            ClientGuild clientGuild = new ClientGuild(guildID);
            guildsCache.addGuild(clientGuild);
            databaseRequests.createGuild(clientGuild);
            databaseRequests.createScheduledEventsSettings(guildID);
            log.info("Joined new guild. Registering " + guildID);
        }

        event.getGuild().updateCommands().addCommands(CommandsUtil.getCommandDataList()).queue();
    }
}
