package com.zzt.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzt.blog.dto.CategoryDTO;
import com.zzt.blog.entity.Category;
import com.zzt.blog.mapper.CategoryMapper;
import com.zzt.blog.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author 227
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public List<Category> listCategories() {
        return this.list(new LambdaQueryWrapper<Category>().orderByAsc(Category::getSort));
    }

    @Override
    public Category getCategoryById(Long id) {
        return this.getById(id);
    }

    @Autowired
    private CategoryMapper categoryMapper;

    @Transactional
    @Override
    public void saveCategory(CategoryDTO category) {
        if (this.lambdaQuery().eq(Category::getName, category.getName()).exists()) {
            throw new RuntimeException("分类名称已存在");
        }
        Category newCategory = new Category();
        BeanUtils.copyProperties(category, newCategory);
        //  设置创建时间
        newCategory.setCreateTime(new Date());
        categoryMapper.insert(newCategory);

    }


    @Transactional
    @Override
    public void updateCategory(CategoryDTO category) {
        Category newCategory = new Category();
        BeanUtils.copyProperties(category, newCategory);
        //  设置更新时间
        newCategory.setUpdateTime(new Date());
        this.updateById(newCategory);
    }
    /**
     * 删除分类
     *1. 判断分类下是否存在文章，如果存在文章，则无法删除
     */
    @Transactional
    @Override
    public void deleteCategory(Long id) {
        // 自定义SQL方法需要在CategoryMapper中定义
        if (baseMapper.countArticlesByCategory(id) > 0) {
            throw new RuntimeException("分类下存在文章，无法删除");
        }
        this.removeById(id);
    }
}