package com.gndbiz.modules.core.support.controller;

import com.gndbiz.modules.core.support.enums.StatusType;
import com.gndbiz.modules.core.support.http.RequestBean;
import com.gndbiz.modules.core.support.http.ResponseBean;
import com.gndbiz.modules.core.support.intf.BaseCrudControllerInterface;
import com.gndbiz.modules.core.support.intf.EntityMapper;
import com.gndbiz.modules.core.support.service.BaseCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
public abstract class BaseCrudConroller<Req, Res, Entity> implements BaseCrudControllerInterface<Req, Res> {

	private final BaseCrudService<Entity> baseService;
	private final EntityMapper<Entity, Res, Req> entityMapper;

	@Override
	@PostMapping
	public ResponseBean<Res> register(RequestBean<Req> req) {
		Entity requestedEntity = entityMapper.toEntity(req.getData());
		Entity registeredEntity =  baseService.register(requestedEntity);
		Res responseDto = entityMapper.toDto(registeredEntity);
		return new ResponseBean<>(StatusType.OK, responseDto);
	}

	@Override
	@GetMapping("{id}")
	public ResponseBean<Res> findOne(@PathVariable int id) {
		Entity findEntity = baseService.findOne(id);
		Res res = entityMapper.toDto(findEntity);
		return new ResponseBean<>(StatusType.OK, res);
	}

	@Override
	@PutMapping
	public ResponseBean<Res> update(RequestBean<Req> req) {
		Entity requestedEntity = entityMapper.toEntity(req.getData());
		Entity updatedEntity = baseService.update(requestedEntity);
		Res res = entityMapper.toDto(updatedEntity);
		return new ResponseBean<>(StatusType.OK, res);
	}

	@Override
	@DeleteMapping("{id}")
	public ResponseBean<Res> delete(@PathVariable int id) {
		Entity findEntity = baseService.delete(id);
		Res res = entityMapper.toDto(findEntity);
		return new ResponseBean<>(StatusType.OK, res);
	}

	@Override
	@GetMapping
	public ResponseBean<List<Res>> findAll() {
		List<Entity> entities = baseService.findAll();
		List<Res> res = new ArrayList<>();
		entities.forEach(entity -> res.add(entityMapper.toDto(entity)));
		return new ResponseBean<>(StatusType.OK, res);
	}

}
