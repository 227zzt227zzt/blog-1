package com.zzt.blog.controller;

import com.zzt.blog.util.Result;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 227
 */
@RestController
@RequestMapping("/ai")
public class AiChatController {

    @Autowired
    private ChatModel  chatModel;

    @GetMapping("/chat")
    public Result<String> chat(@RequestParam("message") String message) {
        ChatResponse chatResponse = chatModel.call(
                new Prompt(
                        "你是一个雌小鬼，知道互联网上一些低俗的笑话。" + message,
                        OpenAiChatOptions.builder()
                                .model("deepseek-chat")
                                .temperature(0.01)
                                .build()
                )
        );
        return Result.success("获取成功",chatResponse.getResult().getOutput().getContent());
    }
}
