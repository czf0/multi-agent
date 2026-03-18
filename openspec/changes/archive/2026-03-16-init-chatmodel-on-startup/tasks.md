## 1. 修改 DynamicChatModelFactory

- [x] 1.1 在 DynamicChatModelFactory 中注入 LlmConfigService
- [x] 1.2 添加 @PostConstruct 初始化方法，加载所有启用的 LLM 配置
- [x] 1.3 添加加载状态跟踪（成功数量、失败数量）
- [x] 1.4 添加 getLoadingStatus() 方法查询加载状态
- [x] 1.5 添加 retryFailedModels() 方法重试失败的配置

## 2. 测试验证

- [ ] 2.1 启动应用验证初始化是否执行
- [ ] 2.2 验证加载状态查询接口
- [ ] 2.3 验证重试功能
