package com.gndbiz.modules.company.mapper;

import com.gndbiz.modules.company.dto.CompanyDTO;
import com.gndbiz.modules.company.form.CompanyForm;
import com.gndbiz.modules.core.domain.company.Company;
import com.gndbiz.modules.core.support.intf.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CompanyMapper extends EntityMapper<Company, CompanyDTO, CompanyForm> {

	@Override
	Company toEntity(CompanyForm form);

	@Override
	CompanyDTO toDto(Company entity);

}
