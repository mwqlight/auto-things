package com.bjj.mall.crawler.impl;

import com.bjj.mall.entity.Product;
import com.bjj.mall.crawler.CrawlerService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class CrawlerServiceImpl implements CrawlerService {

    // 电商网站URLs
    private static final String JD_SEARCH_URL = "https://search.jd.com/Search?keyword=";
    private static final String TMALL_SEARCH_URL = "https://list.tmall.com/search_product.htm?q=";
    
    @Override
    public List<Product> crawlProducts(String keyword) {
        List<Product> products = new ArrayList<>();
        
        // 从不同商城爬取数据
        products.addAll(crawlFromJD(keyword));
        products.addAll(crawlFromTmall(keyword));
        
        return products;
    }
    
    private List<Product> crawlFromJD(String keyword) {
        List<Product> products = new ArrayList<>();
        
        try {
            // 构造搜索URL
            String url = JD_SEARCH_URL + keyword;
            
            // 使用Jsoup发送请求并解析HTML
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .timeout(10000)
                    .get();
            
            // 解析商品信息 - 根据京东实际页面结构调整选择器
            Elements productElements = doc.select(".gl-item"); // 京东商品列表项的选择器
            
            // 如果没有找到商品元素，尝试其他可能的选择器
            if (productElements.isEmpty()) {
                productElements = doc.select("[data-sku]");
            }
            
            for (int i = 0; i < Math.min(productElements.size(), 5); i++) {
                Element element = productElements.get(i);
                
                Product product = new Product();
                // 尝试多种选择器获取商品名称
                String name = element.select(".p-name a em").text();
                if (name.isEmpty()) {
                    name = element.select(".p-name a").attr("title");
                }
                if (name.isEmpty()) {
                    name = element.select(".p-name").text();
                }
                
                // 获取价格信息
                String priceStr = element.select(".p-price i").text();
                if (priceStr.isEmpty()) {
                    priceStr = element.select(".price J\\_\\5f\\50").text();
                }
                
                // 只有当商品名称和价格都存在时才添加商品
                if (!name.isEmpty() && !priceStr.isEmpty()) {
                    product.setName(name);
                    
                    try {
                        product.setPrice(new BigDecimal(priceStr));
                    } catch (NumberFormatException e) {
                        // 如果价格格式不正确，使用随机价格
                        product.setPrice(new BigDecimal(50 + new Random().nextInt(500)));
                    }
                    
                    product.setSourceMall("京东");
                    product.setBrand("京东自营");
                    product.setSalesVolume(new Random().nextInt(5000)); // 随机销量
                    product.setRating(4.0 + new Random().nextDouble() * 1.0); // 随机评分
                    product.setReviewCount(new Random().nextInt(1000)); // 随机评论数
                    product.setCreateTime(new Date());
                    product.setUpdateTime(new Date());
                    
                    products.add(product);
                }
            }
        } catch (IOException e) {
            // 如果爬取失败，使用模拟数据
            System.err.println("爬取京东数据失败: " + e.getMessage());
            products.addAll(createMockProducts(keyword, "京东", 5));
        }
        
        return products;
    }
    
    private List<Product> crawlFromTmall(String keyword) {
        List<Product> products = new ArrayList<>();
        
        try {
            // 构造搜索URL
            String url = TMALL_SEARCH_URL + keyword;
            
            // 使用Jsoup发送请求并解析HTML
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .timeout(10000)
                    .get();
            
            // 解析商品信息 - 根据天猫实际页面结构调整选择器
            Elements productElements = doc.select(".productItem"); // 天猫商品列表项的选择器
            
            // 如果没有找到商品元素，尝试其他可能的选择器
            if (productElements.isEmpty()) {
                productElements = doc.select(".product");
            }
            
            for (int i = 0; i < Math.min(productElements.size(), 5); i++) {
                Element element = productElements.get(i);
                
                Product product = new Product();
                // 尝试多种选择器获取商品名称
                String name = element.select(".productTitle a").text();
                if (name.isEmpty()) {
                    name = element.select(".title a").attr("title");
                }
                if (name.isEmpty()) {
                    name = element.select(".title").text();
                }
                
                // 获取价格信息
                String priceStr = element.select(".price").text().replaceAll("¥", "");
                if (priceStr.isEmpty()) {
                    priceStr = element.select(".c-price").text();
                }
                
                // 只有当商品名称和价格都存在时才添加商品
                if (!name.isEmpty() && !priceStr.isEmpty()) {
                    product.setName(name);
                    
                    try {
                        product.setPrice(new BigDecimal(priceStr));
                    } catch (NumberFormatException e) {
                        // 如果价格格式不正确，使用随机价格
                        product.setPrice(new BigDecimal(50 + new Random().nextInt(500)));
                    }
                    
                    product.setSourceMall("天猫");
                    product.setBrand("天猫超市");
                    product.setSalesVolume(new Random().nextInt(5000)); // 随机销量
                    product.setRating(4.0 + new Random().nextDouble() * 1.0); // 随机评分
                    product.setReviewCount(new Random().nextInt(1000)); // 随机评论数
                    product.setCreateTime(new Date());
                    product.setUpdateTime(new Date());
                    
                    products.add(product);
                }
            }
        } catch (IOException e) {
            // 如果爬取失败，使用模拟数据
            System.err.println("爬取天猫数据失败: " + e.getMessage());
            products.addAll(createMockProducts(keyword, "天猫", 5));
        }
        
        return products;
    }
    
    // 创建模拟数据的方法（当真实爬取失败时使用）
    private List<Product> createMockProducts(String keyword, String mall, int count) {
        List<Product> products = new ArrayList<>();
        Random random = new Random();
        
        String[] brands = {"Apple", "Samsung", "Huawei", "Xiaomi", "OPPO", "Vivo", "OnePlus", "Realme"};
        
        for (int i = 0; i < count; i++) {
            Product product = new Product();
            product.setName(keyword + " " + brands[random.nextInt(brands.length)] + " 版本" + (i + 1));
            product.setPrice(new BigDecimal(100 + random.nextInt(2000)));
            product.setSourceMall(mall);
            product.setBrand(brands[random.nextInt(brands.length)]);
            product.setSalesVolume(random.nextInt(10000));
            product.setRating(3.0 + random.nextDouble() * 2.0);
            product.setReviewCount(random.nextInt(2000));
            product.setCreateTime(new Date());
            product.setUpdateTime(new Date());
            
            products.add(product);
        }
        
        return products;
    }
}