package com.czf.aiagent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czf.aiagent.entity.Agent;
import com.czf.aiagent.mapper.AgentMapper;
import com.czf.aiagent.service.AgentService;
import org.springframework.stereotype.Service;

/**
 * 智能体服务实现类
 */
@Service
public class AgentServiceImpl extends ServiceImpl<AgentMapper, Agent> implements AgentService {

    @Override
    public Agent getByName(String name) {
        return lambdaQuery().eq(Agent::getName, name).one();
    }
}
