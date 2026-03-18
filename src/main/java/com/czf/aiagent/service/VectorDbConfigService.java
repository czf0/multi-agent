package com.czf.aiagent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czf.aiagent.entity.VectorDbConfig;

import java.util.List;

/**
 * 向量数据库配置服务接口
 */
public interface VectorDbConfigService extends IService<VectorDbConfig> {

    /**
     * 获取启用的配置列表
     */
    List<VectorDbConfig> listEnabled();

    /**
     * 根据供应商获取配置列表
     */
    List<VectorDbConfig> listByProvider(String provider);

    /**
     * 根据智能体名称获取绑定的向量数据库配置
     */
    List<VectorDbConfig> listByAgentName(String agentName);

    /**
     * 测试向量数据库配置是否有效
     */
    String testConfig(VectorDbConfig config);
}
