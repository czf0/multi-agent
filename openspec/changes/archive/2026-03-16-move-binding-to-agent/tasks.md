## 1. 数据库和实体类

- [x] 1.1 修改 agent 表，添加 llm_config_id 和 vector_db_config_id 字段
- [x] 1.2 修改 Agent 实体类，添加 llmConfigId 和 vectorDbConfigId 字段

## 2. 后端服务修改

- [x] 2.1 修改 AgentService，添加获取绑定配置的方法
- [x] 2.2 修改 UnifiedChatService，直接从 Agent 获取绑定配置
- [x] 2.3 修改 AgentController，更新创建/更新智能体的接口

## 3. 删除旧绑定表相关代码

- [x] 3.1 创建数据迁移 SQL 脚本，将绑定数据迁移到 agent 表
- [x] 3.2 删除 AgentModelBinding 实体类
- [x] 3.3 删除 AgentModelBindingMapper 相关代码
- [x] 3.4 删除 AgentModelBindingService 相关代码
- [x] 3.5 删除 AdminController 中 agent_model_binding 相关接口

## 4. 前端修改

- [x] 4.1 修改 AgentConfigView.vue，添加绑定配置选择下拉框
- [x] 4.2 更新 api.js 中的 Agent 接口

## 5. 测试验证

- [x] 5.1 测试智能体 CRUD 接口
- [x] 5.2 测试绑定 LLM 功能
- [x] 5.3 测试统一对话接口
