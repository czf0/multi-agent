package com.czf.aiagent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czf.aiagent.entity.Agent;

/**
 * 智能体服务接口
 */
public interface AgentService extends IService<Agent> {

    /**
     * 根据名称获取智能体
     */
    Agent getByName(String name);
}
