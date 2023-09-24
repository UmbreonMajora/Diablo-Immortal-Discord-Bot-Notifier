package net.purplegoose.didnb.news;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.news.dto.ArticleDTO;
import net.purplegoose.didnb.news.enums.Categories;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import static net.purplegoose.didnb.utils.StringUtil.removeAllNonNumericCharacters;

@AllArgsConstructor
public class NewsReader {
    private static final String BASE_BLIZZARD_NEWS_PAGE = "https://news.blizzard.com";
    private static final String URL_LANGUAGE_PATH = "en-gn";
    private static final String RECENT_ARTICLES_ID = "recent-articles";

    private final DatabaseRequests databaseRequests;
    private final GuildsCache guildsCache;
    private final JDA diabloImmortalNotifier;

    public void read() {
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
                String url = BASE_BLIZZARD_NEWS_PAGE + "/" + anchorElement.attr("href");
                String categoryRawName = getArticlesCategory(dataAnalyticsPlacement);
                Categories category = Categories.getCategoriesByRawName(categoryRawName);
                if (category == null) {
                    throw new IOException("Failed to get category, might be a new one? (" + categoryRawName + ")");
                }
                ArticleDTO article = new ArticleDTO(id, url, category.rawName);
                databaseRequests.insertNewArticle(article);
                guildsCache.getAllGuilds().forEach((guildID, clientGuild) -> {

                });
            }
        } catch (IOException e) {
            //todo handle error
            e.printStackTrace();
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

    private String getArticlesCategory(String dataAnalyticsPlacement) {
        return dataAnalyticsPlacement.split(" ")[0];
    }
}