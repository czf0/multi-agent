## MODIFIED Requirements

### Requirement: 应用启动时加载已配置的 LLM 模型
**From**: 项目启动时 SHALL 自动加载数据库中所有已启用的 LLM 配置，提前创建 ChatModel 实例并缓存。

**To**: 项目启动时 SHALL 自动加载数据库中所有已启用的 LLM 配置，提前创建 ChatModel 实例并缓存。使用 @Lazy 注解避免与 LlmConfigService 的循环依赖。

#### Scenario: 启动时加载所有启用的配置
- **WHEN** 应用启动完成
- **THEN** 系统自动调用 LlmConfigService.listEnabled() 获取所有启用的配置，并为每个配置创建 ChatModel 实例（通过 @Lazy 注入解决循环依赖）

#### Scenario: 跳过无效配置
- **WHEN** 某个 LLM 配置创建 ChatModel 失败时
- **THEN** 记录错误日志并跳过该配置，继续加载其他配置，不影响应用启动

#### Scenario: 提供加载状态查询
- **WHEN** 调用 getLoadingStatus() 方法
- **THEN** 返回包含加载状态、已加载数量、失败数量等信息的状态对象

#### Scenario: 启动完成后可重试加载失败的配置
- **WHEN** 调用 retryFailedModels() 方法
- **THEN** 重新尝试加载之前加载失败的模型配置
