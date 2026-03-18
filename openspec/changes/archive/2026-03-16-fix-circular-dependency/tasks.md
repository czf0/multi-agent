## 1. 修复循环依赖

- [x] 1.1 修改 DynamicChatModelFactory，在构造函数中对 LlmConfigService 添加 @Lazy 注解
- [x] 1.2 添加 @Lazy 依赖导入

## 2. 测试验证

- [x] 2.1 启动应用验证是否还有循环依赖错误
- [x] 2.2 验证启动时LLM模型加载功能正常
