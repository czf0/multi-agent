package com.czf.aiagent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czf.aiagent.entity.LlmConfig;

import java.util.List;

/**
 * LLM配置服务接口
 */
public interface LlmConfigService extends IService<LlmConfig> {

    /**
     * 获取启用的配置列表
     */
    List<LlmConfig> listEnabled();

    /**
     * 根据供应商获取配置列表
     */
    List<LlmConfig> listByProvider(String provider);

    /**
     * 根据智能体名称获取绑定的LLM配置
     */
    List<LlmConfig> listByAgentName(String agentName);

    /**
     * 测试LLM配置是否有效
     */
//    String testConfig(LlmConfig config);
}
