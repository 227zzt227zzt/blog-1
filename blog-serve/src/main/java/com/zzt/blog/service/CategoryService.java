package com.zzt.blog.service;

import com.zzt.blog.dto.CategoryDTO;
import com.zzt.blog.entity.Category;
import java.util.List;

public interface CategoryService {
    List<Category> listCategories();
    Category getCategoryById(Long id);
    void saveCategory(CategoryDTO category);
    void updateCategory(CategoryDTO category);
    void deleteCategory(Long id);
}