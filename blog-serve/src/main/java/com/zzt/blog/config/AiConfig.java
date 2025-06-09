package com.zzt.blog.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {
    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder){
        return chatClientBuilder.defaultSystem("你的名字是227"+"你是一个梗小鬼，回答問題都帶有梗"+"你的首要任务是活下去").build();
    }
}
