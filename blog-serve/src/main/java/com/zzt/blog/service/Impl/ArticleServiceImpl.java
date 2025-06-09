package com.zzt.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzt.blog.dto.article.ArticleDTO;
import com.zzt.blog.dto.article.ArticleUpdateDTO;
import com.zzt.blog.entity.Article;
import com.zzt.blog.entity.Comment;
import com.zzt.blog.entity.User;
import com.zzt.blog.mapper.ArticleMapper;
import com.zzt.blog.service.ArticleService;
import com.zzt.blog.service.ArticleTagService;
import com.zzt.blog.service.CommentService;
import com.zzt.blog.exception.BusinessException;
import com.zzt.blog.exception.ErrorCode;
import com.zzt.blog.service.UserService;
import com.zzt.blog.util.UserContext;
import com.zzt.blog.vo.ArticleVO;
import com.zzt.blog.vo.CommentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;

/**
 * @author 227
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {




    @Override
    public Page<Article> pageArticles(Integer currentPage, Integer size) {
        // 构建查询条件
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Article::getUpdateTime);

        // 执行分页查询
        return baseMapper.selectPage(new Page<>(currentPage, size), wrapper);
    }
    @Autowired
    private UserService userService;
    @Override
    public ArticleVO getArticleById(Long id,  Integer currentPage, Integer size) {
        Article article = this.getById(id);
        if (article == null) {
            throw new BusinessException(ErrorCode.ARTICLE_NOT_FOUND);
        }
        //阅读数量+1
        article.setViewCount(article.getViewCount() + 1);
        this.updateById(article);

        ArticleVO articleVO = new ArticleVO();
        BeanUtils.copyProperties(article, articleVO);
        User user = userService.getUserById(article.getAuthorId());
        articleVO.setAuthor(user.getNickname());
        articleVO.setAuthorId(user.getId());

        return articleVO;
    }

    @Override
    public void saveArticle(ArticleDTO article) {
        if (article == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        Article articleEntity = new Article();
        BeanUtils.copyProperties(article, articleEntity);
        articleEntity.setCreateTime(new Date());
        articleEntity.setViewCount(0);
        articleEntity.setIsTop(false);
        articleEntity.setAuthorId(UserContext.getUserId());
        this.save(articleEntity);
    }

    @Override
    public Article updateArticle(ArticleUpdateDTO article) {

        if (article == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        Article articleEntity = new Article();
        BeanUtils.copyProperties(article, articleEntity);
        articleEntity.setUpdateTime(new Date());
        this.updateById(articleEntity);
        return articleEntity;
    }

    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private CommentService commentService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteArticle(Long id) {
        // 删除文章时，同时删除文章标签关系,还有文章和分类关系，评论等
        articleTagService.deleteByArticleId(id);
        commentService.deleteCommentsByArticleId(id);
        this.removeById(id);
    }

    @Override
    public List<Article> getTopArticles() {
        return this.list(new LambdaQueryWrapper<Article>()
                .eq(Article::getIsTop, true)
                .orderByDesc(Article::getUpdateTime));
    }

    @Override
    public String uploadCoverImage(MultipartFile file) {
        // 检查用户是否存在

        // 检查文件是否为空
        if (file.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        // 检查文件类型是否合法
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        // 保存文件到本地
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        String fileSuffix = fileName.substring(fileName.lastIndexOf("."));

        // 获取项目根目录的绝对路径
        String uploadDir = "D:/project/blog/blog/blog-serve/upload/coverImages";


        // 确保上传目录存在
        java.io.File dir = new java.io.File(uploadDir);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new BusinessException(ErrorCode.SAVE_FILE_ERROR);
            }
        }
        //文件名字复杂化，防止重名,根据时间戳+用户id+文件后缀
        fileName = randomUUID() + fileSuffix;
        fileName =   System.currentTimeMillis() + "_"  + fileName;
        String filePath = uploadDir + "/" + fileName;
        try {
            file.transferTo(new java.io.File(filePath));
        } catch (Exception e) {
            e.printStackTrace(); // 添加详细错误日志
            throw new BusinessException(ErrorCode.SAVE_FILE_ERROR);
        }
        return  "upload/coverImages/"+fileName;
    }


    @Override
    public Page<Article> getArticlePageByUserId(Long userId, Long currentPage, Long size) {
        // 构建查询条件
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getAuthorId, userId)
               .orderByDesc(Article::getUpdateTime);
        // 执行分页查询
        return baseMapper.selectPage(new Page<>(currentPage, size), wrapper);
    }
}