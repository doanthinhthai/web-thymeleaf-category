package com.example.demo.controller.admin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import com.example.demo.entity.CategoryEntity;
import com.example.demo.model.CategoryModel;
import com.example.demo.service.ICategoryService;

@Controller
@RequestMapping("admin/categories")
public class CategoryController {

	@Autowired
	private ICategoryService categoryService;

	// 1. Hiển thị form Thêm mới
	@GetMapping("add")
	public String add(ModelMap model) {
		CategoryModel cateModel = new CategoryModel();
		cateModel.setIsEdit(false); // Đánh dấu là thêm mới
		model.addAttribute("category", cateModel);
		return "admin/categories/addOrEdit";
	}

	// 2. Hiển thị form Sửa
	@GetMapping("edit/{categoryId}")
	public ModelAndView edit(ModelMap model, @PathVariable("categoryId") Long categoryId) {
		Optional<CategoryEntity> optCategory = categoryService.findById(categoryId);
		CategoryModel cateModel = new CategoryModel();

		if (optCategory.isPresent()) {
			CategoryEntity entity = optCategory.get();
			// Copy toàn bộ dữ liệu từ Entity sang Model để hiển thị lên form
			BeanUtils.copyProperties(entity, cateModel);
			cateModel.setIsEdit(true); // Đánh dấu là đang cập nhật
			model.addAttribute("category", cateModel);
			return new ModelAndView("admin/categories/addOrEdit", model);
		}

		model.addAttribute("message", "Category không tồn tại!");
		return new ModelAndView("forward:/admin/categories/searchpaginated", model);
	}

	// 3. Xử lý Lưu Categor
	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("category") CategoryModel cateModel,
			BindingResult result) {

		// Nếu người dùng nhập sai quy tắc Validation (để trống tên) -> giữ lại trang
		// form để hiện lỗi
		if (result.hasErrors()) {
			return new ModelAndView("admin/categories/addOrEdit");
		}

		CategoryEntity entity = new CategoryEntity();
		// Copy dữ liệu từ form (Model) sang Entity để chuẩn bị lưu xuống DB
		BeanUtils.copyProperties(cateModel, entity);
		categoryService.save(entity);

		String message = cateModel.getIsEdit() ? "Cập nhật Category thành công!" : "Thêm mới Category thành công!";
		model.addAttribute("message", message);
		return new ModelAndView("forward:/admin/categories/searchpaginated", model);
	}

	// 4. Xóa Category 
	@GetMapping("delete/{categoryId}")
	public ModelAndView delete(ModelMap model, @PathVariable("categoryId") Long categoryId) {
		categoryService.deleteById(categoryId);
		model.addAttribute("message", "Xóa Category thành công!");
		return new ModelAndView("forward:/admin/categories/searchpaginated", model);
	}

	// 5. Tìm kiếm và Phân trang
	@RequestMapping("searchpaginated")
	public String search(ModelMap model, @RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size) {

		int currentPage = page.orElse(1); // Mặc định là trang 1 nếu người dùng không truyền
		int pageSize = size.orElse(5); // Mặc định mỗi trang 5 phần tử

		// Sắp xếp theo tên tăng dần
		Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("name"));
		Page<CategoryEntity> resultPage = null;

		// Nếu có gõ từ khóa tìm kiếm
		if (StringUtils.hasText(name)) {
			resultPage = categoryService.findByNameContaining(name, pageable);
			model.addAttribute("name", name); // Giữ lại từ khóa trên ô input
		} else {
			resultPage = categoryService.findAll(pageable);
		}

		// Tính toán các nút bấm số trang (1, 2, 3...) cho thanh phân trang Bootstrap
		int totalPages = resultPage.getTotalPages();
		if (totalPages > 0) {
			int start = Math.max(1, currentPage - 2);
			int end = Math.min(currentPage + 2, totalPages);
			if (totalPages > 5) {
				if (end == totalPages)
					start = end - 4;
				else if (start == 1)
					end = start + 4;
			}
			List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		model.addAttribute("categoryPage", resultPage);
		return "admin/categories/searchpaging";
	}

	// Đường dẫn gốc: tự chuyển hướng về trang tìm kiếm & phân trang
	@GetMapping("")
	public String index() {
		return "redirect:/admin/categories/searchpaginated";
	}
}