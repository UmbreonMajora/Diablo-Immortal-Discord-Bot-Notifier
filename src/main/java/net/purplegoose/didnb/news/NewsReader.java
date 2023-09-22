package net.purplegoose.didnb.news;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import static net.purplegoose.didnb.utils.StringUtil.removeAllNonNumericCharacters;

public class NewsReader {

    private static final String BASE_BLIZZARD_NEWS_PAGE = "https://news.blizzard.com";
    private static final String URL_LANGUAGE_PATH = "en-gn";
    private static final String RECENT_ARTICLES_ID = "recent-articles";

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
                String id = getBlogID(dataAnalyticsPlacement);
                if (id == null) {
                    throw new IOException("Failed to get blog id.");
                }
                String url = BASE_BLIZZARD_NEWS_PAGE + "/" + anchorElement.attr("href");
                String category = getArticlesCategory(dataAnalyticsPlacement);
                ArticleDTO article = new ArticleDTO(id, url, category);
            }

        } catch (IOException e) {
            //todo handle error
            e.printStackTrace();
        }
    }

    private String getBlogID(String dataAnalyticsPlacement) {
        for (String string : dataAnalyticsPlacement.split(" ")) {
            if (string.contains("blog:")) {
                return removeAllNonNumericCharacters(string);
            }
        }
        return null;
    }

    private String getArticlesCategory(String dataAnalyticsPlacement) {
        return dataAnalyticsPlacement.split(" ")[0];
    }
}