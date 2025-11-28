package com.bjj.mall.config;

import com.bjj.mall.crawler.CrawlerService;
import com.bjj.mall.crawler.impl.AdvancedCrawlerServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CrawlerConfig {

    @Bean
    public CrawlerService crawlerService() {
        return new AdvancedCrawlerServiceImpl();
    }
}