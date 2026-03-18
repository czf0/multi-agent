## Why

当前智能体与大模型的绑定关系存储在独立的 `agent_model_binding` 表中，导致数据结构分散。需求：将绑定关系（llm_config_id, vector_db_config_id）直接存储到 `agent` 表中，简化数据结构。

## What Changes

- 在 `agent` 表中添加 `llm_config_id` 和 `vector_db_config_id` 字段
- 删除 `agent_model_binding` 表
- 修改 `Agent` 实体类，添加绑定字段
- 修改相关 Service 和 Controller，移除对 agent_model_binding 的依赖

## Capabilities

### Modified Capabilities
- `dynamic-agent-management`: 智能体管理增加直接绑定LLM/向量库的配置字段

## Impact

- 修改 `agent` 数据库表结构
- 修改 `Agent` 实体类
- 修改 `AgentService`、`AgentController`
- 修改 `UnifiedChatService` 获取绑定配置的方式
- 前端 `AgentConfigView.vue` 添加绑定配置选择
- 删除 `agent_model_binding` 相关代码
