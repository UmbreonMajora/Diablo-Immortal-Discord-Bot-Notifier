package me.umbreon.didn.events;

import me.umbreon.didn.utils.CommandsUtil;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildReady extends ListenerAdapter {

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        event.getGuild().updateCommands().addCommands(CommandsUtil.getCommandDataList()).queue();
    }

}
