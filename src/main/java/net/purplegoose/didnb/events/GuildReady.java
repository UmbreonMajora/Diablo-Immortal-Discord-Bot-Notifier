package net.purplegoose.didnb.events;

import lombok.extern.slf4j.Slf4j;
import net.purplegoose.didnb.utils.CommandsUtil;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
@Slf4j
public class GuildReady extends ListenerAdapter {

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        log.info("Registered commands in guild {}({})", event.getGuild().getName(), event.getGuild().getId());
        event.getGuild().updateCommands().addCommands(CommandsUtil.getCommandDataList()).queue();
    }

}
