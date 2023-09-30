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

    public NewsReader(DatabaseRequests databaseRequests, GuildsCache guildsCache, JDA diabloImmortalNotifier) {
        this.databaseRequests = databaseRequests;
        this.guildsCache = guildsCache;
        this.diabloImmortalNotifier = diabloImmortalNotifier;

        knownArticles.addAll(databaseRequests.getArticles());
    }

    public void runScheduler() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //read();
            }
        }, TimeUtil.getNextFullMinute(), 60L * 15000L);
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

                if (id == 0) {
                    throw new IOException("Failed to get blog id.");
                }

                if (isArticleKnown(id)) {
                    continue;
                }

                String url = BASE_BLIZZARD_NEWS_PAGE + "/" + anchorElement.attr("href");
                String categoryRawName = getArticlesCategory(dataAnalyticsPlacement);
                Categories category = Categories.getCategoriesByRawName(categoryRawName);

                if (category == null) {
                    throw new IOException("Failed to get category, might be a new one? (" + categoryRawName + ")");
                }

                ArticleDTO article = new ArticleDTO(id, url, category.rawName);
                if (databaseRequests.insertNewArticle(article)) {
                    sendNewsNotification(category, url);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
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
                    String message = String.format("New post in category %s!\n%s", category.rawName, url);
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
            case HEARTSTONE -> {
                return newsChannel.isHearthStoneNewsEnabled();
            }
            case DI -> {
                return newsChannel.isDiabloImmortalNewsEnabled();
            }
            default -> {
                log.error("isNewsEnabledInChannel(NewsChannelDTO, Categories) was called with an unknown category {}.",
                        category);
                return false;
            }
        }
    }
}