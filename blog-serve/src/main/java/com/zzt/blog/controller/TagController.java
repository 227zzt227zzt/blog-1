package com.zzt.blog.controller;

import com.zzt.blog.entity.Tags;
import com.zzt.blog.service.TagService;
import com.zzt.blog.util.Result;
import com.zzt.blog.util.UserContext;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 227
 */
@RestController
@RequestMapping("/tags")
@Tag(name = "标签管理")
public class TagController {
    
    @Autowired
    private TagService tagService;

    @GetMapping
    @Operation(summary = "获取所有标签")
    public Result<List<Tags>> listTags() {
        return Result.success(tagService.listTags());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取标签")
    public Tags getTagById(@PathVariable Integer id) {
        return tagService.getTagById(id);
    }

    @PostMapping
    @Operation(summary = "创建标签")
    public Result<Void> createTag(@RequestBody Tags tag) {
        tagService.saveTag(tag);
        return Result.success();

    }

    @PutMapping
    @Operation(summary = "更新标签")
    public Result<Void> updateTag(@RequestBody Tags tag) {
        tagService.updateTag(tag);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除标签")
    public Result<Void> deleteTag(@PathVariable Integer id) {
        tagService.deleteTag(id);
        return Result.success();
    }
}