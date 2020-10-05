package org.cordnerds.webcrawler.io.writer;

import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author Amila Karunathilaka
 */
// todo need to change sout -> log
@Component
public class ConsoleWriter implements ResultWriter{

    @Override
    public void write(String searchWord, String startUrl, Set<String> resultUrl) {
        System.out.println("Start URL - " + startUrl);
        System.out.println("Search word - " + searchWord);

        if (resultUrl.size() > 0) {
            System.out.println("URL list");
            resultUrl.forEach(url -> System.out.println(url));
        } else {
            System.out.println("Urls are not found");
        }
    }
}
