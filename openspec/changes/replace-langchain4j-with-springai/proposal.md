## Why

项目早期使用了 LangChain4j 作为AI框架，现需要统一替换为 Spring AI 框架，以保持技术栈一致性并简化依赖管理。Spring AI 提供了更统一的 API 来支持多种大模型供应商。

## What Changes

- 移除 pom.xml 中的 LangChain4j 依赖
- 添加 Spring AI 相关依赖（spring-ai-alibaba-starter, spring-ai-ollama-spring-boot-starter, spring-ai-openai-spring-boot-starter）
- 将所有 LangChain4j 的 ChatLanguageModel 接口替换为 Spring AI 的 ChatModel 接口
- 修改 DynamicChatModelFactory 使用 Spring AI 的模型创建方式
- 更新所有 Agent 类使用 Spring AI 的 ChatClient

## Capabilities

### Modified Capabilities
- `unified-chatmodel-interface`: 更新为使用 Spring AI 接口

## Impact

- pom.xml 依赖配置
- DynamicChatModelFactory 类
- 所有 Agent 类（Manus, LoveApp, QuizAssistant, HealthAssistant）
- UnifiedChatService 类
