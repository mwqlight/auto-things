package com.bjj.mall.service.impl;

import com.bjj.mall.entity.SearchRecord;
import com.bjj.mall.repository.SearchRecordRepository;
import com.bjj.mall.service.SearchRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchRecordServiceImpl implements SearchRecordService {

    @Autowired
    private SearchRecordRepository searchRecordRepository;

    @Override
    public void saveSearchRecord(SearchRecord searchRecord) {
        searchRecordRepository.save(searchRecord);
    }

    @Override
    public List<SearchRecord> getRecentSearches(int limit) {
        // 获取最近的搜索记录，按时间倒序排列
        return searchRecordRepository.findAll().stream()
                .sorted((r1, r2) -> r2.getSearchTime().compareTo(r1.getSearchTime()))
                .limit(limit)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<SearchRecord> searchByKeyword(String keyword) {
        return searchRecordRepository.findByKeywordContainingOrderBySearchTimeDesc(keyword);
    }
}