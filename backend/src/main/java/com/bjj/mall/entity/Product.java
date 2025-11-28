package com.bjj.mall.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商品名称
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 商品描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 商品价格
     */
    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * 商品来源商城
     */
    @Column(name = "source_mall")
    private String sourceMall;

    /**
     * 商品链接
     */
    @Column(name = "product_url")
    private String productUrl;

    /**
     * 商品图片链接
     */
    @Column(name = "image_url")
    private String imageUrl;

    /**
     * 品牌
     */
    @Column(name = "brand")
    private String brand;

    /**
     * 销量
     */
    @Column(name = "sales_volume")
    private Integer salesVolume;

    /**
     * 评分
     */
    @Column(name = "rating")
    private Double rating;

    /**
     * 评论数
     */
    @Column(name = "review_count")
    private Integer reviewCount;

    /**
     * 商品参数
     */
    @Column(name = "parameters", columnDefinition = "TEXT")
    private String parameters;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;
}