package com.zzt.blog.controller;

import cn.hutool.db.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzt.blog.dto.SensitiveWordDTO;
import com.zzt.blog.entity.SensitiveWord;
import com.zzt.blog.service.SensitiveWordService;
import com.zzt.blog.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sensitive-words")
@Tag(name = "敏感词管理")
public class SensitiveWordController {
    
    @Autowired
    private SensitiveWordService sensitiveWordService;

    @GetMapping("/pageSensitivePage")
    @Operation(summary = "获取所有敏感词")
    public Result<Map<String, Object>> sensitivePage(@RequestParam(required = false, defaultValue = "1") Integer page,
                                                     @RequestParam(required = false, defaultValue = "10") Integer size) {
        Map<String, Object> result = new HashMap<>();
        result.put("page", page);
        result.put("size", size);
        result.put("users", sensitiveWordService.getSensitivePage(page, size));
        return Result.success("获取成功", result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取敏感词")
    public SensitiveWord getSensitiveWordById(@PathVariable Integer id) {
        return sensitiveWordService.getSensitiveWordById(id);
    }

    @PostMapping
    @Operation(summary = "创建敏感词")
    public void createSensitiveWord(@RequestBody SensitiveWordDTO sensitiveWord) {
        sensitiveWordService.saveSensitiveWord(sensitiveWord);
    }

    @PutMapping
    @Operation(summary = "更新敏感词")
    public void updateSensitiveWord(@RequestBody SensitiveWord sensitiveWord) {
        sensitiveWordService.updateSensitiveWord(sensitiveWord);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除敏感词")
    public void deleteSensitiveWord(@PathVariable Integer id) {
        sensitiveWordService.deleteSensitiveWord(id);
    }
}