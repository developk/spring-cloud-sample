package com.gndbiz.modules.core.support.service;

import com.gndbiz.modules.core.support.intf.BaseCrudInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class BaseCrudService<Entity> implements BaseCrudInterface<Entity> {
	private final JpaRepository<Entity, Integer> baseRepository;

	@Override
	@Transactional
	public Entity register(Entity entity) {
		return baseRepository.save(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public Entity findOne(int id) {
		Optional<Entity> one = baseRepository.findById(id);
		return one.orElse(null);
	}

	@Override
	@Transactional
	public Entity update(Entity entity) {
		return baseRepository.save(entity);
	}

	@Override
	@Transactional
	public Entity delete(int id) {
		Optional<Entity> targetEntity = baseRepository.findById(id);
		if (targetEntity.isPresent()) {
			baseRepository.delete(targetEntity.get());
		} else {
			return null;
		}

		return targetEntity.get();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Entity> findAll() {
		return baseRepository.findAll();
	}

}
