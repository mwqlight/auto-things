package com.bjj.mall.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "search_record")
public class SearchRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 搜索关键词
     */
    @Column(name = "keyword", nullable = false)
    private String keyword;

    /**
     * 搜索时间
     */
    @Column(name = "search_time")
    private Date searchTime;

    /**
     * 用户IP地址
     */
    @Column(name = "ip_address")
    private String ipAddress;
}