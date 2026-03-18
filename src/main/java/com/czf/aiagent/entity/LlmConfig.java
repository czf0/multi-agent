package com.czf.aiagent.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * LLM配置实体类
 */
@Data
@TableName("llm_config")
public class LlmConfig {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 配置名称
     */
    private String name;

    /**
     * 供应商 (DASHSCOPE/OLLAMA/DEEPSEEK/OPENAI/CLAUDE)
     */
    private String provider;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * API Key
     */
    private String apiKey;

    /**
     * API Base URL
     */
    private String apiBaseUrl;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 温度
     */
    private Double temperature;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
