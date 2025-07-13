package com.jobportal.search.repository;

import com.jobportal.search.entity.JobDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobSearchRepository extends ElasticsearchRepository<JobDocument, String> {
    List<JobDocument> findByTitleContainingIgnoreCase(String title);
    List<JobDocument> findByLocation(String location);
    List<JobDocument> findByJobType(String jobType);
    List<JobDocument> findByCompanyName(String companyName);
    List<JobDocument> findByIndustry(String industry);
    List<JobDocument> findByStatus(String status);
}