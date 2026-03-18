## Why

`DynamicChatModelFactory` 类当前混合使用了 LangChain4j 的 `dev.langchain4j.model.chat.ChatModel` 接口和 Spring AI 的 `org.springframework.ai.chat.model.ChatModel` 接口。这导致 `createOpenAiChatModel` 返回的是 LangChain4j 的 `OpenAiChatModel`，而其他方法返回的是 Spring AI 的模型，接口不统一，造成运行时类型转换错误。

## What Changes

- 将 `DynamicChatModelFactory` 中所有模型创建方法统一改为返回 Spring AI 的 `ChatModel` 接口
- 修改 `createOpenAiChatModel` 方法，使用 Spring AI 的 `OpenAiChatModel` 而不是 LangChain4j 的
- 更新相关的类型引用，确保整个项目使用统一的 AI 模型接口

## Capabilities

### New Capabilities
- `unified-chatmodel-interface`: 统一使用 Spring AI 的 ChatModel 接口，消除 LangChain4j 与 Spring AI 混用导致的不兼容问题

### Modified Capabilities
- (无) 本次修改不涉及现有能力的需求变更

## Impact

- 修改 `DynamicChatModelFactory.java` - 将 LangChain4j 的 `OpenAiChatModel` 替换为 Spring AI 的实现
- 影响 `LlmConfigServiceImpl.java` - 测试方法已使用正确的 Spring AI 接口，无需修改
- 消除运行时类型转换异常，确保 DashScope、Ollama、OpenAI 三种模型能正常工作
