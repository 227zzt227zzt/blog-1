package com.zzt.blog.repository;

import com.zzt.blog.entity.ChatHistory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ChatHistoryRepository extends ElasticsearchRepository<ChatHistory, String> {

    // 按sessionId查询历史记录（自动按时间倒序）
    List<ChatHistory> findBySessionIdOrderByTimestampDesc(String sessionId);
}