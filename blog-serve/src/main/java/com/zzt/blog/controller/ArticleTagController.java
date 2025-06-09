package com.zzt.blog.controller;

import com.zzt.blog.entity.ArticleTag;
import com.zzt.blog.service.ArticleTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 227
 */
@RestController
@RequestMapping("/article-tags")
@Tag(name = "文章标签关联管理")
public class ArticleTagController {

    @Autowired
    private ArticleTagService articleTagService;

    // 为文章添加标签
    @PostMapping
    @Operation(summary = "添加文章标签关联")
    public void addTagToArticle(@RequestBody ArticleTag articleTag) {
        articleTagService.addTagToArticle(articleTag);
    }

    // 移除文章的指定标签
    @DeleteMapping
    @Operation(summary = "移除文章标签关联")
    public void removeTagFromArticle(@RequestBody ArticleTag articleTag) {
        articleTagService.removeTagFromArticle(articleTag);
    }

    // 获取文章的所有标签
    @GetMapping("/article/{articleId}")
    @Operation(summary = "获取文章的所有标签")
    public List<Integer> getTagsByArticleId(@PathVariable Long articleId) {
        return articleTagService.getTagsByArticleId(articleId);
    }

    // 获取使用该标签的所有文章
    @GetMapping("/tag/{tagId}")
    @Operation(summary = "获取使用标签的所有文章")
    public List<Long> getArticlesByTagId(@PathVariable Integer tagId) {
        return articleTagService.getArticlesByTagId(tagId);
    }
}