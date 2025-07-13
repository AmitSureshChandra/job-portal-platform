package com.jobportal.search.service;

import com.jobportal.search.dto.SearchRequest;
import com.jobportal.search.entity.JobDocument;
import com.jobportal.search.repository.JobSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {
    
    @Autowired
    private JobSearchRepository jobSearchRepository;
    
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
    
    public JobDocument indexJob(JobDocument jobDocument) {
        return jobSearchRepository.save(jobDocument);
    }
    
    public List<JobDocument> searchJobs(SearchRequest request) {
        Criteria criteria = new Criteria();
        
        if (request.getQuery() != null && !request.getQuery().isEmpty()) {
            criteria = criteria.and(
                new Criteria("title").matches(request.getQuery())
                .or(new Criteria("description").matches(request.getQuery()))
                .or(new Criteria("requirements").matches(request.getQuery()))
            );
        }
        
        if (request.getLocation() != null) {
            criteria = criteria.and(new Criteria("location").is(request.getLocation()));
        }
        
        if (request.getJobTypes() != null && !request.getJobTypes().isEmpty()) {
            criteria = criteria.and(new Criteria("jobType").in(request.getJobTypes()));
        }
        
        if (request.getIndustry() != null) {
            criteria = criteria.and(new Criteria("industry").is(request.getIndustry()));
        }
        
        if (request.getMinSalary() != null) {
            criteria = criteria.and(new Criteria("maxSalary").greaterThanEqual(request.getMinSalary()));
        }
        
        if (request.getMaxSalary() != null) {
            criteria = criteria.and(new Criteria("minSalary").lessThanEqual(request.getMaxSalary()));
        }
        
        // Only active jobs
        criteria = criteria.and(new Criteria("status").is("ACTIVE"));
        
        CriteriaQuery query = new CriteriaQuery(criteria);
        query.setPageable(PageRequest.of(request.getPage(), request.getSize()));
        
        SearchHits<JobDocument> searchHits = elasticsearchOperations.search(query, JobDocument.class);
        return searchHits.stream().map(hit -> hit.getContent()).collect(Collectors.toList());
    }
    
    public List<JobDocument> findByTitle(String title) {
        return jobSearchRepository.findByTitleContainingIgnoreCase(title);
    }
    
    public List<JobDocument> findByLocation(String location) {
        return jobSearchRepository.findByLocation(location);
    }
    
    public List<JobDocument> findByCompany(String companyName) {
        return jobSearchRepository.findByCompanyName(companyName);
    }
    
    public void deleteJob(String id) {
        jobSearchRepository.deleteById(id);
    }
}