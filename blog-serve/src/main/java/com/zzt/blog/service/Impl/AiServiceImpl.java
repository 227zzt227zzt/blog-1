package com.zzt.blog.service.Impl;

import com.zzt.blog.service.AiService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * @author 227
 */
@Service
public class AiServiceImpl implements AiService {
    @Autowired
    private ChatClient chatClient;

    @Override
    public Flux<String> getAiAnswer(String question) {
       return chatClient.prompt().user(question).stream().content();
    }
}
