package com.czf.aiagent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czf.aiagent.entity.Agent;
import org.apache.ibatis.annotations.Mapper;

/**
 * 智能体 Mapper 接口
 */
@Mapper
public interface AgentMapper extends BaseMapper<Agent> {
}
