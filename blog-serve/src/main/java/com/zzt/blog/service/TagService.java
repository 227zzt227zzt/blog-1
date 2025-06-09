package com.zzt.blog.service;

import com.zzt.blog.entity.Tags;
import java.util.List;

public interface TagService {
    List<Tags> listTags();
    Tags getTagById(Integer id);
    void saveTag(Tags tag);
    void updateTag(Tags tag);
    void deleteTag(Integer id);
}