package com.czf.aiagent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czf.aiagent.entity.LlmConfig;
import com.czf.aiagent.mapper.LlmConfigMapper;
import com.czf.aiagent.service.DynamicChatModelFactory;
import com.czf.aiagent.service.LlmConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * LLM配置服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LlmConfigServiceImpl extends ServiceImpl<LlmConfigMapper, LlmConfig> implements LlmConfigService {

//    private final DynamicChatModelFactory dynamicChatModelFactory;

    @Override
    public List<LlmConfig> listEnabled() {
        return baseMapper.listEnabled();
    }

    @Override
    public List<LlmConfig> listByProvider(String provider) {
        return baseMapper.listByProvider(provider);
    }

    @Override
    public List<LlmConfig> listByAgentName(String agentName) {
        return baseMapper.listByAgentName(agentName);
    }
}
//    @Override
//    public String testConfig(LlmConfig config) {
//        try {
//            ChatModel chatModel = dynamicChatModelFactory.createChatModel(config);
//            if (chatModel == null) {
//                return "不支持的供应商: " + config.getProvider();
//            }
//
//            // Send a simple test message
//            String response = chatModel.call("你好，请回复'测试成功'");
//            log.info("Test config success: {} - {}", config.getName(), response);
//            return "测试成功! 响应: " + response;
//        } catch (Exception e) {
//            log.error("Test config failed: {} - {}", config.getName(), e.getMessage());
//            return "测试失败: " + e.getMessage();
//        }
//    }



