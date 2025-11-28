package com.bjj.mall.controller;

import com.bjj.mall.entity.SearchRecord;
import com.bjj.mall.service.SearchRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "*")
public class SearchRecordController {

    @Autowired
    private SearchRecordService searchRecordService;

    @PostMapping("/record")
    public ResponseEntity<String> saveSearchRecord(@RequestBody SearchRecord searchRecord) {
        searchRecordService.saveSearchRecord(searchRecord);
        return ResponseEntity.ok("Search record saved successfully");
    }

    @GetMapping("/recent")
    public ResponseEntity<List<SearchRecord>> getRecentSearches(@RequestParam(defaultValue = "10") int limit) {
        List<SearchRecord> recentSearches = searchRecordService.getRecentSearches(limit);
        return ResponseEntity.ok(recentSearches);
    }
}