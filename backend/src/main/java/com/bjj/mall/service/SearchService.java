package com.bjj.mall.service;

import com.bjj.mall.entity.Product;
import com.bjj.mall.entity.SearchRecord;
import java.util.List;

public interface SearchService {
    List<Product> searchAndCrawlProducts(String keyword);
    String getAiRecommendation(List<Product> products);
    void saveSearchRecord(String keyword, String ipAddress);
}