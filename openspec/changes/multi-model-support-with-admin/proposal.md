## Why

当前项目仅支持 Qwen（大）模型通过硬编码的方式接入。虽然项目已引入 Spring AI 框架（支持 DashScope 和 Ollama），但缺乏统一的模型抽象层，导致无法在运行时动态切换不同供应商的模型，也无法通过后台管理系统配置各个智能体使用的大模型。

## What Changes

1. **创建模型抽象层**
   - 定义 `ModelProvider` 枚举，包含支持的模型供应商（DASHSCOPE、OLLAMA、DEEPSEEK、OPENAI、CLAUDE）
   - 创建 `ModelConfig` 配置类，统一管理各模型配置
   - 创建 `ModelService` 接口，定义模型切换的统一方法

2. **实现多模型支持**
   - 添加 OpenAI 等模型依赖到 pom.xml
   - 扩展 application.yml，添加多模型配置
   - 创建 `ModelRouter` 根据请求参数或配置选择对应的 ChatModel

3. **改造现有组件**
   - 修改 Manus、LoveApp、QuizAssistant、HealthAssistant 从硬编码改为动态获取 ChatModel
   - 修改 AiController 添加模型选择参数

4. **后台管理系统**
   - 创建数据库表：llm_config（模型配置）、vector_db_config（向量数据库配置）、agent_model_binding（智能体绑定）
   - 实现 CRUD 接口和前端页面

## Capabilities

### New Capabilities
- `multi-model-support`: 多模型动态切换支持，允许在运行时选择不同供应商的模型
- `model-admin`: 后台管理系统，支持配置大模型和向量数据库，以及绑定到各个智能体

### Modified Capabilities
- （无）现有功能需求不变

## Impact

- 后端：新增配置类、服务类、实体类、Mapper 类、Controller 接口
- 前端：新增后台管理页面
- 数据库：新增 3 张表
- 依赖：新增 spring-ai-openai-starter
