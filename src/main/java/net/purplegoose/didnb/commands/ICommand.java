package net.purplegoose.didnb.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.purplegoose.didnb.data.LoggingInformation;

public interface ICommand {

    void performCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo);

}
