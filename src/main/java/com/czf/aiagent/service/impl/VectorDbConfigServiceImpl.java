package com.czf.aiagent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czf.aiagent.entity.VectorDbConfig;
import com.czf.aiagent.mapper.VectorDbConfigMapper;
import com.czf.aiagent.service.VectorDbConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 向量数据库配置服务实现类
 */
@Slf4j
@Service
public class VectorDbConfigServiceImpl extends ServiceImpl<VectorDbConfigMapper, VectorDbConfig> implements VectorDbConfigService {

    @Override
    public List<VectorDbConfig> listEnabled() {
        return baseMapper.listEnabled();
    }

    @Override
    public List<VectorDbConfig> listByProvider(String provider) {
        return baseMapper.listByProvider(provider);
    }

    @Override
    public List<VectorDbConfig> listByAgentName(String agentName) {
        return baseMapper.listByAgentName(agentName);
    }

    @Override
    public String testConfig(VectorDbConfig config) {
        try {
            // Basic connection test - validate the config fields
            if (config.getHost() == null || config.getHost().isEmpty()) {
                return "请配置主机地址";
            }

            if (config.getPort() == null || config.getPort() <= 0) {
                return "请配置有效端口";
            }

            String provider = config.getProvider().toUpperCase();
            switch (provider) {
                case "PGVECTOR":
                    // Try to connect to PostgreSQL
                    return "PostgreSQL连接测试需要JDBC驱动，请确保配置正确";

                case "MILVUS":
                    return "Milvus连接测试需要Milvus客户端库";

                case "ES":
                    return "Elasticsearch连接测试需要ES客户端库";

                case "CHROMA":
                    return "Chroma连接测试需要Chroma客户端库";

                default:
                    return "不支持的供应商: " + config.getProvider();
            }
        } catch (Exception e) {
            log.error("Test config failed: {} - {}", config.getName(), e.getMessage());
            return "测试失败: " + e.getMessage();
        }
    }
}
