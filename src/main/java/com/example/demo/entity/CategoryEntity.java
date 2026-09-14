package com.example.demo.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

@Data							//tự tạo getter, getter, toString (lombok)
@AllArgsConstructor				//tự tạo constructor có đủ parameter
@NoArgsConstructor				//tự tạo constructor không parameter
@Entity							//báo cho hibernate
@Table(name = "Categories") 		// Tên bảng trong SQL Server
public class CategoryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Khóa chính tự tăng (IDENTITY)
    private Long categoryId;

    @Column(name = "category_name", length = 200, columnDefinition = "nvarchar(200) not null")
    private String name;

    @Column(length = 255)
    private String images;

    private int status = 1; // 1: Hoạt động, 0: Khóa
}