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
import net.purplegoose.didnb.news.exceptions.ToggleFailureException;

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

    static {
        commandData = Commands.slash(COMMAND, "desc");
        for (Categories category : Categories.values()) {
            commandOptionData.addChoice(category.rawName, category.rawName);
        }
        commandData.addOptions(commandOptionData);
    }

    //todo: add to localization
    private static final String CHANNEL_NOT_REGISTERED_FAILURE_LOG_MESSAGE = "%s tried to enable or disable in the " +
            "receiving of news in channel with id %s and guild with id %s but it failed the channel is not registered.";
    private static final String FAILED_TO_TOGGLE_NEWS_CHANNEL_IS_NOT_REGISTERED =
            "Failed to toggle news, channel is not registered.";
    private static final String TOGGLE_SUCCESS_MESSAGE = "Successfully set 'receive %s news' to '%s'.";
    private static final String TOGGLE_FAILED_MESSAGE = "Failed to toggle 'receive %s news'. Please try again.";

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    @Override
    public void performCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        String guildID = logInfo.getGuildID();
        String channelID = logInfo.getChannelID();

        if (!isChannelRegistered(guildID, channelID)) {
            replyWithMessageToUser(event, FAILED_TO_TOGGLE_NEWS_CHANNEL_IS_NOT_REGISTERED, true);
            logChannelNotRegisteredFailure(logInfo);
            return;
        }

        NewsChannelDTO newsChannelDTO = guildsCache.getClientGuildByID(guildID).getNewsChannelByChannelID(channelID);
        String selectedNews = getSelectedNews(event);
        createUseLogEntry(logInfo, this.getClass());

        try {
            toggleSelectedNews(selectedNews, newsChannelDTO);
            replySuccessToUser(event, newsChannelDTO, selectedNews);
        } catch (ToggleFailureException e) {
            replyFailureToUser(event, selectedNews);
        }
    }

    private void replySuccessToUser(SlashCommandInteractionEvent event, NewsChannelDTO newsChannelDTO,
                                    String selectedNews) throws ToggleFailureException {
        boolean value = getCurrentValue(selectedNews, newsChannelDTO);
        String replyMessage = String.format(TOGGLE_SUCCESS_MESSAGE, selectedNews, value);
        replyWithMessageToUser(event, replyMessage, true);
    }

    private void replyFailureToUser(SlashCommandInteractionEvent event, String selectedNews) {
        String replyMessage = String.format(TOGGLE_FAILED_MESSAGE, selectedNews);
        replyWithMessageToUser(event, replyMessage, true);
    }

    private void logChannelNotRegisteredFailure(LoggingInformation logInfo) {
        String user = logInfo.getExecutor();
        String channelID = logInfo.getChannelID();
        String guildID = logInfo.getGuildID();
        String replyMessage = String.format(CHANNEL_NOT_REGISTERED_FAILURE_LOG_MESSAGE, user, channelID, guildID);
        createErrorLogEntry(logInfo, this.getClass(), replyMessage);
    }

    private boolean getCurrentValue(String selectedNews, NewsChannelDTO newsChannelDTO) throws ToggleFailureException {
        Categories cat = Categories.getCategoriesByRawName(selectedNews);
        if (cat == null)
            throw new ToggleFailureException("Failed to toggle news setting. The category was null.");
        switch (cat) {
            case DI -> {
                return newsChannelDTO.isDiabloImmortalNewsEnabled();
            }
            case HEARTHSTONE -> {
                return newsChannelDTO.isHearthStoneNewsEnabled();
            }
            case OVERWATCH -> {
                return newsChannelDTO.isOverwatchEnabled();
            }
            case BNET -> {
                return newsChannelDTO.isBnetEnabled();
            }
            case D4 -> {
                return newsChannelDTO.isD4Enabled();
            }
            case CORTEZ -> {
                return newsChannelDTO.isCortezEnabled();
            }
            case HEROES -> {
                return newsChannelDTO.isHeroesEnabled();
            }
            case WOW_WEB -> {
                return newsChannelDTO.isWowWebEnabled();
            }
            case NEWS -> {
                return newsChannelDTO.isNewsEnabled();
            }
            default -> throw new ToggleFailureException("Failed to toggle news setting. The category " + selectedNews +
                    " couldn't be found.");
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

    private void toggleSelectedNews(String selectedNews, NewsChannelDTO newsChannelDTO) throws ToggleFailureException {
        Categories cat = Categories.getCategoriesByRawName(selectedNews);
        if (cat == null)
            throw new ToggleFailureException("Failed to toggle news setting. The category was null.");
        switch (cat) {
            case DI -> {
                boolean currentValue = newsChannelDTO.isDiabloImmortalNewsEnabled();
                newsChannelDTO.setDiabloImmortalNewsEnabled(!currentValue);
            }
            case HEARTHSTONE -> {
                boolean currentValue = newsChannelDTO.isHearthStoneNewsEnabled();
                newsChannelDTO.setHearthStoneNewsEnabled(!currentValue);
            }
            case OVERWATCH -> {
                boolean currentValue = newsChannelDTO.isOverwatchEnabled();
                newsChannelDTO.setOverwatchEnabled(!currentValue);
            }
            case BNET -> {
                boolean currentValue = newsChannelDTO.isBnetEnabled();
                newsChannelDTO.setBnetEnabled(!currentValue);
            }
            case D4 -> {
                boolean currentValue = newsChannelDTO.isD4Enabled();
                newsChannelDTO.setD4Enabled(!currentValue);
            }
            case CORTEZ -> {
                boolean currentValue = newsChannelDTO.isCortezEnabled();
                newsChannelDTO.setCortezEnabled(!currentValue);
            }
            case HEROES -> {
                boolean currentValue = newsChannelDTO.isHeroesEnabled();
                newsChannelDTO.setHeroesEnabled(!currentValue);
            }
            case WOW_WEB -> {
                boolean currentValue = newsChannelDTO.isWowWebEnabled();
                newsChannelDTO.setWowWebEnabled(!currentValue);
            }
            case NEWS -> {
                boolean currentValue = newsChannelDTO.isNewsEnabled();
                newsChannelDTO.setNewsEnabled(!currentValue);
            }
            default -> throw new ToggleFailureException("Failed to toggle news setting. The category " + selectedNews +
                    " couldn't be found.");
        }
        databaseRequests.updateNewsChannel(newsChannelDTO);
    }
}
