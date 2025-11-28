package com.bjj.mall.ai.impl;

import com.bjj.mall.entity.Product;
import com.bjj.mall.ai.AiRecommendationService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.Constants;

@Service
public class AiRecommendationServiceImpl implements AiRecommendationService {

    @Value("${spring.ai.alibaba.api-key}")
    private String apiKey;
    
    @Value("${spring.ai.alibaba.model}")
    private String model;

    @Override
    public String generateRecommendation(List<Product> products) {
        // 检查API密钥是否配置
        if (apiKey == null || apiKey.equals("your-api-key")) {
            return generateMockRecommendation(products);
        }
        
        // 构造商品信息字符串
        String productInfo = products.stream()
            .map(product -> String.format("商品名称：%s，价格：¥%s，来源：%s，评分：%s", 
                product.getName(), product.getPrice(), product.getSourceMall(), product.getRating()))
            .collect(Collectors.joining("\n"));
        
        // 构造提示词
        String prompt = String.format("基于以下商品信息，请为用户推荐最适合的商品，并简要说明推荐理由：\n%s\n\n请按以下格式回复：\n推荐商品：<商品名称>\n推荐理由：<简要说明推荐理由>", productInfo);
        
        try {
            // 设置API密钥
            Constants.apiKey = apiKey;
            
            // 创建生成器实例
            Generation gen = new Generation();
            
            // 构造消息
            Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(prompt)
                .build();
            
            // 构建生成参数
            GenerationParam param = GenerationParam.builder()
                .model(model)
                .messages(java.util.Arrays.asList(userMsg))
                .resultFormat("message")
                .build();
            
            // 调用AI模型生成推荐
            GenerationResult result = gen.call(param);
            
            // 返回AI生成的推荐结果
            return result.getOutput().getChoices().get(0).getMessage().getContent();
        } catch (ApiException | NoApiKeyException e) {
            // 如果调用AI接口失败，则回退到模拟推荐
            System.err.println("调用AI接口失败：" + e.getMessage());
            return generateMockRecommendation(products);
        } catch (Exception e) {
            // 其他异常也回退到模拟推荐
            System.err.println("发生未知错误：" + e.getMessage());
            return generateMockRecommendation(products);
        }
    }

    /**
     * 生成模拟推荐结果（当AI接口不可用时使用）
     */
    private String generateMockRecommendation(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return "暂无商品信息可供推荐";
        }
        
        StringBuilder recommendation = new StringBuilder();
        recommendation.append("基于您的搜索，我们为您推荐以下商品：\n");
        
        // 简单的推荐逻辑：推荐价格最低且评分较高的商品
        products.stream()
            .min((p1, p2) -> {
                int priceComparison = p1.getPrice().compareTo(p2.getPrice());
                if (priceComparison != 0) {
                    return priceComparison;
                }
                return p2.getRating().compareTo(p1.getRating());
            })
            .ifPresent(product -> {
                recommendation.append("推荐商品：").append(product.getName())
                    .append("\n价格：¥").append(product.getPrice())
                    .append("\n来源商城：").append(product.getSourceMall())
                    .append("\n评分：").append(product.getRating());
            });
            
        return recommendation.toString();
    }

    @Override
    public void reloadAiModel() {
        // 重新加载AI模型配置
        System.out.println("AI模型配置已更新，API Key: " + (apiKey != null && !apiKey.equals("your-api-key") ? "已配置" : "未配置"));
    }
}