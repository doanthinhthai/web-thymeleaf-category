package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.example.demo.entity.CategoryEntity;

public interface ICategoryService {
	
	<S extends CategoryEntity> S save(S entity);

	List<CategoryEntity> findAll();

	Page<CategoryEntity> findAll(Pageable pageable);

	List<CategoryEntity> findAll(Sort sort);

	Optional<CategoryEntity> findById(Long id);

	long count();

	void deleteById(Long id);

	void delete(CategoryEntity entity);

	List<CategoryEntity> findByNameContaining(String name);

	Page<CategoryEntity> findByNameContaining(String name, Pageable pageable);
}