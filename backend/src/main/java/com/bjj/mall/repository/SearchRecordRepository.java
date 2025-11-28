package com.bjj.mall.repository;

import com.bjj.mall.entity.SearchRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRecordRepository extends JpaRepository<SearchRecord, Long> {
    List<SearchRecord> findByKeywordContainingOrderBySearchTimeDesc(String keyword);
}