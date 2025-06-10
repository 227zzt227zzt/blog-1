package com.zzt.blog.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Document(indexName = "chat_history")
@Data
public class ChatHistory {

    @Id
    private String id;
    
    @Field(type = FieldType.Keyword)
    private String sessionId;
    
    @Field(type = FieldType.Text)
    private String message;
    
    @Field(type = FieldType.Text)
    private String response;
    
    @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
    private Long timestamp = System.currentTimeMillis();

    // 构造器/getter/setter
}