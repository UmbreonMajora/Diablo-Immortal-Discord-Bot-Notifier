package me.umbreon.didn.events;

import me.umbreon.didn.utils.CommandsUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildReady extends ListenerAdapter {

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        Guild guild = event.getGuild();
        guild.updateCommands().addCommands(CommandsUtil.getCommandDataList()).queue();
    }

}
