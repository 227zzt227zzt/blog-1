package com.zzt.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzt.blog.dto.article.ArticleDTO;
import com.zzt.blog.dto.article.ArticleUpdateDTO;
import com.zzt.blog.entity.Article;
import com.zzt.blog.vo.ArticleVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 227
 */


public interface ArticleService {
    Page<Article> pageArticles(Integer currentPage, Integer size);
    ArticleVO getArticleById(Long id ,  Integer currentPage, Integer size);
    void saveArticle(ArticleDTO article);
    Article updateArticle(ArticleUpdateDTO article);
    void deleteArticle(Long id);
    List<Article> getTopArticles();
    String uploadCoverImage(MultipartFile file);

    Page<Article> getArticlePageByUserId(Long userId, Long currentPage, Long size);
}