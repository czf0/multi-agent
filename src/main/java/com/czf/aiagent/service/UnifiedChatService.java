package com.czf.aiagent.service;

import reactor.core.publisher.Flux;

/**
 * 统一对话服务接口
 */
public interface UnifiedChatService {

    /**
     * 统一对话接口
     * @param agentId 智能体ID
     * @param message 用户消息
     * @param chatId 会话ID
     * @param configId 可选的LLM配置ID（覆盖默认配置）
     * @return 流式响应
     */
    Flux<String> chat(Long agentId, String message, String chatId, Long configId);
}
