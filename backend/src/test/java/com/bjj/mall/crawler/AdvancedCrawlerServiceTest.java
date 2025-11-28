package com.bjj.mall.crawler;

import com.bjj.mall.MallApplication;
import com.bjj.mall.crawler.impl.AdvancedCrawlerServiceImpl;
import com.bjj.mall.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MallApplication.class)
public class AdvancedCrawlerServiceTest {

    @Test
    public void testCrawlProducts() {
        AdvancedCrawlerServiceImpl crawlerService = new AdvancedCrawlerServiceImpl();
        
        // 测试爬取"手机"关键字的商品
        List<Product> products = crawlerService.crawlProducts("手机");
        
        // 验证返回结果不为空
        assertNotNull(products, "爬取结果不应为null");
        assertFalse(products.isEmpty(), "应返回至少一个商品");
        
        // 验证每个商品的基本属性
        for (Product product : products) {
            assertNotNull(product.getName(), "商品名称不应为null");
            assertFalse(product.getName().trim().isEmpty(), "商品名称不应为空");
            assertNotNull(product.getPrice(), "商品价格不应为null");
            assertTrue(product.getPrice().compareTo(java.math.BigDecimal.ZERO) > 0, "商品价格应大于0");
            assertNotNull(product.getSourceMall(), "商品来源不应为null");
            assertTrue(product.getSalesVolume() >= 0, "销量不应为负数");
            assertTrue(product.getRating() >= 0 && product.getRating() <= 5, "评分应在0-5之间");
            assertTrue(product.getReviewCount() >= 0, "评论数不应为负数");
        }
        
        System.out.println("总共爬取到 " + products.size() + " 个商品");
        for (int i = 0; i < Math.min(5, products.size()); i++) {
            Product product = products.get(i);
            System.out.println("商品名称: " + product.getName());
            System.out.println("价格: " + product.getPrice());
            System.out.println("来源: " + product.getSourceMall());
            System.out.println("销量: " + product.getSalesVolume());
            System.out.println("评分: " + product.getRating());
            System.out.println("评论数: " + product.getReviewCount());
            System.out.println("---");
        }
    }
    
    @Test
    public void testDataCleaning() {
        AdvancedCrawlerServiceImpl crawlerService = new AdvancedCrawlerServiceImpl();
        
        // 创建一些有质量问题的测试数据
        com.bjj.mall.entity.Product product1 = new com.bjj.mall.entity.Product();
        product1.setName("  测试手机  "); // 包含多余空格
        
        com.bjj.mall.entity.Product product2 = new com.bjj.mall.entity.Product();
        product2.setName("iPhone\n13\tPro"); // 包含换行符和制表符
        
        com.bjj.mall.entity.Product product3 = new com.bjj.mall.entity.Product();
        product3.setName(""); // 空名称
        
        com.bjj.mall.entity.Product product4 = new com.bjj.mall.entity.Product();
        product4.setName(null); // null名称
        
        java.util.List<com.bjj.mall.entity.Product> rawProducts = 
            java.util.Arrays.asList(product1, product2, product3, product4);
        
        // 调用数据清洗方法
        java.lang.reflect.Method cleanMethod = null;
        try {
            cleanMethod = AdvancedCrawlerServiceImpl.class.getDeclaredMethod(
                "cleanAndStandardizeProducts", java.util.List.class);
            cleanMethod.setAccessible(true);
            List<Product> cleanedProducts = (List<Product>) cleanMethod.invoke(crawlerService, rawProducts);
            
            // 验证清洗结果
            assertEquals(2, cleanedProducts.size(), "应只保留2个有效商品");
            
            Product cleanedProduct1 = cleanedProducts.get(0);
            assertEquals("测试手机", cleanedProduct1.getName(), "应去除首尾空格");
            
            Product cleanedProduct2 = cleanedProducts.get(1);
            assertEquals("iPhone 13 Pro", cleanedProduct2.getName(), "应去除换行符和制表符");
            
        } catch (Exception e) {
            fail("数据清洗方法调用失败: " + e.getMessage());
        }
    }
}