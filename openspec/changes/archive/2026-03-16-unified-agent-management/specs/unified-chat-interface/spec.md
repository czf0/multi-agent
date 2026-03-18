## ADDED Requirements

### Requirement: 统一对话接口

系统 SHALL 提供统一的对话接口，根据智能体ID自动选择对应的大模型进行处理。

#### Scenario: 通过统一接口发送消息
- **WHEN** 用户调用 POST /ai/chat/{agentId}，传入消息内容和会话ID
- **THEN** 系统根据 agentId 查询绑定的 LLM 配置，创建 ChatModel，返回流式响应

#### Scenario: 智能体未绑定 LLM
- **WHEN** 用户向未绑定 LLM 的智能体发送消息
- **THEN** 系统返回错误提示"该智能体未配置大模型"

#### Scenario: 智能体不存在
- **WHEN** 用户向不存在的智能体发送消息
- **THEN** 系统返回错误提示"智能体不存在"

#### Scenario: 使用默认 LLM
- **WHEN** 智能体已配置默认 LLM 且请求中未指定 configId
- **THEN** 系统使用绑定的默认 LLM 进行对话

#### Scenario: 覆盖使用指定 LLM
- **WHEN** 请求中指定了 configId
- **THEN** 系统优先使用指定的 configId 对应的 LLM，忽略智能体绑定的默认配置
