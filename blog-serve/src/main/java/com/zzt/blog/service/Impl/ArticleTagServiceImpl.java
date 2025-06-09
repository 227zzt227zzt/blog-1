package com.zzt.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzt.blog.entity.ArticleTag;
import com.zzt.blog.mapper.ArticleTagMapper;
import com.zzt.blog.service.ArticleTagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

    @Transactional
    @Override
    public void addTagToArticle(ArticleTag articleTag) {
        if (!this.lambdaQuery()
                .eq(ArticleTag::getArticleId, articleTag.getArticleId())
                .eq(ArticleTag::getTagId, articleTag.getTagId())
                .exists()) {
            this.save(articleTag);
        }
    }

    @Transactional
    @Override
    public void removeTagFromArticle(ArticleTag articleTag) {
        this.remove(new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getArticleId, articleTag.getArticleId())
                .eq(ArticleTag::getTagId, articleTag.getTagId()));
    }

    @Override
    public List<Integer> getTagsByArticleId(Long articleId) {
        return this.lambdaQuery()
                .select(ArticleTag::getTagId)
                .eq(ArticleTag::getArticleId, articleId)
                .list()
                .stream()
                .map(ArticleTag::getTagId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getArticlesByTagId(Integer tagId) {
        return this.lambdaQuery()
                .select(ArticleTag::getArticleId)
                .eq(ArticleTag::getTagId, tagId)
                .list()
                .stream()
                .map(ArticleTag::getArticleId)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByArticleId(Long articleId) {
        this.remove(new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getArticleId, articleId));
    }
}