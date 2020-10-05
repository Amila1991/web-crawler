package org.cordnerds.webcrawler.io.writer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Set;

/**
 * @author Amila Karunathilaka
 */
@Component
public class FileWriter implements ResultWriter {

    @Value("${web.crawler.output.file.name: outputUrls.txt}")
    private String fileName;

    @Override
    public void write(String searchWord, String startUrl, Set<String> resultUrl) {
        try {
            PrintWriter printWriter = new PrintWriter(new java.io.FileWriter(fileName));
            printWriter.println("Start URL - " + startUrl);
            printWriter.println("Search word - " + searchWord);

            if (resultUrl.size() > 0) {
                printWriter.println("URL list");
                resultUrl.forEach(url -> printWriter.println(url));
            } else {
                printWriter.println("Urls are not found");
            }
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
