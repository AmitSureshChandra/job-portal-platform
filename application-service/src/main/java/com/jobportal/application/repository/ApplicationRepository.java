package com.jobportal.application.repository;

import com.jobportal.application.entity.Application;
import com.jobportal.application.entity.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByUserId(Long userId);
    List<Application> findByJobId(Long jobId);
    List<Application> findByCompanyId(Long companyId);
    List<Application> findByStatus(ApplicationStatus status);
    List<Application> findByUserIdAndStatus(Long userId, ApplicationStatus status);
    List<Application> findByCompanyIdAndStatus(Long companyId, ApplicationStatus status);
    Optional<Application> findByUserIdAndJobId(Long userId, Long jobId);
    boolean existsByUserIdAndJobId(Long userId, Long jobId);
}