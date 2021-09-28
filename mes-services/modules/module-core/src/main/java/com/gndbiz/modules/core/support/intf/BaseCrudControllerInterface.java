package com.gndbiz.modules.core.support.intf;

import com.gndbiz.modules.core.support.http.RequestBean;
import com.gndbiz.modules.core.support.http.ResponseBean;

import java.util.List;

/**
 *  CRUD 공통 기능 인터페이스
 * @param <Req> Form
 * @param <Res> DTO
 */
public interface BaseCrudControllerInterface<Req, Res> {
	ResponseBean<Res> register(RequestBean<Req> req);
	ResponseBean<Res> findOne(int id);
	ResponseBean<Res> update(RequestBean<Req> req);
	ResponseBean<Res> delete(int id);
	ResponseBean<List<Res>> findAll();
}