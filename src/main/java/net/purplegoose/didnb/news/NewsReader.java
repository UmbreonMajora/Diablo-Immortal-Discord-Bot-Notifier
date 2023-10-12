package net.purplegoose.didnb.news;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.news.dto.ArticleDTO;
import net.purplegoose.didnb.news.dto.NewsChannelDTO;
import net.purplegoose.didnb.news.enums.Categories;
import net.purplegoose.didnb.utils.ChannelLogger;
import net.purplegoose.didnb.utils.TimeUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

import static net.purplegoose.didnb.utils.StringUtil.removeAllNonNumericCharacters;

@Slf4j
public class NewsReader {
    private static final String BASE_BLIZZARD_NEWS_PAGE = "https://news.blizzard.com";
    private static final String URL_LANGUAGE_PATH = "en-gn";
    private static final String RECENT_ARTICLES_ID = "recent-articles";

    private final Map<String, Integer> nullCounterMap = new HashMap<>();
    private final List<ArticleDTO> knownArticles = new ArrayList<>();

    private final DatabaseRequests databaseRequests;
    private final GuildsCache guildsCache;
    private final JDA diabloImmortalNotifier;
    private final ChannelLogger channelLogger;

    public NewsReader(DatabaseRequests databaseRequests, GuildsCache guildsCache, JDA diabloImmortalNotifier,
                      ChannelLogger channelLogger) {
        this.databaseRequests = databaseRequests;
        this.guildsCache = guildsCache;
        this.diabloImmortalNotifier = diabloImmortalNotifier;
        this.channelLogger = channelLogger;

        knownArticles.addAll(databaseRequests.getArticles());
    }

    public void runScheduler() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                read();
            }
        }, TimeUtil.getNextFullMinute(), 60L * 5000L);
    }

    private void read() {
        try {
            Document document = Jsoup.connect(BASE_BLIZZARD_NEWS_PAGE + "/" + URL_LANGUAGE_PATH).get();
            Element recentArticles = document.getElementById(RECENT_ARTICLES_ID);

            if (recentArticles == null) {
                throw new IOException("Failed to get recent articles.");
            }

            Elements articleList = recentArticles.select("li");

            for (Element element : articleList) {
                Elements anchorElement = element.select("a");
                String dataAnalyticsPlacement = anchorElement.attr("data-analytics-placement");
                int id = getBlogID(dataAnalyticsPlacement);
                String categoryRawName = getArticlesCategory(dataAnalyticsPlacement);
                Categories category = Categories.getCategoriesByRawName(categoryRawName);

                if (!isArticleValid(id, category, categoryRawName)) {
                    continue;
                }

                String url = BASE_BLIZZARD_NEWS_PAGE + "/" + anchorElement.attr("href");
                ArticleDTO article = new ArticleDTO(id, url, category.rawName);

                if (databaseRequests.insertNewArticle(article)) {
                    sendNewsNotification(category, url);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        log.info("Rode news from blizzard news page.");
    }

    private boolean isArticleValid(int id, Categories category, String categoryRawName) {
        if (id == 0) {
            log.error("Failed to get blog id.");
            return false;
        }

        if (isArticleKnown(id)) {

            return false;
        }

        if (category == null) {
            String logMessage = String.format("Failed to get category, might be a new one? (%s)", categoryRawName);
            channelLogger.sendChannelLog(logMessage);
            log.error(logMessage);
            return false;
        }

        return true;
    }

    private void sendNewsNotification(Categories category, String url) {
        for (ClientGuild clientGuild : guildsCache.getAllGuilds().values()) {
            for (NewsChannelDTO newsChannelDTO : clientGuild.getNewsChannels().values()) {
                if (isNewsEnabledInChannel(newsChannelDTO, category)) {
                    String channelID = newsChannelDTO.getChannelID();
                    TextChannel textChannel = diabloImmortalNotifier.getTextChannelById(channelID);
                    if (textChannel == null) {
                        handleNullChannel(channelID, clientGuild);
                        continue;
                    }
                    String message = "New post in category " + category.rawName + "!\n" + url;
                    textChannel.sendMessage(message).queue();
                }
            }
        }
    }

    private int getBlogID(String dataAnalyticsPlacement) {
        for (String string : dataAnalyticsPlacement.split(" ")) {
            if (string.contains("blog:")) {
                String s = removeAllNonNumericCharacters(string);
                return Integer.parseInt(s);
            }
        }
        return 0;
    }

    private boolean isArticleKnown(int id) {
        for (ArticleDTO knownArticle : knownArticles) {
            if (knownArticle.getId() == id) {
                return true;
            }
        }
        return false;
    }

    private void handleNullChannel(String channelID, ClientGuild clientGuild) {
        nullCounterMap.compute(channelID, (key, value) -> {
            if (value == null)
                return 1;
            if (value == 10) {
                databaseRequests.deleteNewsChannelByID(channelID);
                clientGuild.getNewsChannels().remove(channelID);
                return 0;
            }
            return value + 1;
        });
    }

    private String getArticlesCategory(String dataAnalyticsPlacement) {
        return dataAnalyticsPlacement.split(" ")[0];
    }

    private boolean isNewsEnabledInChannel(NewsChannelDTO newsChannel, Categories category) {
        switch (category) {
            case HEARTHSTONE -> {
                return newsChannel.isHearthStoneNewsEnabled();
            }
            case DI -> {
                return newsChannel.isDiabloImmortalNewsEnabled();
            }
            case OVERWATCH -> {
                return newsChannel.isOverwatchEnabled();
            }
            case BNET -> {
                return newsChannel.isBnetEnabled();
            }
            case D4 -> {
                return newsChannel.isD4Enabled();
            }
            case CORTEZ -> {
                return newsChannel.isCortezEnabled();
            }
            case HEROES -> {
                return newsChannel.isHeroesEnabled();
            }
            case WOW_WEB -> {
                return newsChannel.isWowWebEnabled();
            }
            case NEWS -> {
                return newsChannel.isNewsEnabled();
            }
            default -> {
                log.error("isNewsEnabledInChannel(NewsChannelDTO, Categories) was called with an unknown category {}.",
                        category);
                return false;
            }
        }
    }
}