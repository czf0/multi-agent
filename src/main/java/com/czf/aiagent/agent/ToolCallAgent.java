package com.czf.aiagent.agent;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.czf.aiagent.agent.enums.AgentStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.tool.ToolCallback;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 工具调用代理，实现think和act方法处理工具调用
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class ToolCallAgent extends ReActAgent {

    private final ToolCallback[] availableTools;
    private final ToolCallingManager toolCallingManager;
    private final ChatOptions chatOptions;
    private ChatResponse toolCallChatResponse;
    private final String terminateToolName = "doTerminate";

    public ToolCallAgent(ToolCallback[] availableTools) {
        super();
        this.availableTools = availableTools;
        this.toolCallingManager = ToolCallingManager.builder().build();
        // 禁用Spring AI内置工具调用，自行管理
        this.chatOptions = DashScopeChatOptions.builder()
                .withProxyToolCalls(true)
                .build();
    }

    /**
     * 思考阶段：分析当前状态并确定要调用的工具
     */
    @Override
    public boolean think() {
        // 添加下一步提示词
        if (StrUtil.isNotBlank(getNextStepPrompt())) {
            getMessageList().add(new UserMessage(getNextStepPrompt()));
        }

        // 调用AI获取工具选择
        try {
            Prompt prompt = new Prompt(getMessageList(), this.chatOptions);
            ChatResponse chatResponse = getChatClient().prompt(prompt)
                    .system(getSystemPrompt())
                    .tools(availableTools)
                    .call()
                    .chatResponse();

            this.toolCallChatResponse = chatResponse;

            // 解析响应
            AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
            List<AssistantMessage.ToolCall> toolCallList = assistantMessage.getToolCalls();

            // 记录选择
            String result = assistantMessage.getText();
            log.info("{} thinking: {}", getName(), result);
            log.info("{} called {} tools", getName(), toolCallList.size());

            if (!toolCallList.isEmpty()) {
                String toolCallInfo = toolCallList.stream()
                        .map(tc -> String.format("Tool: %s, Parameter: %s", tc.name(), tc.arguments()))
                        .collect(Collectors.joining("\n"));
                log.info(toolCallInfo);
            }

            // 没有工具调用时记录助手消息
            if (toolCallList.isEmpty()) {
                getMessageList().add(assistantMessage);
                return false;
            }

            return true;
        } catch (Exception e) {
            log.error("{} occur mistake in the thinking process: {}", getName(), e.getMessage());
            getMessageList().add(new AssistantMessage("处理错误: " + e.getMessage()));
            return false;
        }
    }

    /**
     * 行动阶段：执行选定的工具
     */
    @Override
    public String act() {
        if (!toolCallChatResponse.hasToolCalls()) {
            return "No Tools needed to call";
        }

        // 执行工具调用
        Prompt prompt = new Prompt(getMessageList(), this.chatOptions);
        ToolExecutionResult result = toolCallingManager.executeToolCalls(prompt, toolCallChatResponse);

        // 更新消息上下文
        setMessageList(result.conversationHistory());
        ToolResponseMessage response = (ToolResponseMessage) CollUtil.getLast(result.conversationHistory());

        // 检查是否调用了终止工具
        boolean terminated = response.getResponses().stream()
                .anyMatch(r -> r.name().equals(terminateToolName));
        if (terminated) {
            setState(AgentStatusEnum.FINISHED);
        }

        // 格式化结果
        String results = response.getResponses().stream()
                .map(r -> "Call Tool[" + r.name() + "], Result: " + r.responseData())
                .collect(Collectors.joining("\n"));
        log.info(results);

        return results;
    }
}
