## Why

当前 `DynamicChatModelFactory` 需要注入 `LlmConfigService` 来获取LLM配置列表进行初始化，而 `LlmConfigServiceImpl` 在初始化过程中可能需要使用 `DynamicChatModelFactory`，导致循环依赖问题。需要解耦这两个类，避免Spring容器启动失败。

## What Changes

- 修改 `DynamicChatModelFactory`，使用 `@Lazy` 注解延迟注入 `LlmConfigService`
- 或者将 `DynamicChatModelFactory` 中的初始化逻辑移至单独的 `ModelInitializer` 类
- 确保应用启动时能够正常初始化

## Capabilities

### Modified Capabilities
- `startup-model-loading`: 修改启动时模型加载的实现方式，避免循环依赖

## Impact

- 修改 `DynamicChatModelFactory` 类
- 可能需要创建新的初始化类
