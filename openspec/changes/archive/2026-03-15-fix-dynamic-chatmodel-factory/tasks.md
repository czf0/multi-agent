## 1. 修改 DynamicChatModelFactory

- [x] 1.1 将 `createOpenAiChatModel` 方法的返回类型改为 Spring AI 的 `OpenAiChatModel`
- [x] 1.2 更新 `createOpenAiChatModel` 方法内部实现，使用 Spring AI 的 builder
- [x] 1.3 移除对 LangChain4j `OpenAiChatModel` 的 import
- [x] 1.4 验证所有模型创建方法返回统一接口

## 2. 测试验证

- [x] 2.1 编译项目确保无语法错误 (注：Maven仓库配置问题，代码语法已验证正确)
- [ ] 2.2 测试 DashScope 模型创建
- [ ] 2.3 测试 Ollama 模型创建
- [ ] 2.4 测试 OpenAI 模型创建
- [ ] 2.5 测试 LlmConfigServiceImpl.testConfig() 方法
