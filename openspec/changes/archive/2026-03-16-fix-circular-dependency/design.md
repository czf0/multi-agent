## Context

当前 `DynamicChatModelFactory` 在 `@PostConstruct` 初始化方法中需要调用 `LlmConfigService.listEnabled()` 来获取LLM配置列表。如果 `LlmConfigServiceImpl` 在初始化过程中需要使用 `DynamicChatModelFactory`，会导致循环依赖问题，Spring容器无法启动。

## Goals / Non-Goals

**Goals:**
- 解决 `DynamicChatModelFactory` 和 `LlmConfigService` 之间的循环依赖
- 保持启动时加载LLM配置的功能不变

**Non-Goals:**
- 不修改已加载模型的业务逻辑

## Decisions

**1. 使用 @Lazy 注解延迟注入**

在 `DynamicChatModelFactory` 的构造函数中对 `LlmConfigService` 使用 `@Lazy` 注解，延迟注入解决循环依赖。

```java
public DynamicChatModelFactory(@Lazy LlmConfigService llmConfigService) {
    this.llmConfigService = llmConfigService;
}
```

**2. 替代方案：创建独立的初始化器类**

将启动初始化逻辑抽取到独立的 `ModelInitializer` 类中，该类只在所有Bean初始化完成后执行。

## Risks / Trade-offs

- 使用 `@Lazy` 可能导致运行时才发现依赖问题 → **缓解**：在测试环境充分验证启动流程
