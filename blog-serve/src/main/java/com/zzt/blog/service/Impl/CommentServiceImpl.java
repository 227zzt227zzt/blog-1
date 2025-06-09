package com.zzt.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzt.blog.dto.CommentDTO;
import com.zzt.blog.entity.Comment;
import com.zzt.blog.entity.User;
import com.zzt.blog.enums.UserStatus;
import com.zzt.blog.mapper.CommentMapper;
import com.zzt.blog.service.CommentService;
import com.zzt.blog.service.UserService;
import com.zzt.blog.vo.CommentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {



    @Override
    public Comment getCommentById(Long id) {
        return this.getById(id);
    }

    @Transactional
    @Override
    public void saveComment(CommentDTO comment) {
        Comment c = new Comment();
        BeanUtils.copyProperties(comment, c);
        this.save(c);
    }

    @Transactional
    @Override
    public void updateComment(Comment comment) {
        this.updateById(comment);
    }

    @Transactional
    @Override
    public void deleteComment(Long id) {
        // 级联删除子评论
        this.remove(new LambdaQueryWrapper<Comment>().eq(Comment::getParentId, id));
        this.removeById(id);
    }

    @Override
    public List<Comment> getPendingComments() {
        return this.list(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getStatus, 0));
    }

    @Override
    public void deleteCommentsByArticleId(Long id) {
        this.remove(new LambdaQueryWrapper<Comment>().eq(Comment::getArticleId, id));
    }
    @Autowired
    private UserService userService;
    @Override
    public Page<CommentVO> getCommentPage(Long articleId, Integer page, Integer size) {
        //根据文章id查询
        Page<Comment> commentPage = this.page(new Page<>(page, size), new LambdaQueryWrapper<Comment>()
               .eq(Comment::getArticleId, articleId)
               .orderByDesc(Comment::getCreateTime));
        Page<CommentVO> commentVOPage = new Page<>();
        BeanUtils.copyProperties(commentPage, commentVOPage);
        List<CommentVO> commentVOList = new ArrayList<>();
        for (Comment comment : commentPage.getRecords()) {
            CommentVO commentVO = new CommentVO();
            BeanUtils.copyProperties(comment, commentVO);
            commentVO.setUserName(userService.getUserNameById(comment.getUserId()));
            commentVOList.add(commentVO);
        }
        commentVOPage.setRecords(commentVOList);
        return commentVOPage;
    }
}