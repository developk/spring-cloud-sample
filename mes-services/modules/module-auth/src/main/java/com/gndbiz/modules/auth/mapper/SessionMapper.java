package com.gndbiz.modules.auth.mapper;

import com.gndbiz.modules.auth.dto.SessionDto;
import com.gndbiz.modules.core.domain.user.User;
import com.gndbiz.modules.core.support.intf.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface SessionMapper extends EntityMapper<User, SessionDto, String> {

	@Override
	@Mappings(@Mapping(source = "entity.role", target = "role"))
	SessionDto toDto(User entity);

}
