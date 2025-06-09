package com.zzt.blog.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzt.blog.dto.SensitiveWordDTO;
import com.zzt.blog.entity.SensitiveWord;
import com.zzt.blog.mapper.SensitiveWordMapper;
import com.zzt.blog.service.SensitiveWordService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author 227
 */
@Service
public class SensitiveWordServiceImpl extends ServiceImpl<SensitiveWordMapper, SensitiveWord> implements SensitiveWordService {


    @Override
    public SensitiveWord getSensitiveWordById(Integer id) {
        return this.getById(id);
    }

    @Transactional
    @Override
    public void saveSensitiveWord(SensitiveWordDTO sensitiveWord) {
        if (this.lambdaQuery().eq(SensitiveWord::getWord, sensitiveWord.getWord()).exists()) {
            throw new RuntimeException("敏感词已存在");
        }
        SensitiveWord newSensitiveWord = new SensitiveWord();
        BeanUtils.copyProperties(sensitiveWord, newSensitiveWord);
        this.save(newSensitiveWord);
    }

    @Transactional
    @Override
    public void updateSensitiveWord(SensitiveWord sensitiveWord) {
        this.updateById(sensitiveWord);
    }

    @Transactional
    @Override
    public void deleteSensitiveWord(Integer id) {
        this.removeById(id);
    }

    @Override
    public String filterContent(String content) {
        List<SensitiveWord> words = this.list();
        for (SensitiveWord word : words) {
            String replacement = word.getReplaceWord() != null ? word.getReplaceWord() : "***";
            content = content.replaceAll(
                "(?i)" + Pattern.quote(word.getWord()),
                replacement
            );
        }
        return content;
    }

    @Override
    public Page<SensitiveWord> getSensitivePage(Integer page, Integer size) {
        Page<SensitiveWord> sensitivePage = new Page<>(page, size);
        return this.page(sensitivePage, null);
    }
}