package com.gndbiz.modules.user.mapper;

import com.gndbiz.modules.core.domain.user.User;
import com.gndbiz.modules.core.support.intf.EntityMapper;
import com.gndbiz.modules.user.dto.UserDTO;
import com.gndbiz.modules.user.form.UserForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper extends EntityMapper<User, UserDTO, UserForm> {

	@Override
	@Mappings(@Mapping(source = "form.role", target = "role"))
	User toEntity(UserForm form);

	@Override
	@Mappings(@Mapping(source = "entity.role", target = "role"))
	UserDTO toDto(User entity);
}
