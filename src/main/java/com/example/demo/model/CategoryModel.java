package com.example.demo.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryModel {
    private Long categoryId;

    @NotEmpty(message = "Tên danh mục không được để trống!") // Kiểm tra không rỗng
    private String name;

    private String images;
    private int status = 1;
    
    private Boolean isEdit = false; // Đánh dấu: false là Thêm mới, true là Sửa
}