## Context

当前 `DynamicChatModelFactory` 采用懒加载模式，只在首次请求时才创建 ChatModel 实例。这导致：
1. 首次请求响应延迟较大
2. 配置错误无法在启动时发现，只有在用户使用时才暴露
3. 无法提前预热模型

## Goals / Non-Goals

**Goals:**
- 项目启动时自动加载所有已启用的 LLM 配置
- 提前验证配置有效性，及时发现配置问题
- 提供加载状态查询接口

**Non-Goals:**
- 不修改现有的 Agent 组件逻辑
- 不修改数据库表结构

## Decisions

**决定使用 `@PostConstruct` 在 `DynamicChatModelFactory` 中实现初始化**

原因：
1. `DynamicChatModelFactory` 本身就是管理 ChatModel 的组件，最适合负责初始化
2. 利用依赖注入确保 LlmConfigService 已准备好
3. 实现简单，无需新增类

**备选方案：创建独立的 `ChatModelInitializer` 类**
- 优点：职责更分离
- 缺点：增加了复杂度，本项目不需要如此细粒度的分离

## 实现方案

1. 在 `DynamicChatModelFactory` 中注入 `LlmConfigService`
2. 添加 `@PostConstruct` 注解的 `initialize()` 方法
3. 调用 `llmConfigService.listEnabled()` 获取所有启用的配置
4. 遍历配置，调用 `createChatModel()` 创建并缓存实例
5. 添加加载状态跟踪，记录成功/失败数量

## Risks / Trade-offs

**[风险] 启动时间增加**
→ **缓解措施**：配置较多时可选异步加载，但同步加载更可靠

**[风险] 启动时网络问题导致加载失败**
→ **缓解措施**：捕获异常继续启动，提供重试接口
