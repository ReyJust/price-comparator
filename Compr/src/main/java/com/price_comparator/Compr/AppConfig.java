package com.price_comparator.Compr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
//@EnableAsync
@ComponentScan
public class AppConfig {

    @Bean
    public Hibernate hibernate() {
        return new Hibernate();
    }

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

    @Bean
    @Scope("singleton")
    public AmazonScrapper amazonScrapper() {
        return new AmazonScrapper();
    }

    @Bean
    @Scope("singleton")
    public NewEggScrapper newEggScrapper() {
        return new NewEggScrapper();
    }

    @Bean
    @Scope("singleton")
    public ArgosScrapper argosScrapper() {
        return new ArgosScrapper();
    }

    @Bean
    @Scope("singleton")
    public BoxScrapper boxScrapper() {
        return new BoxScrapper();
    }

    @Bean
    @Scope("singleton")
    public FlipKartScrapper flipKartScrapper() {
        return new FlipKartScrapper();
    }
}
