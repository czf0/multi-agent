package com.czf.aiagent.agent;

import com.czf.aiagent.advisor.MyLoggerAdvisor;
import com.czf.aiagent.service.DynamicModelService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;

/**
 * 超级智能体，具备自主规划能力
 * @author JoFend
 */
@Component
public class ManusAssistant extends ToolCallAgent {

    private final DynamicModelService dynamicModelService;

    public ManusAssistant(ToolCallback[] allTools, DynamicModelService dynamicModelService) {
        super(allTools);

        // 基础配置
        this.setName("ManusAssistant");
        this.setMaxSteps(20);

        // 提示词设置
        this.setSystemPrompt(
            "You are LenManus, an all-capable AI assistant, aimed at solving any task presented by the user. " +
            "You have various tools at your disposal that you can call upon to efficiently complete complex requests."
        );

        this.setNextStepPrompt(
            "Based on user needs, proactively select the most appropriate tool or combination of tools. " +
            "For complex tasks, you can break down the problem and use different tools step by step to solve it. " +
            "After using each tool, clearly explain the execution results and suggest the next steps. " +
            "If you want to stop the interaction at any point, use the `terminate` tool/function call."
        );
        this.dynamicModelService = dynamicModelService;
        // 使用默认模型初始化
        initChatClient();
    }

    /**
     * 使用指定模型初始化
     */
    public void initChatClient() {
        ChatModel chatModel = dynamicModelService.getChatModelForAgent(getName());
        if (chatModel == null) {
            // 如果没有配置，使用内置的默认模型
            return;
        }

        this.setChatClient(
            ChatClient.builder(chatModel)
                .defaultAdvisors(new MyLoggerAdvisor())
                .build()
        );
    }

    /**
     * 使用指定配置ID运行
     */
    public void runWithConfigId(Long configId) {
        ChatModel chatModel = dynamicModelService.getChatModelByConfigId(configId);
        if (chatModel != null) {
            this.setChatClient(
                ChatClient.builder(chatModel)
                    .defaultAdvisors(new MyLoggerAdvisor())
                    .build()
            );
        }
    }
}
