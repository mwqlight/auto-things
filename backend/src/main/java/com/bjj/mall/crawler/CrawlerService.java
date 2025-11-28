package com.bjj.mall.crawler;

import com.bjj.mall.entity.Product;
import java.util.List;

public interface CrawlerService {
    List<Product> crawlProducts(String keyword);
}