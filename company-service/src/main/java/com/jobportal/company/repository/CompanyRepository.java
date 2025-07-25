package com.jobportal.company.repository;

import com.jobportal.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByName(String name);
    List<Company> findByOwnerId(Long ownerId);
    List<Company> findByIndustry(String industry);
    List<Company> findByLocation(String location);
    boolean existsByName(String name);
}