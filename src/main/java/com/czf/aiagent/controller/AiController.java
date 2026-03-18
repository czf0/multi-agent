package com.czf.aiagent.controller;

/**
 * AI 智能体统一控制器
 * 提供各类AI应用的对话接口，包括恋爱大师、超级智能体、答题助手、健康助手等
 *
 * @author Jofend
 * @date 2024-01-01
 */


import com.czf.aiagent.agent.HealthAssistant;
import com.czf.aiagent.agent.ManusAssistant;
import com.czf.aiagent.agent.QuizAssistant;
import com.czf.aiagent.app.LoveApp;
import com.czf.aiagent.service.UnifiedChatService;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private LoveApp loveApp;

    @Resource
    private ManusAssistant manus;

    @Resource
    private QuizAssistant quizAssistant;

    @Resource
    private HealthAssistant healthAssistant;

    @Resource
    private UnifiedChatService unifiedChatService;

    /**
     * 同步调用 AI 恋爱大师应用
     *
     * @param message 用户消息
     * @param chatId 会话ID
     * @return AI 响应内容
     */
    @GetMapping("/love_app/chat/sync")
    public String doChatWithLoveAppSync(
            @RequestParam String message,
            @RequestParam String chatId) {
        return loveApp.doChat(message, chatId);
    }

    /**
     * SSE 流式调用 AI 恋爱大师应用
     *
     * @param message 用户消息
     * @param chatId 会话ID
     * @return 流式响应Flux
     */
    @GetMapping(value = "/love_app/chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithLoveAppSSE(
            @RequestParam String message,
            @RequestParam String chatId) {
        return loveApp.doChatByStream(message, chatId);
    }

    /**
     * SSE 流式调用 AI 恋爱大师应用 (ServerSentEvent)
     *
     * @param message 用户消息
     * @param chatId 会话ID
     * @return ServerSentEvent流式响应
     */
    @GetMapping(value = "/love_app/chat/server_sent_event")
    public Flux<ServerSentEvent<String>> doChatWithLoveAppServerSentEvent(
            @RequestParam String message,
            @RequestParam String chatId) {
        return loveApp.doChatByStream(message, chatId)
                .map(chunk -> ServerSentEvent.<String>builder()
                        .data(chunk)
                        .build());
    }

    /**
     * SSE 流式调用 AI 恋爱大师应用 (SseEmitter)
     *
     * @param message 用户消息
     * @param chatId 会话ID
     * @param configId 可选的LLM配置ID
     * @return SseEmitter对象
     * @throws IOException 如果发送消息失败
     */
    @GetMapping(value = "/love_app/chat/sse_emitter")
    public SseEmitter doChatWithLoveAppServerSseEmitter(
            @RequestParam String message,
            @RequestParam String chatId,
            @RequestParam(required = false) Long configId) {
        SseEmitter sseEmitter = new SseEmitter(180000L);
        loveApp.doChatByStream(message, chatId)
                .subscribe(chunk -> {
                    try {
                        sseEmitter.send(chunk);
                    } catch (IOException e) {
                        sseEmitter.completeWithError(e);
                    }
                }, sseEmitter::completeWithError, sseEmitter::complete);
        return sseEmitter;
    }

    /**
     * 流式调用 Manus 超级智能体
     *
     * @param message 用户消息
     * @param configId 可选的LLM配置ID
     * @return SseEmitter对象
     */
    @GetMapping("/manus/chat")
    public SseEmitter doChatWithManus(
            @RequestParam String message,
            @RequestParam(required = false) Long configId) {
        manus.runWithConfigId(configId);
        return manus.runStream(message);
    }

    /**
     * 流式调用 Quiz 智慧答题助手
     *
     * @param message 用户消息
     * @param configId 可选的LLM配置ID
     * @return SseEmitter对象
     */
    @GetMapping("/quiz/chat")
    public SseEmitter doChatWithQuiz(
            @RequestParam String message,
            @RequestParam(required = false) Long configId) {
        quizAssistant.runWithConfigId(configId);
        return quizAssistant.runStream(message);
    }

    /**
     * 流式调用 云医通健康助手
     *
     * @param message 用户消息
     * @param configId 可选的LLM配置ID
     * @return SseEmitter对象
     */
    @GetMapping("/health/chat")
    public SseEmitter doChatWithHealthAssistant(
            @RequestParam String message,
            @RequestParam(required = false) Long configId) {
        healthAssistant.runWithConfigId(configId);
        return healthAssistant.runStream(message);
    }

    /**
     * 统一对话接口 - 根据智能体ID自动选择对应的大模型
     *
     * @param agentId 智能体ID
     * @param message 用户消息
     * @param chatId 会话ID
     * @param configId 可选的LLM配置ID
     * @return SseEmitter对象
     */
    @GetMapping("/chat/{agentId}")
    public SseEmitter chatWithAgent(
            @PathVariable Long agentId,
            @RequestParam String message,
            @RequestParam String chatId,
            @RequestParam(required = false) Long configId) {
        SseEmitter sseEmitter = new SseEmitter(180000L);
        unifiedChatService.chat(agentId, message, chatId, configId)
                .subscribe(chunk -> {
                    try {
                        sseEmitter.send(chunk);
                    } catch (IOException e) {
                        sseEmitter.completeWithError(e);
                    }
                }, sseEmitter::completeWithError, sseEmitter::complete);
        return sseEmitter;
    }
}
