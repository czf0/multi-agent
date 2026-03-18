package com.czf.aiagent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czf.aiagent.entity.LlmConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * LLM配置Mapper接口
 */
@Mapper
public interface LlmConfigMapper extends BaseMapper<LlmConfig> {

    /**
     * 获取启用的配置列表，按优先级排序
     */
    @Select("SELECT * FROM llm_config WHERE enabled = 1 ORDER BY priority DESC")
    List<LlmConfig> listEnabled();

    /**
     * 根据供应商获取配置列表
     */
    @Select("SELECT * FROM llm_config WHERE provider = #{provider} AND enabled = 1 ORDER BY priority DESC")
    List<LlmConfig> listByProvider(@Param("provider") String provider);

    /**
     * 根据智能体名称获取绑定的LLM配置
     */
    @Select("SELECT lc.* FROM llm_config lc " +
            "INNER JOIN agent_model_binding amb ON lc.id = amb.llm_config_id " +
            "WHERE amb.agent_name = #{agentName} AND lc.enabled = 1 " +
            "ORDER BY amb.is_default DESC, lc.priority DESC")
    List<LlmConfig> listByAgentName(@Param("agentName") String agentName);
}
