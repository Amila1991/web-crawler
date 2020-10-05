package org.cordnerds.webcrawler.executor.starter;

import org.cordnerds.webcrawler.config.CrawlerInput;
import org.cordnerds.webcrawler.executor.WebCrawlerExecutor;
import org.cordnerds.webcrawler.io.writer.ResultWriter;
import org.cordnerds.webcrawler.job.WebCrawlerJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Amila Karunathilaka
 */
@Component
public class WebCrawlerStarter {

    private WebCrawlerExecutor executor;
    private WebCrawlerJob initialJob;
    private CrawlerInput input;
    private List<ResultWriter> resultWriterList;

    @Autowired
    public WebCrawlerStarter(WebCrawlerExecutor executor, WebCrawlerJob initialJob, CrawlerInput input,
                             List<ResultWriter> resultWriterList) {
        this.executor = executor;
        this.initialJob = initialJob;
        this.input = input;
        this.resultWriterList = resultWriterList;

        try {
            start();
        } catch (InterruptedException e) {
            //todo log
        }
    }

    public void start() throws InterruptedException {
        executor.submitTask(initialJob, input.getSearchWord(), input.getStartUrl());
        executor.resultPrintAndShutdown(resultWriterList, input.getSearchWord(), input.getStartUrl());
    }


}
