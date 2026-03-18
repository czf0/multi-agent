## Context

当前项目中的智能体（Manus、QuizAssistant、HealthAssistant、LoveApp）是硬编码的Spring组件，每个都有独立的API接口。这种方式不够灵活，无法通过后台动态管理智能体及其绑定的LLM。

## Goals / Non-Goals

**Goals:**
- 创建智能体管理模块，支持动态配置智能体
- 提供统一的对话接口，根据智能体名称自动路由到对应LLM
- 支持智能体绑定LLM配置

**Non-Goals:**
- 不修改现有Agent组件的内部逻辑（Manus、QuizAssistant、HealthAssistant、LoveApp保持不变）
- 不删除已有的独立接口（保留兼容性）
- 不修改现有智能体的代码

## Decisions

**1. 数据库设计**

新建 `agent` 表存储智能体信息：
- id: 主键
- name: 智能体名称（唯一）
- description: 描述
- system_prompt: 系统提示词
- enabled: 是否启用
- create_time/update_time: 时间戳

沿用现有的 `agent_model_binding` 表建立智能体与LLM的绑定关系。

**2. 统一对话接口设计**

新增接口：`POST /ai/chat/{agentId}`

路由逻辑：
1. 根据 agentId 查询智能体是否存在
2. 查询智能体绑定的 LLM 配置
3. 如果指定了 configId，优先使用指定的 LLM
4. 使用 DynamicChatModelFactory 创建 ChatModel
5. 调用统一的对话处理逻辑

**3. 动态Agent vs 静态Agent**

采用混合模式（**不删除现有静态Agent**）：
- 现有的静态Agent（Manus、QuizAssistant、HealthAssistant、LoveApp）保留其独立接口和代码
- 新增的动态Agent通过统一接口 `/ai/chat/{agentId}` 访问
- 两者共存，互不影响

## Risks / Trade-offs

**[风险] 静态Agent如何与动态Agent统一**
→ **缓解措施**：静态Agent组件保持不变，新增动态Agent使用统一的通用处理逻辑

**[风险] 系统提示词的存储和加载**
→ **缓解措施**：在统一接口中，根据agentName加载对应的systemPrompt
