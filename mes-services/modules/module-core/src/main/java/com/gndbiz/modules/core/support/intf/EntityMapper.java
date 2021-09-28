package com.gndbiz.modules.core.support.intf;

public interface EntityMapper<E, D, F> {
	E toEntity(F form);
	D toDto(E entity);
}
