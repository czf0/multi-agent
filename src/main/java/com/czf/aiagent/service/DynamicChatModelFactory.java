package com.czf.aiagent.service;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.czf.aiagent.entity.LlmConfig;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Lazy;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 动态ChatModel工厂 - 根据配置动态创建ChatModel实例
 */
@Slf4j
@Component
public class DynamicChatModelFactory {

    /**
     * ChatModel缓存 - 避免重复创建相同配置的实例
     */
    private final Map<String, ChatModel> chatModelCache = new ConcurrentHashMap<>();

    /**
     * 加载状态跟踪
     */
    private final List<LlmConfig> failedConfigs = new CopyOnWriteArrayList<>();
    /**
     * 记录项目启动时已加载的ChatModel数量
     */
    private int loadedCount = 0;
    private boolean initializationComplete = false;
    private final LlmConfigService llmConfigService;

    public DynamicChatModelFactory(@Lazy LlmConfigService llmConfigService) {
        this.llmConfigService = llmConfigService;
    }

    /**
     * 项目启动时初始化，加载所有已启用的LLM配置
     */
    @PostConstruct
    public void initialize() {
        log.info("Starting ChatModel initialization...");
        try {
            List<LlmConfig> enabledConfigs = llmConfigService.listEnabled();
            if (enabledConfigs == null || enabledConfigs.isEmpty()) {
                log.info("No enabled LLM configurations found");
                initializationComplete = true;
                return;
            }

            log.info("Found {} enabled LLM configurations", enabledConfigs.size());
            for (LlmConfig config : enabledConfigs) {
                try {
                    ChatModel chatModel = createChatModel(config);
                    if (chatModel != null) {
                        loadedCount++;
                        log.info("Successfully loaded ChatModel: {} - {}", config.getName(), config.getProvider());
                    } else {
                        failedConfigs.add(config);
                        log.warn("Failed to load ChatModel: {} - {}", config.getName(), config.getProvider());
                    }
                } catch (Exception e) {
                    failedConfigs.add(config);
                    log.error("Error loading ChatModel {}: {}", config.getName(), e.getMessage());
                }
            }

            log.info("ChatModel initialization complete. Loaded: {}, Failed: {}", loadedCount, failedConfigs.size());
        } catch (Exception e) {
            log.error("Failed to initialize ChatModels: {}", e.getMessage());
        } finally {
            initializationComplete = true;
        }
    }

    /**
     * 获取加载状态
     */
    public LoadingStatus getLoadingStatus() {
        return new LoadingStatus(initializationComplete, loadedCount, failedConfigs.size(),
                failedConfigs.stream().map(LlmConfig::getName).toList());
    }

    /**
     * 重试加载失败的配置
     */
    public int retryFailedModels() {
        if (failedConfigs.isEmpty()) {
            log.info("No failed configurations to retry");
            return 0;
        }

        log.info("Retrying {} failed configurations...", failedConfigs.size());
        int retryCount = 0;
        List<LlmConfig> configsToRetry = new ArrayList<>(failedConfigs);
        failedConfigs.clear();

        for (LlmConfig config : configsToRetry) {
            try {
                ChatModel chatModel = createChatModel(config);
                if (chatModel != null) {
                    loadedCount++;
                    retryCount++;
                    log.info("Successfully retried ChatModel: {} - {}", config.getName(), config.getProvider());
                } else {
                    failedConfigs.add(config);
                    log.warn("Failed to retry ChatModel: {} - {}", config.getName(), config.getProvider());
                }
            } catch (Exception e) {
                failedConfigs.add(config);
                log.error("Error retrying ChatModel {}: {}", config.getName(), e.getMessage());
            }
        }

        log.info("Retry complete. Success: {}, Still failed: {}", retryCount, failedConfigs.size());
        return retryCount;
    }

    /**
     * 加载状态记录
     */
    public record LoadingStatus(boolean complete, int loadedCount, int failedCount, List<String> failedNames) {
    }

    /**
     * 根据LLM配置创建ChatModel
     */
    public ChatModel createChatModel(LlmConfig config) {
        String cacheKey = buildCacheKey(config);

        // 先从缓存获取
        ChatModel cachedModel = chatModelCache.get(cacheKey);
        if (cachedModel != null) {
            log.debug("Using cached ChatModel for: {}", config.getName());
            return cachedModel;
        }

        // 根据供应商创建对应的ChatModel
        ChatModel chatModel = doCreateChatModel(config);

        // 放入缓存
        if (chatModel != null) {
            chatModelCache.put(cacheKey, chatModel);
            log.info("Created and cached ChatModel: {} - {}", config.getName(), config.getProvider());
        }

        return chatModel;
    }

    /**
     * 清空缓存
     */
    public void clearCache() {
        chatModelCache.clear();
        log.info("ChatModel cache cleared");
    }

    /**
     * 清空指定配置的缓存
     */
    public void clearCache(Long configId) {
        chatModelCache.entrySet().removeIf(entry ->
            entry.getKey().contains("_" + configId));
    }

    /**
     * 重新加载配置
     */
    public void reloadConfig(LlmConfig config) {
        String cacheKey = buildCacheKey(config);
        chatModelCache.remove(cacheKey);
        log.info("Removed cached ChatModel for reload: {}", config.getName());
    }

    private ChatModel doCreateChatModel(LlmConfig config) {
        String provider = config.getProvider().toUpperCase();

        try {
            return switch (provider) {
                case "DASHSCOPE" -> createDashScopeChatModel(config);
                case "OLLAMA" -> createOllamaChatModel(config);
                case "OPENAI" -> createOpenAiChatModel(config);
                case "DEEPSEEK" ->
                    // DeepSeek可以使用OpenAI兼容接口
                        createOpenAiChatModel(config);
                case "CLAUDE" ->
                    // Claude可以使用OpenAI兼容接口
                        createOpenAiChatModel(config);
                default -> {
                    log.error("Unsupported provider: {}", provider);
                    yield null;
                }
            };
        } catch (Exception e) {
            log.error("Failed to create ChatModel for {}: {}", provider, e.getMessage());
            return null;
        }
    }

    private DashScopeChatModel createDashScopeChatModel(LlmConfig config) {
        String apiKey = config.getApiKey();
        DashScopeChatOptions options = DashScopeChatOptions.builder()
                .withModel(config.getModelName())
                .withTemperature(Objects.isNull(config.getTemperature()) ? 0.7D : config.getTemperature())
                .build();
        return new DashScopeChatModel(new DashScopeApi(apiKey), options);
    }

    private OllamaChatModel createOllamaChatModel(LlmConfig config) {
        String baseUrl = config.getApiBaseUrl();
        OllamaApi ollamaApi = StringUtils.hasLength(baseUrl) ? new OllamaApi(baseUrl) : new OllamaApi();
        OllamaOptions options = OllamaOptions.builder()
                .model(config.getModelName())
                .temperature(Objects.isNull(config.getTemperature()) ? 0.7D : config.getTemperature())
                .build();
        return OllamaChatModel.builder().ollamaApi(ollamaApi).defaultOptions( options).build();
    }

    private OpenAiChatModel createOpenAiChatModel(LlmConfig config) {
        String baseUrl = config.getApiBaseUrl();
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = "https://api.openai.com/v1";
        }

        OpenAiApi openAiApi = OpenAiApi.builder()
                .apiKey(config.getApiKey())
                .baseUrl(baseUrl)
                .build();

        return OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(config.getModelName())
                        .temperature(Objects.isNull(config.getTemperature()) ? 0.7D : config.getTemperature())
                        .build())
                .build();
    }

    private String buildCacheKey(LlmConfig config) {
        return config.getProvider() + "_" + config.getModelName() + "_" + config.getId();
    }
}
