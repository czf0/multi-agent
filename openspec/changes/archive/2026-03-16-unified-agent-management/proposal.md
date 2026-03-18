## Why

当前项目中的智能体（Manus、QuizAssistant、HealthAssistant、LoveApp）是硬编码为Spring组件的，每个智能体有独立的API接口，无法通过后台动态管理。需求：将智能体纳入数据库管理，支持动态配置智能体及其绑定的LLM，并提供统一的对话接口，后端根据请求的智能体自动选择对应的大模型。

## What Changes

- 新增智能体管理表（agent）和对应的实体类、Mapper、Service
- 智能体支持配置名称、描述、系统提示词等属性
- **保留现有的智能体（Manus、QuizAssistant、HealthAssistant、LoveApp）及其接口不变**
- 新增统一的对话接口 `/ai/chat/{agentId}`，用于动态创建的智能体
- 后端根据 agentId 自动查询绑定的 LLM 配置并调用

## Capabilities

### New Capabilities
- `dynamic-agent-management`: 动态智能体管理，支持增删改查，绑定LLM
- `unified-chat-interface`: 统一对话接口，根据智能体名称自动路由到对应LLM

### Modified Capabilities
- (无) 本次修改不涉及现有能力的需求变更

## Impact

- 新增 `agent` 数据库表
- 新增 `Agent` 实体类、Mapper、Service、Controller
- 修改 `AiController` 添加统一对话接口
- 前端添加智能体管理页面
