package com.gndbiz.modules.company.controller;

import com.gndbiz.modules.company.dto.CompanyDTO;
import com.gndbiz.modules.company.form.CompanyForm;
import com.gndbiz.modules.company.mapper.CompanyMapper;
import com.gndbiz.modules.core.domain.company.Company;
import com.gndbiz.modules.core.domain.company.CompanyService;
import com.gndbiz.modules.core.support.controller.BaseCrudConroller;
import com.gndbiz.modules.core.support.http.RequestBean;
import com.gndbiz.modules.core.support.http.ResponseBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/company", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "company", description = "회사 API Microservice")
public class CompanyController extends BaseCrudConroller<CompanyForm, CompanyDTO, Company> {

	private CompanyService companyService;
	private CompanyMapper companyMapper;

	public CompanyController(CompanyService companyService, CompanyMapper companyMapper) {
		super(companyService, companyMapper);
		this.companyService = companyService;
		this.companyMapper = companyMapper;
	}

	@Override
	@Operation(
			operationId = "company-register",
			tags = {"company"},
			method = "POST",
			summary = "회사 등록",
			description = "회사 정보를 등록합니다.",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
				description = "등록하려는 회서 정보를 포함하는 요청 데이터",
				required = true
			)

	)
	public ResponseBean<CompanyDTO> register(@RequestBody @Valid final RequestBean<CompanyForm> req) {
		return super.register(req);
	}

	@Override
	@Operation(
			operationId = "company-find-one",
			tags = {"company"},
			method = "GET",
			summary = "회사 검색",
			description = "특정 회사를 검색합니다."
	)
	public ResponseBean<CompanyDTO> findOne(
			@Parameter(name = "id", description = "회사 PK", required = true)
			@PathVariable final int id) {
		return super.findOne(id);
	}

	@Override
	@Operation(
			operationId = "company-update",
			tags = {"company"},
			method = "PUT",
			summary = "회사 정보 수정",
			description = "회사 정보를 수정합니다.",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					  description = "수정하려는 회사 정보를 포함하는 요청 데이터",
					  required = true
			)
	)
	public ResponseBean<CompanyDTO> update(@RequestBody final RequestBean<CompanyForm> req) {
		return super.update(req);
	}

	@Override
	@Operation(
			operationId = "company-delete",
			tags = {"company"},
			method = "DELETE",
			summary = "회사 정보 삭제",
			description = "특정 회사를 삭제합니다.",
			hidden = true
	)
	public ResponseBean<CompanyDTO> delete(
			@Parameter(name = "id", description = "회사 PK", required = true)
			@PathVariable final int id) {
		return super.delete(id);
	}

	@Override
	@Operation(
			operationId = "company-find-all",
			tags = {"company"},
			method = "GET",
			summary = "전체 등록 회사 조회",
			description = "등록된 전체 회사를 조회합니다."
	)
	public ResponseBean<List<CompanyDTO>> findAll() {
		return super.findAll();
	}

}
