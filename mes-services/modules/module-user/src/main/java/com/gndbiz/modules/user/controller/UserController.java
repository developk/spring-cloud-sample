package com.gndbiz.modules.user.controller;

import com.gndbiz.modules.core.domain.user.User;
import com.gndbiz.modules.core.domain.user.UserService;
import com.gndbiz.modules.core.support.controller.BaseCrudConroller;
import com.gndbiz.modules.core.support.enums.StatusType;
import com.gndbiz.modules.core.support.http.RequestBean;
import com.gndbiz.modules.core.support.http.ResponseBean;
import com.gndbiz.modules.user.dto.UserDTO;
import com.gndbiz.modules.user.form.UserForm;
import com.gndbiz.modules.user.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name="user", description = "사용자 API Microservice")
@Slf4j
@RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
public class UserController extends BaseCrudConroller<UserForm, UserDTO, User> {

	private UserService userService;
	private UserMapper userMapper;

	@Value("${springdoc.description}")
	private String description;

	public UserController(UserService userService, UserMapper userMapper) {
		super(userService, userMapper);
		this.userService = userService;
		this.userMapper = userMapper;
	}

	@Override
	@Operation(
			operationId = "users-register",
			tags = {"user"},
			method = "POST",
			summary = "사용자 등록",
			description = "사용자 정보를 등록합니다.",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "등록하려는 사용자 정보를 포함하는 요청 데이터",
					required = true
			)
	)
	public ResponseBean<UserDTO> register(@RequestBody @Valid final RequestBean<UserForm> req) {
		return super.register(req);
	}

	@Override
	@Operation(
			operationId = "users-find-one",
			tags = {"user"},
			method = "GET",
			summary = "사용자 검색",
			description = "특정 사용자를 검색합니다."
	)
	public ResponseBean<UserDTO> findOne(
			@Parameter(name = "id", description = "사용자 PK", required = true)
			@PathVariable final int id) {
		return super.findOne(id);
	}

	@Override
	@Operation(
			operationId = "users-update",
			tags = {"user"},
			method = "PUT",
			summary = "사용자 정보 수정",
			description = "사용자 정보를 수정합니다.",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "수정하려는 사용자 정보를 포함하는 요청 데이터",
					required = true
			)
	)
	public ResponseBean<UserDTO> update(@RequestBody final RequestBean<UserForm> req) {
		return super.update(req);
	}

	@Override
	@Operation(
			operationId = "users-delete",
			tags = {"user"},
			method = "DELETE",
			summary = "사용자 삭제",
			description = "특정 사용자를 삭제합니다.",
			hidden = true
	)
	public ResponseBean<UserDTO> delete(
			@Parameter(name = "id", description = "사용자 PK", required = true)
			@PathVariable final int id) {
		return super.delete(id);
	}

	@Override
	@Operation(
			operationId = "users-find-all",
			tags = {"user"},
			method = "GET",
			summary = "전체 사용자 조회",
			description = "전체 사용자를 조회합니다."
	)
	public ResponseBean<List<UserDTO>> findAll() {
		log.info("abc: {}", description);
		return super.findAll();
	}

	@Operation(
			operationId = "users-find-by-user-name",
			tags = {"user"},
			method = "GET",
			summary = "계정을 통한 사용자 정보 조회",
			description = "계정을 통해 사용자 정보를 조회합니다."
	)
	@GetMapping("/name/{username}")
	public ResponseBean<UserDTO> findUserByUsername(@PathVariable String username) {
		User findUser = userService.findUserByUsername(username);
		UserDTO dto = userMapper.toDto(findUser);
		return new ResponseBean<>(StatusType.OK, dto);
	}

}
