## Context

当前智能体与大模型的绑定关系存储在独立的 `agent_model_binding` 表中，需要通过额外的查询才能获取智能体绑定的LLM配置。这种设计导致数据结构分散，增加了查询复杂度。

## Goals / Non-Goals

**Goals:**
- 将绑定关系直接存储到 `agent` 表中
- 简化数据结构，减少关联查询
- 保持现有功能不变

**Non-Goals:**
- 不修改现有静态Agent的行为

## Decisions

**1. 数据库设计**

在 `agent` 表中添加两个字段：
- `llm_config_id`: 绑定的LLM配置ID
- `vector_db_config_id`: 绑定的向量数据库配置ID

删除 `agent_model_binding` 表。

**2. 数据迁移**

提供SQL脚本，将现有绑定数据迁移到agent表中。

**3. 代码修改**

- 修改 `Agent` 实体类，添加 llmConfigId 和 vectorDbConfigId 字段
- 修改 `AgentService`，添加获取绑定配置的方法
- 修改 `UnifiedChatService`，直接从Agent获取绑定配置
- 删除 `agent_model_binding` 相关代码

## Risks / Trade-offs

**[风险] 数据迁移丢失**
→ **缓解措施**：先备份数据，执行迁移脚本后再删除旧表
