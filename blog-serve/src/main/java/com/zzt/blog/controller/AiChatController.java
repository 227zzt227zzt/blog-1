package com.zzt.blog.controller;

import com.zzt.blog.service.AiService;
import com.zzt.blog.util.Result;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import org.springframework.http.MediaType;

/**
 * AI Chat Controller for handling AI chat interactions
 * @author 227
 */
@RestController
@RequestMapping("/ai")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AiChatController {
    
    @Autowired
    private AiService aiChatService;
    
    @Autowired
    private ChatModel chatModel;

    /**
     * Stream chat response from AI service
     * @param message User's message
     * @return Flux of String containing AI's response
     */
    @GetMapping(value = "/chatStream", produces = "text/html;charset=UTF-8")
    public Flux<String> chatStream(@RequestParam(value = "message",defaultValue = "你好，你是谁，你要干什么") String message){
        return aiChatService.getAiAnswer(message);
    }

    /**
     * Alternative chat endpoint using ChatModel directly
     * @param message User's message
     * @return Flux of String containing AI's response
     */
    @GetMapping(value = "/chat2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat2(@RequestParam("message") String message) {
        try {
            return chatModel.stream(message);
        } catch (Exception e) {
            return Flux.error(e);
        }
    }
}
