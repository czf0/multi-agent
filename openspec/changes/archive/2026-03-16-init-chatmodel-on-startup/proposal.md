## Why

当前 `DynamicChatModelFactory` 只在首次请求时才创建 ChatModel 实例，导致首次请求会有延迟，且如果配置有问题也无法在启动时发现。通过在项目启动时加载所有已配置的 LLM 模型，可以提前验证配置有效性，提升首次请求的响应速度。

## What Changes

- 在 `DynamicChatModelFactory` 中添加 `@PostConstruct` 初始化方法，应用启动时自动加载所有启用的 LLM 配置
- 创建 `ChatModelInitializer` 初始化器类，专门负责启动时的模型加载
- 添加启动加载结果的状态接口，供外部查询加载状态

## Capabilities

### New Capabilities
- `startup-model-loading`: 项目启动时自动加载已配置的 LLM 模型，提前验证配置有效性

### Modified Capabilities
- (无) 本次修改不涉及现有能力的需求变更

## Impact

- 新增 `ChatModelInitializer` 初始化器类
- 修改 `DynamicChatModelFactory` 添加初始化方法
- 影响应用启动时间（需要等待模型加载完成）
