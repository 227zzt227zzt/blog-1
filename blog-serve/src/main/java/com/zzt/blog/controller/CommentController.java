package com.zzt.blog.controller;

import com.zzt.blog.dto.CommentDTO;
import com.zzt.blog.entity.Comment;
import com.zzt.blog.service.CommentService;
import com.zzt.blog.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 227
 */ // 评论控制器
@RestController
@RequestMapping("/comments")
@Tag(name = "评论管理", description = "评论管理接口")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/article")
    @Operation(summary = "根据文章ID获取评论列表分页获取评论列表")
    public Result<Map<String, Object>> commentPage(
            @RequestParam(required = false, defaultValue = "1") Integer currentPage,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) Long articleId) {
        Map<String, Object> result = new HashMap<>();
        result.put("page", currentPage);
        result.put("size", size);
        result.put("users", commentService.getCommentPage(articleId,currentPage, size));
        return Result.success("获取成功", result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取评论", description = "根据ID获取评论")
    public Result<Comment> getCommentById(@PathVariable Long id) {

        return Result.success(commentService.getCommentById(id));
    }

    @PostMapping("save")
    @Operation(summary = "保存评论", description = "保存评论")
    public Result<Void> saveComment(@RequestBody CommentDTO comment) {
        commentService.saveComment(comment);
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "[待修改]更新评论", description = "更新评论")
    public void updateComment(@RequestBody Comment comment) {
        commentService.updateComment(comment);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除评论", description = "删除评论")
    public Result<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return Result.success();
    }
}