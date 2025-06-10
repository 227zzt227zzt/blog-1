package com.zzt.blog.config;

import okhttp3.OkHttpClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author 227
 */
@Configuration
public class AiConfig {
    @Bean
    public OpenAiChatModel openAiChatModel() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(30))
                .readTimeout(Duration.ofSeconds(60))
                .writeTimeout(Duration.ofSeconds(60))
                .build();

        return new OpenAiChatModel(new OpenAiApi("https://api.deepseek.com","sk-ba2596c1b95e4c6e8f27cb3d735dcafe"));
    }
}
