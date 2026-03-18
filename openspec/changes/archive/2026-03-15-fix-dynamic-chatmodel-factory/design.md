## Context

当前 `DynamicChatModelFactory` 类在创建不同供应商的 ChatModel 时使用了不一致的接口：
- `createDashScopeChatModel` 返回 Spring AI 的 `DashScopeChatModel`
- `createOllamaChatModel` 返回 Spring AI 的 `OllamaChatModel`
- `createOpenAiChatModel` 返回 LangChain4j 的 `OpenAiChatModel`

这导致接口不统一，`LlmConfigServiceImpl.testConfig()` 方法调用时会因为类型不匹配而出现运行时错误。

## Goals / Non-Goals

**Goals:**
- 统一 `DynamicChatModelFactory` 中所有模型创建方法返回 Spring AI 的 `org.springframework.ai.chat.model.ChatModel` 接口
- 确保 DashScope、Ollama、OpenAI、DeepSeek、Claude 五种模型都能正常工作

**Non-Goals:**
- 不修改现有的 LLM 配置数据模型
- 不添加新的模型供应商支持
- 不修改 Agent 组件的业务逻辑

## Decisions

**决定使用 Spring AI 的 ChatModel 接口作为统一接口**

原因：
1. 项目主要使用 Spring AI 框架（DashScope、Ollama 都使用 Spring AI）
2. Spring AI 提供了更丰富的模型支持和配置选项
3. LangChain4j 的 OpenAiChatModel 与 Spring AI 的模型接口不兼容

**实现方案：**
1. 将 `createOpenAiChatModel` 方法的返回类型从 `dev.langchain4j.model.openai.OpenAiChatModel` 改为 Spring AI 的 `org.springframework.ai.openai.OpenAiChatModel`
2. 更新方法内部实现，使用 Spring AI 的 `OpenAiChatModel.builder()` 创建实例
3. 移除对 LangChain4j 相关类的 import

## Risks / Trade-offs

**[风险]** 运行时兼容性问题
→ **缓解措施**：在修改后通过测试验证所有模型供应商都能正常工作

**[风险]** API 差异
→ **缓解措施**：Spring AI 的 OpenAiChatModel 与 LangChain4j 的接口略有差异，需要调整温度等参数的设置方式
