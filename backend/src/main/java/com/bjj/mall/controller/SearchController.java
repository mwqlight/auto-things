package com.bjj.mall.controller;

import com.bjj.mall.entity.Product;
import com.bjj.mall.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "*")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/products")
    public ResponseEntity<SearchResult> searchProducts(
            @RequestParam String keyword,
            HttpServletRequest request) {
        
        // 保存搜索记录
        String ipAddress = getClientIpAddress(request);
        searchService.saveSearchRecord(keyword, ipAddress);
        
        // 搜索并爬取商品信息
        List<Product> products = searchService.searchAndCrawlProducts(keyword);
        
        // 获取AI推荐
        String recommendation = searchService.getAiRecommendation(products);
        
        SearchResult result = new SearchResult();
        result.setProducts(products);
        result.setRecommendation(recommendation);
        
        return ResponseEntity.ok(result);
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
    
    // 搜索结果封装类
    public static class SearchResult {
        private List<Product> products;
        private String recommendation;
        
        public List<Product> getProducts() {
            return products;
        }
        
        public void setProducts(List<Product> products) {
            this.products = products;
        }
        
        public String getRecommendation() {
            return recommendation;
        }
        
        public void setRecommendation(String recommendation) {
            this.recommendation = recommendation;
        }
    }
}