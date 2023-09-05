package net.purplegoose.didnb.commands.scheduled_events;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ScheduledEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.purplegoose.didnb.annotations.CommandAnnotation;
import net.purplegoose.didnb.commands.Command;
import net.purplegoose.didnb.data.LoggingInformation;

@Slf4j
@CommandAnnotation(
        name = "dse",
        usage = "/dse",
        description = "This command deletes all scheduled events."
)
public class DeleteAllEvents extends Command {

    public static final String COMMAND = "dse";

    @Override
    public void performCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        Guild guild = event.getGuild();
        if (guild == null) {
            createErrorLogEntry(logInfo, DeleteAllEvents.class, "Guild is null!");
            return;
        }
        for (ScheduledEvent scheduledEvent : guild.getScheduledEvents()) {
            scheduledEvent.delete().queue();
        }
        createUseLogEntry(logInfo, DeleteAllEvents.class);
        event.getHook().setEphemeral(true).sendMessage("Deleting all scheduled messages...").queue();
    }

}
