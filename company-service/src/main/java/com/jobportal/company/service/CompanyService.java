package com.jobportal.company.service;

import com.jobportal.company.dto.CompanyRegistrationDto;
import com.jobportal.company.entity.Company;
import com.jobportal.company.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CompanyService {
    
    @Autowired
    private CompanyRepository companyRepository;
    
    public Company registerCompany(CompanyRegistrationDto dto) {
        if (companyRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Company name already exists");
        }
        
        Company company = new Company();
        company.setName(dto.getName());
        company.setDescription(dto.getDescription());
        company.setIndustry(dto.getIndustry());
        company.setLocation(dto.getLocation());
        company.setWebsite(dto.getWebsite());
        company.setSize(dto.getSize());
        company.setOwnerId(dto.getOwnerId());
        
        return companyRepository.save(company);
    }
    
    public Company findById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
    }
    
    public List<Company> findByOwnerId(Long ownerId) {
        return companyRepository.findByOwnerId(ownerId);
    }
    
    public List<Company> searchByIndustry(String industry) {
        return companyRepository.findByIndustry(industry);
    }
    
    public List<Company> searchByLocation(String location) {
        return companyRepository.findByLocation(location);
    }
}