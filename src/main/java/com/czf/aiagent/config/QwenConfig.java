package com.czf.aiagent.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 通义千问配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "qwen")
public class QwenConfig {

    /**
     * 通义千问API密钥
     * 支持从环境变量 QWEN_API_KEY 读取，或使用配置的默认值
     */
    @Value("${qwen.api-key:${QWEN_API_KEY:}}")
    private String apiKey;

    /**
     * 文本模型名称
     */
    private String textModel = "qwen-plus";

    /**
     * 视觉模型名称
     */
    private String visionModel = "qwen-vl-plus";

    /**
     * 视频模型名称
     */
    private String videoModel = "qwen-vl-max-latest";

    /**
     * 音频模型名称
     */
    private String audioModel = "qwen2-audio-instruct";

    /**
     * 是否开启流式输出
     */
    private boolean streamOutput = false;

    /**
     * 是否使用联网搜索
     */
    private boolean enableSearch = false;
}