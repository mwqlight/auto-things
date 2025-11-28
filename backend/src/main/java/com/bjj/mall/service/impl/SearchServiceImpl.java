package com.bjj.mall.service.impl;

import com.bjj.mall.entity.Product;
import com.bjj.mall.entity.SearchRecord;
import com.bjj.mall.crawler.CrawlerService;
import com.bjj.mall.ai.AiRecommendationService;
import com.bjj.mall.service.SearchService;
import com.bjj.mall.service.SearchRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private CrawlerService crawlerService;

    @Autowired
    private AiRecommendationService aiRecommendationService;

    @Autowired
    private SearchRecordService searchRecordService;

    @Override
    public List<Product> searchAndCrawlProducts(String keyword) {
        // 从网络爬取商品信息
        return crawlerService.crawlProducts(keyword);
    }

    @Override
    public String getAiRecommendation(List<Product> products) {
        // 获取AI推荐
        return aiRecommendationService.generateRecommendation(products);
    }

    @Override
    public void saveSearchRecord(String keyword, String ipAddress) {
        // 保存搜索记录
        SearchRecord record = new SearchRecord();
        record.setKeyword(keyword);
        record.setIpAddress(ipAddress);
        record.setSearchTime(new Date());
        searchRecordService.saveSearchRecord(record);
    }
}