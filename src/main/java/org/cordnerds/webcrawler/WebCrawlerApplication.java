package org.cordnerds.webcrawler;

import org.cordnerds.webcrawler.executor.WebCrawlerExecutor;
import org.cordnerds.webcrawler.job.WebCrawlerJob;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@EnableConfigurationProperties
@SpringBootApplication
public class WebCrawlerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(WebCrawlerApplication.class, args);
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public WebCrawlerJob job(WebCrawlerExecutor executor, ApplicationContext context)  {
        return new WebCrawlerJob(executor, context);
    }

}
