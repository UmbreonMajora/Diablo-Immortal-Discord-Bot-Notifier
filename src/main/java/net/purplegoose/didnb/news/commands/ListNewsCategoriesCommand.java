package net.purplegoose.didnb.news.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.purplegoose.didnb.annotations.CommandAnnotation;
import net.purplegoose.didnb.commands.Command;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.news.enums.Categories;

import static net.purplegoose.didnb.utils.StringUtil.NEW_LINE;

@CommandAnnotation(
        name = "newscategories",
        usage = "/newscategories",
        description = "Show's a list with all categories in the news section."
)
public class ListNewsCategoriesCommand extends Command {
    public static final String COMMAND = "newscategories";
    public static final SlashCommandData commandData = Commands.slash(COMMAND,
            "Show's a list with all categories in the news section.");
    private static final MessageEmbed newsCategoriesEmbed;

    static {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Blizzard News Categories");
        StringBuilder stringBuilder = new StringBuilder(50);
        for (Categories category : Categories.values()) {
            stringBuilder.append(category.rawName)
                    .append(" = ")
                    .append(category.fullName)
                    .append(NEW_LINE);
        }
        embedBuilder.setDescription(stringBuilder.toString());
        embedBuilder.setFooter("purplegoose.net");
        newsCategoriesEmbed = embedBuilder.build();
    }

    @Override
    public void performCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        replyWithEmbedToUser(event, newsCategoriesEmbed, true);
        createUseLogEntry(logInfo, this.getClass());
    }
}