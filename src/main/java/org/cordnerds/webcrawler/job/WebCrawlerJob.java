package org.cordnerds.webcrawler.job;

import org.apache.commons.validator.routines.UrlValidator;
import org.cordnerds.webcrawler.executor.WebCrawlerExecutor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Amila Karunathilaka
 */

public class WebCrawlerJob {

    private final static String WEB_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private final static Set<String> VISITED_URLS_SET = Collections.synchronizedSet(new HashSet<>());
    private final static Set<String> RESULT_URL_SET = Collections.synchronizedSet(new HashSet<>());

    @Value("${web.crawler.max.page:10}")
    private int maxPages;

    @Value("${web.crawler.connection.timeout:1000}")
    private int connTimeout;

    private WebCrawlerExecutor webCrawlerExecutor;
    private ApplicationContext context;

    @Autowired
    public WebCrawlerJob(WebCrawlerExecutor webCrawlerExecutor, ApplicationContext context) {
        this.webCrawlerExecutor = webCrawlerExecutor;
        this.context = context;
    }

    public void search(String searchWord, String url) throws IOException {
        synchronized (VISITED_URLS_SET) {
            if (VISITED_URLS_SET.size() >= maxPages || VISITED_URLS_SET.contains(url)) {
                return;
            }
            VISITED_URLS_SET.add(url);
        }
        Optional<Document> documentOpt = this.crawl(url, searchWord);

        if (documentOpt.isPresent()) {
            boolean success = searchToWord(documentOpt.get(), searchWord);
            if (success) {
                RESULT_URL_SET.add(url);
            }
        }
    }

    private Optional<Document> crawl(String url, String searchWord) throws IOException {
        Connection conn = Jsoup.connect(url).timeout(connTimeout).userAgent(WEB_AGENT);
        Document document = conn.get();
        if (conn.response().statusCode() != 200 && conn.response().contentType().contains("text/html")) {
            return Optional.empty();
        }


        document.select("a")
                .parallelStream()
                .map(element -> element.attr("href"))
                .map(nextUrl -> nextUrl.split("#")[0])
                .map(nextUrl -> nextUrl.split("\\?")[0])
                .distinct().filter(nextUrl -> UrlValidator.getInstance().isValid(nextUrl))
                .forEach(nextUrl -> {
                    WebCrawlerJob crawlerJob = context.getBean(WebCrawlerJob.class);
                    this.webCrawlerExecutor.addTask(crawlerJob, searchWord, nextUrl);
                });

        return Optional.ofNullable(document);
    }

    private boolean searchToWord(Document document, String word) {
        String bodyText = document.body().text();
        return bodyText.toLowerCase().contains(word.toLowerCase());
    }

    public static Set<String> getResultUrls() {
        return RESULT_URL_SET;
    }

    public void setMaxPages(int maxPages) {
        this.maxPages = maxPages;
    }

    public void setConnTimeout(int connTimeout) {
        this.connTimeout = connTimeout;
    }
}
