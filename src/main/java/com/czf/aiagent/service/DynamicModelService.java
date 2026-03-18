package com.czf.aiagent.service;

import com.czf.aiagent.entity.Agent;
import com.czf.aiagent.entity.LlmConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 动态模型服务 - 从数据库配置获取ChatModel
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DynamicModelService {

    private final LlmConfigService llmConfigService;
    private final AgentService agentService;
    private final DynamicChatModelFactory chatModelFactory;

    /**
     * 获取智能体的默认ChatModel（通过agentName查询agent获取绑定的llmConfigId）
     */
    public ChatModel getChatModelForAgent(String agentName) {
        // 直接从Agent获取绑定的LLM配置ID
        Agent agent = agentService.getByName(agentName);
        if (agent == null) {
            log.warn("Agent not found: {}", agentName);
            return null;
        }

        Long llmConfigId = agent.getLlmConfigId();
        if (llmConfigId == null) {
            log.warn("No LLM config bound to agent: {}", agentName);
            return null;
        }

        // 获取LLM配置
        LlmConfig config = llmConfigService.getById(llmConfigId);
        if (config == null || !config.getEnabled()) {
            log.warn("LLM config not found or disabled: {}", llmConfigId);
            return null;
        }

        // 创建ChatModel
        return chatModelFactory.createChatModel(config);
    }

    /**
     * 获取指定ID的ChatModel
     */
    public ChatModel getChatModelByConfigId(Long configId) {
        LlmConfig config = llmConfigService.getById(configId);
        if (config == null || !config.getEnabled()) {
            log.warn("LLM config not found or disabled: {}", configId);
            return null;
        }

        return chatModelFactory.createChatModel(config);
    }

    /**
     * 获取指定智能体名称和配置ID的ChatModel
     */
    public ChatModel getChatModelForAgent(String agentName, Long configId) {
        if (configId != null) {
            return getChatModelByConfigId(configId);
        }
        return getChatModelForAgent(agentName);
    }

    /**
     * 获取智能体可用的LLM配置列表
     */
    public List<LlmConfig> getAvailableConfigsForAgent(String agentName) {
        return llmConfigService.listEnabled();
    }

    /**
     * 刷新配置缓存
     */
    public void refreshConfig(Long configId) {
        chatModelFactory.clearCache(configId);
        log.info("Cache refreshed for config: {}", configId);
    }

    /**
     * 清空所有缓存
     */
    public void clearAllCache() {
        chatModelFactory.clearCache();
        log.info("All ChatModel cache cleared");
    }
}
