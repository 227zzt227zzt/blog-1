package com.zzt.blog.controller;

import com.zzt.blog.service.AiService;
import com.zzt.blog.util.Result;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author 227
 */
@RestController
@RequestMapping("/ai")
public class AiChatController {
    @Autowired
    private AiService aiChatService;
    @GetMapping(value = "/chatStream", produces = "text/html;charset=UTF-8")
    public Flux<String> chatStream(@RequestParam(value = "message",defaultValue = "你好，你是谁，你要干什么") String message){
        return aiChatService.getAiAnswer(message);
    }
    @Autowired
    private ChatModel  chatModel;
    @GetMapping(value = "/chat2",produces = "text/html;charset=UTF-8")
    public Flux<String> chat2(@RequestParam("message") String message) {
        return  chatModel.stream(message);
    }

}
