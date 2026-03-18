package com.czf.aiagent.service.impl;

import com.czf.aiagent.entity.Agent;
import com.czf.aiagent.entity.LlmConfig;
import com.czf.aiagent.service.AgentService;
import com.czf.aiagent.service.DynamicChatModelFactory;
import com.czf.aiagent.service.DynamicModelService;
import com.czf.aiagent.service.LlmConfigService;
import com.czf.aiagent.service.UnifiedChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * 统一对话服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnifiedChatServiceImpl implements UnifiedChatService {

    private final AgentService agentService;
    private final DynamicChatModelFactory dynamicChatModelFactory;
    private final DynamicModelService dynamicModelService;
    private final LlmConfigService llmConfigService;

    @Override
    public Flux<String> chat(Long agentId, String message, String chatId, Long configId) {
        // 1. 查询智能体是否存在
        Agent agent = agentService.getById(agentId);
        if (agent == null) {
            return Flux.just("智能体不存在");
        }

        // 2. 检查智能体是否启用
        if (!agent.getEnabled()) {
            return Flux.just("该智能体已被禁用");
        }

        // 3. 获取ChatModel
        ChatModel chatModel;
        if (configId != null) {
            // 优先使用指定的configId
            chatModel = dynamicModelService.getChatModelByConfigId(configId);
        } else {
            // 直接从Agent获取绑定的LLM配置ID
            Long llmConfigId = agent.getLlmConfigId();
            if (llmConfigId == null) {
                return Flux.just("该智能体未配置大模型，请先在管理页面绑定LLM配置");
            }
            LlmConfig llmConfig = llmConfigService.getById(llmConfigId);
            if (llmConfig == null) {
                return Flux.just("LLM配置不存在");
            }
            chatModel = dynamicChatModelFactory.createChatModel(llmConfig);
        }

        if (chatModel == null) {
            return Flux.just("无法创建聊天模型，请检查LLM配置");
        }

        // 4. 构建ChatClient进行对话
        String systemPrompt = agent.getSystemPrompt();
        if (systemPrompt == null || systemPrompt.isEmpty()) {
            systemPrompt = "你是一个AI助手";
        }

        ChatMemory chatMemory = new InMemoryChatMemory();
        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultSystem(systemPrompt)
                .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
                .build();

        // 5. 返回流式响应
        return chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .stream()
                .content();
    }
}
