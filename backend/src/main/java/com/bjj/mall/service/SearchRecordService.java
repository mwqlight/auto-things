package com.bjj.mall.service;

import com.bjj.mall.entity.SearchRecord;
import java.util.List;

public interface SearchRecordService {
    void saveSearchRecord(SearchRecord searchRecord);
    List<SearchRecord> getRecentSearches(int limit);
    List<SearchRecord> searchByKeyword(String keyword);
}