## Why

当前项目的多模型配置通过 application.yml 配置文件实现，每次修改大模型配置（如 API Key、模型名称）都需要修改配置文件和重启应用，不够灵活。应该将模型配置存储在数据库中，通过后台管理界面动态配置，无需修改代码或重启服务。

## What Changes

1. **数据库存储模型配置**
   - 创建 llm_config 表存储大模型配置（供应商、模型名、API Key、API Base URL、优先级等）
   - 创建 vector_db_config 表存储向量数据库配置
   - 创建 agent_model_binding 表存储智能体与模型的绑定关系

2. **后台管理接口**
   - LLM 配置 CRUD 接口（增删改查）
   - 向量数据库配置 CRUD 接口
   - 智能体绑定配置接口
   - 配置测试接口（验证 API Key 是否有效）

3. **动态模型服务**
   - 从数据库加载模型配置
   - 支持运行时刷新配置（无需重启）
   - 动态创建 ChatModel 实例

4. **前端后台管理页面**
   - LLM 配置管理页面
   - 向量数据库配置管理页面
   - 智能体绑定配置页面

## Capabilities

### New Capabilities
- `dynamic-llm-config`: 动态大模型配置，配置存储在数据库中，支持后台管理界面修改
- `dynamic-vector-db-config`: 动态向量数据库配置，配置存储在数据库中
- `agent-model-binding`: 智能体与模型绑定，配置各智能体使用的模型

### Modified Capabilities
- （无）现有功能需求不变

## Impact

- 后端：新增实体类、Mapper、Service、Controller
- 前端：新增后台管理页面
- 数据库：新增 3 张表
