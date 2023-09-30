package net.purplegoose.didnb.news.commands;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.purplegoose.didnb.annotations.CommandAnnotation;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.Command;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.news.dto.NewsChannelDTO;
import net.purplegoose.didnb.news.enums.Categories;

import java.util.Map;

@AllArgsConstructor
@CommandAnnotation(
        name = "togglenews",
        description = "Enables or disables the receiving of a news category in a registered news channel.",
        usage = "/togglenews [news_category]"
)
public class ToggleNewsCommand extends Command {
    public static final String COMMAND = "togglenews";
    private static final String COMMAND_OPTION = "news_category";

    public static final SlashCommandData commandData;
    private static final OptionData commandOptionData = new OptionData(OptionType.STRING, COMMAND_OPTION, "Select the category of the news you want to receive.", true);

    private static final String FAILURE_LOG_MESSAGE = "%s tried to enable or disable in the receiving of news in " +
            "channel with id %s and guild with id %s but it failed the channel ist not registered";
    private static final String TOGGLE_FAILURE_CHANNEL_NOT_REGISTERD =
            "Failed to toggle news, channel is not registered.";

    static {
        commandData = Commands.slash(COMMAND, "desc");
        for (Categories category : Categories.values()) {
            commandOptionData.addChoice(category.rawName, category.rawName);
        }
        commandData.addOptions(commandOptionData);
    }

    private final DatabaseRequests databaseRequests;
    private final GuildsCache guildsCache;

    @Override
    public void performCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        String guildID = logInfo.getGuildID();
        String channelID = logInfo.getChannelID();
        if (!isChannelRegistered(guildID, channelID)) {
            replyToUser(event, TOGGLE_FAILURE_CHANNEL_NOT_REGISTERD, true);
            String user = logInfo.getExecutor();
            createErrorLogEntry(logInfo, this.getClass(), String.format(FAILURE_LOG_MESSAGE, user, channelID, guildID));
            return;
        }

        NewsChannelDTO newsChannelDTO = guildsCache.getClientGuildByID(guildID).getNewsChannels().get(channelID);
        String selectedNews = getSelectedNews(event);
        if (toggleSelectedNews(selectedNews, newsChannelDTO)) {
            databaseRequests.updateNewsChannel(newsChannelDTO);
            replyToUser(event, "Successfully toggled " + selectedNews, true);
            createUseLogEntry(logInfo, this.getClass());
        } else {
            replyToUser(event, "Failed to toggle " + selectedNews, true);
            createUseLogEntry(logInfo, this.getClass());
        }
    }

    private boolean isChannelRegistered(String guildID, String channelID) {
        Map<String, NewsChannelDTO> guildNewsChannels = guildsCache.getClientGuildByID(guildID).getNewsChannels();
        return guildNewsChannels.containsKey(channelID);
    }

    private String getSelectedNews(SlashCommandInteractionEvent event) {
        OptionMapping optionMapping = event.getOption(COMMAND_OPTION);
        return optionMapping != null ? optionMapping.getAsString() : null;
    }

    private boolean toggleSelectedNews(String selectedNews, NewsChannelDTO newsChannelDTO) {
        Categories cat = Categories.getCategoriesByRawName(selectedNews);
        if (cat == null)
            return false;
        switch (cat) {
            case DI -> {
                boolean currentValue = newsChannelDTO.isDiabloImmortalNewsEnabled();
                newsChannelDTO.setDiabloImmortalNewsEnabled(!currentValue);
            }
            case HEARTSTONE -> {
                boolean currentValue = newsChannelDTO.isHearthStoneNewsEnabled();
                newsChannelDTO.setHearthStoneNewsEnabled(!currentValue);
            }
            default -> {
                return false;
            }
        }
        return true;
    }
}
