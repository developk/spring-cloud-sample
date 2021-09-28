package com.gndbiz.modules.core.support.intf;

import java.util.List;

/**
 *
 * CRUD 공통 기능 인터페이스
 *
 * @param <E> Entity
 */
public interface BaseCrudInterface<E> {
	E register(E e);
	E findOne(int id);
	E update(E e);
	E delete(int id);
	List<E> findAll();
}
