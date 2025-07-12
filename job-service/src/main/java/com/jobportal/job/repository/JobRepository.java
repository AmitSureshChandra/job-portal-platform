package com.jobportal.job.repository;

import com.jobportal.job.entity.Job;
import com.jobportal.job.entity.JobStatus;
import com.jobportal.job.entity.JobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByCompanyId(Long companyId);
    List<Job> findByPostedBy(Long postedBy);
    List<Job> findByStatus(JobStatus status);
    List<Job> findByType(JobType type);
    List<Job> findByLocation(String location);
    
    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' AND " +
           "(:title IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:type IS NULL OR j.type = :type)")
    List<Job> searchJobs(@Param("title") String title, 
                        @Param("location") String location, 
                        @Param("type") JobType type);
}