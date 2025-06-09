package com.zzt.blog.controller;

import com.zzt.blog.dto.CategoryDTO;
import com.zzt.blog.entity.Category;
import com.zzt.blog.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 227
 */
@RestController
@RequestMapping("/categories")
@Tag(name = "分类管理")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @Operation(summary = "获取所有分类")
    public List<Category> listCategories() {
        return categoryService.listCategories();
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取分类")
    public Category getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping
    @Operation(summary = "创建分类")
    public void createCategory(@RequestBody CategoryDTO category) {
        categoryService.saveCategory(category);
    }

    @PutMapping
    @Operation(summary = "更新分类")
    public void updateCategory(@RequestBody CategoryDTO category) {
        categoryService.updateCategory(category);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除分类")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}