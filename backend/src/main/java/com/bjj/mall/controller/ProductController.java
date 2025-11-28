package com.bjj.mall.controller;

import com.bjj.mall.entity.Product;
import com.bjj.mall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> products = productService.searchProducts(keyword);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/compare")
    public ResponseEntity<List<Product>> comparePrices(@RequestBody List<Product> products) {
        List<Product> comparedProducts = productService.comparePrices(products);
        return ResponseEntity.ok(comparedProducts);
    }
}