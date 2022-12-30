package me.umbreon.didn.events;

import me.umbreon.didn.utils.CommandsUtil;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildJoin extends ListenerAdapter {

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        event.getGuild().updateCommands().addCommands(CommandsUtil.getCommandDataList()).queue();
    }
}
