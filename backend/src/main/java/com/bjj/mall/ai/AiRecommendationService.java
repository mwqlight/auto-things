package com.bjj.mall.ai;

import com.bjj.mall.entity.Product;
import java.util.List;

public interface AiRecommendationService {
    String generateRecommendation(List<Product> products);
    void reloadAiModel();
}