package com.price_comparator.Compr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * The app config delcaring the spring beans
 */
@Configuration
@EnableAsync
@ComponentScan
public class AppConfig {
    /**
     * 
     * @return hibernate the hibernate session
     */
    @Bean
    public Hibernate hibernate() {
        return new Hibernate();
    }

    /**
     * 
     * @return executor the thread executor
     */
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("poolThread-");
        executor.initialize();

        return executor;
    }

    /**
     * Actual Bean for running the Amazon Scrapper.
     * 
     * @return AmazonScrapper
     */
    @Bean
    @Scope("singleton")
    public AmazonScrapper amazonScrapper() {
        return new AmazonScrapper();
    }

    /**
     * Actual Bean for running the NewEgg Scrapper.
     * 
     * @return NewEggScrapper
     */
    @Bean
    @Scope("singleton")
    public NewEggScrapper newEggScrapper() {
        return new NewEggScrapper();
    }

    /**
     * Actual Bean for running the Argos Scrapper.
     * 
     * @return ArgosScrapper
     */
    @Bean
    @Scope("singleton")
    public ArgosScrapper argosScrapper() {
        return new ArgosScrapper();
    }

    /**
     * Actual Bean for running the Box Scrapper.
     * 
     * @return BoxScrapper
     */
    @Bean
    @Scope("singleton")
    public BoxScrapper boxScrapper() {
        return new BoxScrapper();
    }

    /**
     * Actual Bean for running the Flipkart Scrapper.
     * 
     * @return FlipKartScrapper
     */
    @Bean
    @Scope("singleton")
    public FlipKartScrapper flipKartScrapper() {
        return new FlipKartScrapper();
    }
}
