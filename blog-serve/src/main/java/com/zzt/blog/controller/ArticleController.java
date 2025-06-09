package com.zzt.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzt.blog.dto.article.ArticleDTO;
import com.zzt.blog.dto.article.ArticleUpdateDTO;
import com.zzt.blog.entity.Article;
import com.zzt.blog.service.ArticleService;
import com.zzt.blog.util.Result;
import com.zzt.blog.vo.ArticleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * @author 227
 */
@RestController
@RequestMapping("/articles")
@Tag(name = "文章管理")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping
    @Operation(summary = "获取文章列表")
    public Result<Page<Article>> listArticles(
            @Parameter(description = "页码", required = true) @RequestParam(defaultValue = "1") Integer currentPage,
            @Parameter(description = "每页数量", required = true) @RequestParam(defaultValue = "10") Integer size) {
        Page<Article> articlePage = articleService.pageArticles(currentPage, size);
        return Result.success(articlePage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取文章详情")
    public Result<ArticleVO> getArticleById(@PathVariable Long id,@Parameter(description = "评论页码", required = true) @RequestParam(defaultValue = "1") Integer currentPage,
                                            @Parameter(description = "每页评论数量", required = true) @RequestParam(defaultValue = "10") Integer size) {

        return Result.success(articleService.getArticleById(id,  currentPage, size));
    }


    @PostMapping
    @Operation(summary = "新增文章")
    public Result<ArticleDTO> saveArticle(@RequestBody ArticleDTO article) {
        if (article == null) {
            return Result.error("参数错误");
        }
        articleService.saveArticle(article);
        return Result.success("新增文章成功",article);

    }

    @PutMapping
    @Operation(summary = "更新文章")
    public Result<Article> updateArticle(@RequestBody ArticleUpdateDTO article) {
        return Result.success(articleService.updateArticle(article));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除文章")
    public Result<Void> deleteArticle(@PathVariable Long id) {
        if (id == null) {
            return Result.error("参数错误");
        }
        articleService.deleteArticle(id);
        return Result.success();
    }

    @PostMapping("/uploadCover")
    @Operation(summary = "上传文章封面")
    public Result<String> uploadCover(
            @Parameter(description = "封面图片") @RequestParam("file") MultipartFile file) {

        try {
            String coverUrl = articleService.uploadCoverImage(file);
            return Result.success("上传成功",coverUrl);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    @GetMapping("/getArticlePageByUserId")
    @Operation(summary = "获取用户文章列表")
    public Result<Page<Article>> getArticlePageByUserId(@RequestParam(value = "userId") Long userId,
                                                        @RequestParam(value = "currentPage",defaultValue = "1") Long currentPage,
                                                        @RequestParam(value = "size",defaultValue = "10") Long size){
        return Result.success(articleService.getArticlePageByUserId(userId,currentPage,size));
    }

}