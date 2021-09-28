package com.gndbiz.modules.core.domain.company;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
	Company findCompanyByCode(String code);
}
