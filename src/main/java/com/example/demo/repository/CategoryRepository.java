package com.example.demo.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.CategoryEntity;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

	// 1. Tìm kiếm theo tên (không phân trang)
	List<CategoryEntity> findByNameContaining(String name);

	// 2. Tìm kiếm theo tên CÓ PHÂN TRANG
	Page<CategoryEntity> findByNameContaining(String name, Pageable pageable);
}

/*
 * JpaRepository: có sẵn các hàm CRUD như: save(), findById(), .. -> kế thừa
 * findByNameContaining: là hàm suy diễn (derived query method). Khi thấy chữ
 * Containing -> Spring Data JPA tự dịch sang SQL Server Pageable: chứa info
 * need ở trang thứ mấy (page), size, và sort Page: đối tượng trả về chứa list
 * data include: totalPages, totalElements, number
 */