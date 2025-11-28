package com.bjj.mall.crawler.impl;

import com.bjj.mall.entity.Product;
import com.bjj.mall.crawler.CrawlerService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AdvancedCrawlerServiceImpl implements CrawlerService {

    // 模拟电商网站URLs
    private static final String JD_SEARCH_URL = "https://search.jd.com/Search?keyword=";
    private static final String TMALL_SEARCH_URL = "https://list.tmall.com/search_product.htm?q=";
    
    // Cookie文件路径
    private static final String JD_COOKIE_FILE = "jd_cookies.txt";
    private static final String TMALL_COOKIE_FILE = "tmall_cookies.txt";
    
    // 代理IP配置文件路径
    private static final String PROXY_CONFIG_FILE = "proxy_config.txt";
    
    // 请求间隔时间（毫秒）
    private static final long REQUEST_DELAY = 2000;
    
    @Override
    public List<Product> crawlProducts(String keyword) {
        List<Product> products = new ArrayList<>();
        
        // 从不同商城爬取数据
        products.addAll(crawlFromJD(keyword));
        products.addAll(crawlFromTmall(keyword));
        
        // 数据清洗和标准化处理
        return cleanAndStandardizeProducts(products);
    }
    
    private List<Product> crawlFromJD(String keyword) {
        List<Product> products = new ArrayList<>();
        
        // 使用Selenium WebDriver处理需要JavaScript渲染的页面
        WebDriver driver = null;
        try {
            // 设置ChromeDriver路径（需要根据实际环境调整）
            // System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
            
            // 配置Chrome选项
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless"); // 无头模式
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.setExperimentalOption("useAutomationExtension", false);
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            
            // 添加代理IP配置
            configureProxy(options);
            
            // 创建WebDriver实例
            driver = new ChromeDriver(options);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            
            // 尝试加载已保存的Cookie
            loadCookies(driver, JD_COOKIE_FILE);
            
            // 访问京东搜索页面
            String url = JD_SEARCH_URL + keyword;
            driver.get(url);
            
            // 等待页面加载完成
            Thread.sleep(3000);
            
            // 检查是否需要登录
            if (isLoginRequired(driver, "京东")) {
                // 执行登录流程
                performJDLogin(driver);
                // 保存Cookie以便下次使用
                saveCookies(driver, JD_COOKIE_FILE);
            }
            
            // 解析商品信息
            List<WebElement> productElements = driver.findElements(By.cssSelector(".gl-item"));
            
            for (int i = 0; i < Math.min(productElements.size(), 5); i++) {
                WebElement element = productElements.get(i);
                
                Product product = new Product();
                try {
                    // 获取商品名称
                    WebElement nameElement = element.findElement(By.cssSelector(".p-name a em"));
                    product.setName(nameElement.getText());
                    
                    // 获取商品价格
                    WebElement priceElement = element.findElement(By.cssSelector(".p-price i"));
                    product.setPrice(new BigDecimal(priceElement.getText()));
                    
                    product.setSourceMall("京东");
                    product.setBrand("京东自营");
                    product.setSalesVolume(new Random().nextInt(5000)); // 随机销量
                    product.setRating(4.0 + new Random().nextDouble() * 1.0); // 随机评分
                    product.setReviewCount(new Random().nextInt(1000)); // 随机评论数
                    
                    products.add(product);
                } catch (Exception e) {
                    // 如果解析失败，跳过该商品
                    System.err.println("解析京东商品信息失败: " + e.getMessage());
                }
            }
            
            // 控制请求频率
            Thread.sleep(REQUEST_DELAY);
        } catch (Exception e) {
            // 如果爬取失败，使用模拟数据
            System.err.println("爬取京东数据失败: " + e.getMessage());
            products.addAll(createMockProducts(keyword, "京东", 5));
        } finally {
            // 关闭WebDriver
            if (driver != null) {
                driver.quit();
            }
        }
        
        return products;
    }
    
    private List<Product> crawlFromTmall(String keyword) {
        List<Product> products = new ArrayList<>();
        
        // 使用Selenium WebDriver处理需要JavaScript渲染的页面
        WebDriver driver = null;
        try {
            // 设置ChromeDriver路径（需要根据实际环境调整）
            // System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
            
            // 配置Chrome选项
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless"); // 无头模式
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.setExperimentalOption("useAutomationExtension", false);
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            
            // 添加代理IP配置
            configureProxy(options);
            
            // 创建WebDriver实例
            driver = new ChromeDriver(options);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            
            // 尝试加载已保存的Cookie
            loadCookies(driver, TMALL_COOKIE_FILE);
            
            // 访问天猫搜索页面
            String url = TMALL_SEARCH_URL + keyword;
            driver.get(url);
            
            // 等待页面加载完成
            Thread.sleep(3000);
            
            // 检查是否需要登录
            if (isLoginRequired(driver, "天猫")) {
                // 执行登录流程
                performTmallLogin(driver);
                // 保存Cookie以便下次使用
                saveCookies(driver, TMALL_COOKIE_FILE);
            }
            
            // 解析商品信息
            List<WebElement> productElements = driver.findElements(By.cssSelector(".product"));
            
            for (int i = 0; i < Math.min(productElements.size(), 5); i++) {
                WebElement element = productElements.get(i);
                
                Product product = new Product();
                try {
                    // 获取商品名称
                    WebElement nameElement = element.findElement(By.cssSelector(".productTitle a"));
                    product.setName(nameElement.getText());
                    
                    // 获取商品价格
                    WebElement priceElement = element.findElement(By.cssSelector(".price"));
                    String priceText = priceElement.getText().replaceAll("¥", "").trim();
                    product.setPrice(new BigDecimal(priceText));
                    
                    product.setSourceMall("天猫");
                    product.setBrand("天猫超市");
                    product.setSalesVolume(new Random().nextInt(5000)); // 随机销量
                    product.setRating(4.0 + new Random().nextDouble() * 1.0); // 随机评分
                    product.setReviewCount(new Random().nextInt(1000)); // 随机评论数
                    
                    products.add(product);
                } catch (Exception e) {
                    // 如果解析失败，跳过该商品
                    System.err.println("解析天猫商品信息失败: " + e.getMessage());
                }
            }
            
            // 控制请求频率
            Thread.sleep(REQUEST_DELAY);
        } catch (Exception e) {
            // 如果爬取失败，使用模拟数据
            System.err.println("爬取天猫数据失败: " + e.getMessage());
            products.addAll(createMockProducts(keyword, "天猫", 5));
        } finally {
            // 关闭WebDriver
            if (driver != null) {
                driver.quit();
            }
        }
        
        return products;
    }
    
    /**
     * 数据清洗和标准化处理
     */
    private List<Product> cleanAndStandardizeProducts(List<Product> rawProducts) {
        List<Product> cleanedProducts = new ArrayList<>();
        
        for (Product product : rawProducts) {
            // 创建新的产品对象用于存储清洗后的数据
            Product cleanedProduct = new Product();
            
            // 清洗和标准化产品名称
            if (product.getName() != null) {
                // 去除首尾空格
                String cleanedName = product.getName().trim();
                // 去除特殊字符和多余空格
                cleanedName = cleanedName.replaceAll("[\\t\\n\\r]+", " ");
                cleanedName = cleanedName.replaceAll("\\s+", " ");
                cleanedProduct.setName(cleanedName);
            }
            
            // 标准化价格
            if (product.getPrice() != null) {
                // 确保价格为正数
                if (product.getPrice().compareTo(BigDecimal.ZERO) > 0) {
                    cleanedProduct.setPrice(product.getPrice());
                } else {
                    // 如果价格无效，设置为默认值
                    cleanedProduct.setPrice(new BigDecimal("0.01"));
                }
            } else {
                // 如果价格为空，设置为默认值
                cleanedProduct.setPrice(new BigDecimal("0.01"));
            }
            
            // 标准化来源商城
            if (product.getSourceMall() != null) {
                cleanedProduct.setSourceMall(product.getSourceMall().trim());
            }
            
            // 标准化品牌
            if (product.getBrand() != null) {
                cleanedProduct.setBrand(product.getBrand().trim());
            }
            
            // 标准化销量（确保非负）
            if (product.getSalesVolume() != null && product.getSalesVolume() >= 0) {
                cleanedProduct.setSalesVolume(product.getSalesVolume());
            } else {
                cleanedProduct.setSalesVolume(0);
            }
            
            // 标准化评分（确保在0-5范围内）
            if (product.getRating() != null) {
                double rating = product.getRating();
                if (rating < 0) {
                    rating = 0;
                } else if (rating > 5) {
                    rating = 5;
                }
                cleanedProduct.setRating(rating);
            } else {
                cleanedProduct.setRating(0.0);
            }
            
            // 标准化评论数（确保非负）
            if (product.getReviewCount() != null && product.getReviewCount() >= 0) {
                cleanedProduct.setReviewCount(product.getReviewCount());
            } else {
                cleanedProduct.setReviewCount(0);
            }
            
            // 只添加名称不为空的产品
            if (cleanedProduct.getName() != null && !cleanedProduct.getName().isEmpty()) {
                cleanedProducts.add(cleanedProduct);
            }
        }
        
        return cleanedProducts;
    }
    
    /**
     * 配置代理IP
     */
    private void configureProxy(ChromeOptions options) {
        try {
            File file = new File(PROXY_CONFIG_FILE);
            if (!file.exists()) {
                System.out.println("代理配置文件不存在: " + PROXY_CONFIG_FILE);
                return;
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String proxyLine = reader.readLine();
            reader.close();
            
            if (proxyLine != null && !proxyLine.trim().isEmpty()) {
                options.addArguments("--proxy-server=http://" + proxyLine.trim());
                System.out.println("已配置代理IP: " + proxyLine.trim());
            }
        } catch (Exception e) {
            System.err.println("配置代理IP失败: " + e.getMessage());
        }
    }
    
    /**
     * 检查是否需要登录
     */
    private boolean isLoginRequired(WebDriver driver, String platform) {
        try {
            // 根据平台检查登录状态
            if ("京东".equals(platform)) {
                // 检查页面是否包含登录相关元素
                driver.findElement(By.linkText("登录"));
                return true;
            } else if ("天猫".equals(platform)) {
                // 检查页面是否包含登录相关元素
                driver.findElement(By.linkText("请登录"));
                return true;
            }
        } catch (NoSuchElementException e) {
            // 如果找不到登录元素，说明已经登录
            return false;
        }
        return false;
    }
    
    /**
     * 执行京东登录流程
     */
    private void performJDLogin(WebDriver driver) {
        try {
            // 点击登录按钮
            WebElement loginButton = driver.findElement(By.linkText("登录"));
            loginButton.click();
            
            // 等待登录页面加载
            Thread.sleep(2000);
            
            // 这里应该实现实际的登录逻辑
            // 由于安全原因，我们不会在代码中硬编码用户名和密码
            // 实际应用中，应该从配置文件或环境变量中读取
            System.out.println("请手动完成京东登录流程，或在配置文件中提供登录凭证");
            
            // 等待用户手动登录
            Thread.sleep(10000);
        } catch (Exception e) {
            System.err.println("京东登录失败: " + e.getMessage());
        }
    }
    
    /**
     * 执行天猫登录流程
     */
    private void performTmallLogin(WebDriver driver) {
        try {
            // 点击登录按钮
            WebElement loginButton = driver.findElement(By.linkText("请登录"));
            loginButton.click();
            
            // 等待登录页面加载
            Thread.sleep(2000);
            
            // 这里应该实现实际的登录逻辑
            // 由于安全原因，我们不会在代码中硬编码用户名和密码
            // 实际应用中，应该从配置文件或环境变量中读取
            System.out.println("请手动完成天猫登录流程，或在配置文件中提供登录凭证");
            
            // 等待用户手动登录
            Thread.sleep(10000);
        } catch (Exception e) {
            System.err.println("天猫登录失败: " + e.getMessage());
        }
    }
    
    /**
     * 保存Cookie到文件
     */
    private void saveCookies(WebDriver driver, String filename) {
        try {
            File file = new File(filename);
            FileWriter fileWriter = new FileWriter(file);
            
            Set<Cookie> cookies = driver.manage().getCookies();
            for (Cookie cookie : cookies) {
                fileWriter.write(cookie.getName() + ";" + cookie.getValue() + ";" + 
                                cookie.getDomain() + ";" + cookie.getPath() + ";" + 
                                cookie.getExpiry() + ";" + cookie.isSecure() + "\n");
            }
            
            fileWriter.close();
            System.out.println("Cookies已保存到 " + filename);
        } catch (IOException e) {
            System.err.println("保存Cookie失败: " + e.getMessage());
        }
    }
    
    /**
     * 从文件加载Cookie
     */
    private void loadCookies(WebDriver driver, String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("Cookie文件不存在: " + filename);
                return;
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 6) {
                    String name = parts[0];
                    String value = parts[1];
                    String domain = parts[2];
                    String path = parts[3];
                    // 注意：这里简化处理，实际应用中需要正确解析过期时间和安全标志
                    Cookie cookie = new Cookie(name, value, domain, path, null);
                    driver.manage().addCookie(cookie);
                }
            }
            reader.close();
            System.out.println("从 " + filename + " 成功加载Cookie");
        } catch (Exception e) {
            System.err.println("加载Cookie失败: " + e.getMessage());
        }
    }
    
    // 创建模拟数据的方法（当真实爬取失败时使用）
    private List<Product> createMockProducts(String keyword, String mall, int count) {
        List<Product> products = new ArrayList<>();
        Random random = new Random();
        
        for (int i = 0; i < count; i++) {
            Product product = new Product();
            product.setName(keyword + " - " + mall + "版本" + (i + 1));
            product.setPrice(new BigDecimal(50 + random.nextInt(500)));
            product.setSourceMall(mall);
            product.setBrand(mall + "品牌" + (i + 1));
            product.setSalesVolume(random.nextInt(5000));
            product.setRating(3.5 + random.nextDouble() * 1.5);
            product.setReviewCount(random.nextInt(1000));
            
            products.add(product);
        }
        
        return products;
    }
}