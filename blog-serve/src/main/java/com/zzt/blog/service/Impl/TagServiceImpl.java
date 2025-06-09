package com.zzt.blog.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzt.blog.entity.Tags;
import com.zzt.blog.mapper.TagMapper;
import com.zzt.blog.service.TagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 227
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tags> implements TagService {

    @Override
    public List<Tags> listTags() {
        return this.list();
    }

    @Override
    public Tags getTagById(Integer id) {
        return this.getById(id);
    }

    @Transactional
    @Override
    public void saveTag(Tags tag) {
        this.save(tag);
    }

    @Transactional
    @Override
    public void updateTag(Tags tag) {
        this.updateById(tag);
    }

    @Transactional
    @Override
    public void deleteTag(Integer id) {
        if (baseMapper.countArticlesByTag(id) > 0) {
            throw new RuntimeException("标签已被文章使用，无法删除");
        }
        this.removeById(id);
    }
}