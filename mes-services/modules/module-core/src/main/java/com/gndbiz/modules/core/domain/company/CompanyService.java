package com.gndbiz.modules.core.domain.company;

import com.gndbiz.modules.core.support.service.BaseCrudService;
import org.springframework.stereotype.Service;


@Service
public class CompanyService extends BaseCrudService<Company> {

	private final CompanyRepository companyRepository;

	public CompanyService(CompanyRepository companyRepository) {
		super(companyRepository);
		this.companyRepository = companyRepository;
	}
}
