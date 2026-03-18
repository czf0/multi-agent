## ADDED Requirements

### Requirement: DynamicChatModelFactory 统一使用 Spring AI ChatModel 接口

`DynamicChatModelFactory.doCreateChatModel` 方法 SHALL 返回统一的 `org.springframework.ai.chat.model.ChatModel` 接口实例，而不是混用 LangChain4j 和 Spring AI 的实现。

#### Scenario: DashScope 模型创建
- **WHEN** 调用 `createChatModel` 方法，传入 provider 为 "DASHSCOPE" 的 LlmConfig
- **THEN** 返回 `DashScopeChatModel` 实例，该实例实现 `org.springframework.ai.chat.model.ChatModel` 接口

#### Scenario: Ollama 模型创建
- **WHEN** 调用 `createChatModel` 方法，传入 provider 为 "OLLAMA" 的 LlmConfig
- **THEN** 返回 `OllamaChatModel` 实例，该实例实现 `org.springframework.ai.chat.model.ChatModel` 接口

#### Scenario: OpenAI 模型创建
- **WHEN** 调用 `createChatModel` 方法，传入 provider 为 "OPENAI" 的 LlmConfig
- **THEN** 返回 `OpenAiChatModel` 实例（来自 Spring AI），该实例实现 `org.springframework.ai.chat.model.ChatModel` 接口

#### Scenario: DeepSeek 模型创建
- **WHEN** 调用 `createChatModel` 方法，传入 provider 为 "DEEPSEEK" 的 LlmConfig
- **THEN** 返回使用 OpenAI 兼容接口的 `OpenAiChatModel` 实例（来自 Spring AI）

#### Scenario: Claude 模型创建
- **WHEN** 调用 `createChatModel` 方法，传入 provider 为 "CLAUDE" 的 LlmConfig
- **THEN** 返回使用 OpenAI 兼容接口的 `OpenAiChatModel` 实例（来自 Spring AI）

#### Scenario: 不支持的供应商
- **WHEN** 调用 `createChatModel` 方法，传入不支持的 provider
- **THEN** 记录错误日志并返回 null
