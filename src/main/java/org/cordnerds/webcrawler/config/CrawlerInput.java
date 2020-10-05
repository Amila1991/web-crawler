package org.cordnerds.webcrawler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Amila Karunathilaka
 */
@Configuration
public class CrawlerInput {

    @Value("${web.crawler.start.url}")
    private String startUrl;

    @Value("${web.crawler.search.word}")
    private String searchWord;

    public String getStartUrl() {
        return startUrl;
    }

    public void setStartUrl(String startUrl) {
        this.startUrl = startUrl;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }
}
