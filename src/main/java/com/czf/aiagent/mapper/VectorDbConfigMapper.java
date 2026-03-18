package com.czf.aiagent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czf.aiagent.entity.VectorDbConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 向量数据库配置Mapper接口
 */
@Mapper
public interface VectorDbConfigMapper extends BaseMapper<VectorDbConfig> {

    /**
     * 获取启用的配置列表
     */
    @Select("SELECT * FROM vector_db_config WHERE enabled = 1")
    List<VectorDbConfig> listEnabled();

    /**
     * 根据供应商获取配置列表
     */
    @Select("SELECT * FROM vector_db_config WHERE provider = #{provider} AND enabled = 1")
    List<VectorDbConfig> listByProvider(@Param("provider") String provider);

    /**
     * 根据智能体名称获取绑定的向量数据库配置
     */
    @Select("SELECT vdc.* FROM vector_db_config vdc " +
            "INNER JOIN agent_model_binding amb ON vdc.id = amb.vector_db_config_id " +
            "WHERE amb.agent_name = #{agentName} AND vdc.enabled = 1")
    List<VectorDbConfig> listByAgentName(@Param("agentName") String agentName);
}
