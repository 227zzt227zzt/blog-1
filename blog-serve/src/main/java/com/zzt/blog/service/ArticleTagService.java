package com.zzt.blog.service;

import com.zzt.blog.entity.ArticleTag;
import java.util.List;

public interface ArticleTagService {
    void addTagToArticle(ArticleTag articleTag);
    void removeTagFromArticle(ArticleTag articleTag);
    List<Integer> getTagsByArticleId(Long articleId);
    List<Long> getArticlesByTagId(Integer tagId);
    void deleteByArticleId(Long articleId);
}