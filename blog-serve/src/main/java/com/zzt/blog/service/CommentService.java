package com.zzt.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzt.blog.dto.CommentDTO;
import com.zzt.blog.entity.Comment;
import com.zzt.blog.vo.CommentVO;

import java.util.List;

/**
 * @author 227
 */
public interface CommentService {
    Comment getCommentById(Long id);
    void saveComment(CommentDTO comment);
    void updateComment(Comment comment);
    void deleteComment(Long id);
    List<Comment> getPendingComments();

    void deleteCommentsByArticleId(Long id);

    Page<CommentVO> getCommentPage( Long articleId ,Integer page, Integer size);
}