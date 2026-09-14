package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.example.demo.entity.CategoryEntity;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.ICategoryService;

@Service
public class CategoryServiceImpl implements ICategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public <S extends CategoryEntity> S save(S entity) {
		
		// Nếu là thêm mới (chưa có ID) -> Lưu mới luôn
		if (entity.getCategoryId() == null) {
			return categoryRepository.save(entity);
		} else {
			// Nếu là cập nhật (đã có ID) -> kiểm tra để tránh mất dữ liệu cũ
			Optional<CategoryEntity> opt = findById(entity.getCategoryId());
			if (opt.isPresent()) {
				if (!StringUtils.hasText(entity.getName())) {
					entity.setName(opt.get().getName()); // Nếu tên mới để trống thì giữ tên cũ
				}
				if (!StringUtils.hasText(entity.getImages())) {
					entity.setImages(opt.get().getImages()); // Nếu ảnh mới để trống thì giữ ảnh cũ
				}
			}
			return categoryRepository.save(entity);
		}
	}

	@Override
	public List<CategoryEntity> findAll() {
		return categoryRepository.findAll();
	}

	@Override
	public Page<CategoryEntity> findAll(Pageable pageable) {
		return categoryRepository.findAll(pageable);
	}

	@Override
	public List<CategoryEntity> findAll(Sort sort) {
		return categoryRepository.findAll(sort);
	}

	@Override
	public Optional<CategoryEntity> findById(Long id) {
		return categoryRepository.findById(id);
	}

	@Override
	public long count() {
		return categoryRepository.count();
	}

	@Override
	public void deleteById(Long id) {
		categoryRepository.deleteById(id);
	}

	@Override
	public void delete(CategoryEntity entity) {
		categoryRepository.delete(entity);
	}

	@Override
	public List<CategoryEntity> findByNameContaining(String name) {
		return categoryRepository.findByNameContaining(name);
	}

	@Override
	public Page<CategoryEntity> findByNameContaining(String name, Pageable pageable) {
		return categoryRepository.findByNameContaining(name, pageable);
	}
}