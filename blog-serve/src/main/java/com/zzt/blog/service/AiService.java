package com.zzt.blog.service;

import reactor.core.publisher.Flux;

public interface AiService {
    /**
     * 获取AI的回答
     * @param question 问题
     * @return 回答
     */
    Flux<String> getAiAnswer(String question);
}
