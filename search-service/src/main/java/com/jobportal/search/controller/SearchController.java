package com.jobportal.search.controller;

import com.jobportal.search.dto.SearchRequest;
import com.jobportal.search.entity.JobDocument;
import com.jobportal.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    
    @Autowired
    private SearchService searchService;
    
    @PostMapping("/jobs")
    public ResponseEntity<List<JobDocument>> searchJobs(@RequestBody SearchRequest request) {
        List<JobDocument> jobs = searchService.searchJobs(request);
        return ResponseEntity.ok(jobs);
    }
    
    @GetMapping("/jobs/title")
    public ResponseEntity<List<JobDocument>> searchByTitle(@RequestParam String title) {
        List<JobDocument> jobs = searchService.findByTitle(title);
        return ResponseEntity.ok(jobs);
    }
    
    @GetMapping("/jobs/location")
    public ResponseEntity<List<JobDocument>> searchByLocation(@RequestParam String location) {
        List<JobDocument> jobs = searchService.findByLocation(location);
        return ResponseEntity.ok(jobs);
    }
    
    @GetMapping("/jobs/company")
    public ResponseEntity<List<JobDocument>> searchByCompany(@RequestParam String companyName) {
        List<JobDocument> jobs = searchService.findByCompany(companyName);
        return ResponseEntity.ok(jobs);
    }
    
    @PostMapping("/jobs/index")
    public ResponseEntity<JobDocument> indexJob(@RequestBody JobDocument jobDocument) {
        JobDocument indexed = searchService.indexJob(jobDocument);
        return ResponseEntity.ok(indexed);
    }
    
    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable String id) {
        searchService.deleteJob(id);
        return ResponseEntity.ok().build();
    }
}