## Context

当前项目使用 Spring AI 框架，已支持 DashScope（阿里云百练）和 Ollama，但各 Agent 组件（Manus、LoveApp、QuizAssistant、HealthAssistant）直接依赖 `@Qualifier("dashscopeChatModel")` 硬编码注入，无法动态切换模型。项目需要：
1. 统一的模型抽象层，支持运行时动态切换
2. 后台管理系统，配置各智能体使用的模型和向量数据库

## Goals / Non-Goals

**Goals:**
- 实现多模型动态切换（DashScope、Ollama、DeepSeek、OpenAI、Claude）
- 通过 API 参数或配置指定使用的模型
- 后台管理：配置大模型、向量化数据库、智能体绑定
- 保持向后兼容，现有 Qwen 功能正常工作

**Non-Goals:**
- 不实现模型的运行时注册/注销（配置后重启生效）
- 不实现复杂的模型负载均衡
- 不实现前端登录认证

## Decisions

### 1. 模型抽象层设计
**决策：** 使用 Spring AI 的 `ChatModel` 接口 + `ModelService` 抽象

**备选方案考虑：**
- 方案A（采用）：在 `ModelService` 中维护 `Map<String, ChatModel>`，通过 provider 或 modelName 路由
- 方案B：使用 Spring Cloud Gateway 做模型路由 —— 复杂度高，不适合本项目
- 方案C：每个 Agent 创建多个 ChatModel Bean —— 代码重复，维护困难

**理由：** Spring AI 的 `ChatModel` 是统一的模型接口，通过 Map 缓存不同供应商的实例最简单直接。

### 2. 模型切换方式
**决策：** 支持两种模式 - 配置驱动 + 请求参数

- 默认使用配置文件中的默认模型
- API 可通过 `provider` 和 `model` 参数覆盖

**备选：** 仅支持配置驱动 —— 不够灵活

### 3. 后台管理数据存储
**决策：** 使用 MySQL 存储配置（已有 MySQL 环境）

**备选方案考虑：**
- 方案A（采用）：MySQL + MyBatis-Plus
- 方案B：配置中心（Nacos）—— 引入额外依赖，本项目不需要

### 4. Agent 与模型绑定
**决策：** 通过 `agent_model_binding` 表关联 Agent 和 LLM/VectorDB 配置

**备选方案考虑：**
- 方案A（采用）：数据库存储绑定关系，支持运行时修改
- 方案B：硬编码在配置文件中 —— 不够灵活

## Risks / Trade-offs

- **[风险]** 不同模型的 API 兼容性
  - **缓解：** 通过 `ModelRouter` 统一适配，只暴露 `ChatModel` 接口

- **[风险]** 数据库配置变更后需要重启生效
  - **缓解：** 使用 Spring Bean 缓存，启动时加载，后续可通过管理接口刷新

- **[权衡]** 后台管理系统增加开发工作量
  - **理由：** 用户明确要求必须实现，否则无法满足实际使用需求

## Migration Plan

1. 新增数据库表（llm_config、vector_db_config、agent_model_binding）
2. 部署后端服务，验证 ModelService 正常工作
3. 测试各 Agent 使用不同模型
4. 部署前端，打开后台管理页面
5. 配置各 Agent 的默认模型

## Open Questions

- **Q1:** 是否需要支持模型热更新（修改配置不重启）？
  - 建议：暂不需要，后续迭代优化

- **Q2:** 前端是否需要权限管理？
  - 建议：暂不需要，后续迭代优化
