package com.bjj.mall.service;

import com.bjj.mall.entity.Product;
import java.util.List;

public interface ProductService {
    List<Product> searchProducts(String keyword);
    List<Product> comparePrices(List<Product> products);
    void saveProduct(Product product);
    List<Product> getProductsBySource(String sourceMall);
}