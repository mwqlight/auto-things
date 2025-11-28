package com.bjj.mall.service.impl;

import com.bjj.mall.entity.Product;
import com.bjj.mall.repository.ProductRepository;
import com.bjj.mall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<Product> comparePrices(List<Product> products) {
        // 按价格排序
        return products.stream()
                .sorted(Comparator.comparing(Product::getPrice))
                .collect(Collectors.toList());
    }

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public List<Product> getProductsBySource(String sourceMall) {
        return productRepository.findBySourceMallOrderByPriceAsc(sourceMall);
    }
}