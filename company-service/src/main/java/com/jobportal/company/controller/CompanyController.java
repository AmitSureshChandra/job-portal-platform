package com.jobportal.company.controller;

import com.jobportal.company.dto.CompanyRegistrationDto;
import com.jobportal.company.entity.Company;
import com.jobportal.company.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    
    @Autowired
    private CompanyService companyService;
    
    @PostMapping("/register")
    public ResponseEntity<Company> registerCompany(@Valid @RequestBody CompanyRegistrationDto dto) {
        Company company = companyService.registerCompany(dto);
        return ResponseEntity.ok(company);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        Company company = companyService.findById(id);
        return ResponseEntity.ok(company);
    }
    
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Company>> getCompaniesByOwner(@PathVariable Long ownerId) {
        List<Company> companies = companyService.findByOwnerId(ownerId);
        return ResponseEntity.ok(companies);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Company>> searchCompanies(
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String location) {
        
        if (industry != null) {
            return ResponseEntity.ok(companyService.searchByIndustry(industry));
        }
        if (location != null) {
            return ResponseEntity.ok(companyService.searchByLocation(location));
        }
        
        return ResponseEntity.badRequest().build();
    }
}