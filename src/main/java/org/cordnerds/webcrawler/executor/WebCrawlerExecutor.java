package org.cordnerds.webcrawler.executor;

import org.cordnerds.webcrawler.io.writer.ResultWriter;
import org.cordnerds.webcrawler.job.WebCrawlerJob;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author Amila Karunathilaka
 */
@Component
public class WebCrawlerExecutor {

    private ExecutorCompletionService<Boolean> executorCompletionService;
    private ExecutorService executorService;

    public WebCrawlerExecutor() {
        this.executorService = Executors.newFixedThreadPool(10);
        this.executorCompletionService = new ExecutorCompletionService<>(executorService);
    }

    public void submitTask(WebCrawlerJob job, String searchWord, String url) {
         executorCompletionService.submit(() -> {
             job.search(searchWord, url);
             return true;
         });
    }

    public void resultPrintAndShutdown(List<ResultWriter> writers, String searchWord, String url)
            throws InterruptedException {
        int stop = 0;
        Thread.sleep(2000);
        while (true) {
            if (executorCompletionService.poll() == null && stop >= 2) {
                writers.forEach(writer -> writer.write(searchWord, url, WebCrawlerJob.getResultUrls()));
                executorService.shutdown();
                break;
            } else if (executorCompletionService.poll() == null) {
                stop++;
                Thread.sleep(2000);
            } else {
                stop = 0;
            }
        }
    }
}
