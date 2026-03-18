## Why

当前后端代码存在以下问题需要优化重构：

1. **安全问题**：硬编码的 API Key 存在于多处
2. **架构问题**：Agent 在 Controller 中直接 new 实例，未使用依赖注入
3. **代码质量问题**：测试方法未实现、线程安全问题、未使用的代码
4. **一致性问理**：命名不规范、类似功能接口不统一

## What Changes

1. **安全优化**
   - 移除硬编码的 API Key，改用环境变量或配置中心
   - API Key 敏感信息脱敏显示

2. **架构优化**
   - 将 AiController 中的 Agent 实例化改为依赖注入
   - 添加 Agent 工厂模式管理
   - 实现线程安全的 Agent 状态管理

3. **代码质量优化**
   - 实现 LlmConfigService.testConfig() 实际测试逻辑
   - 清理未使用的代码和注释掉的代码
   - 统一命名规范
   - 合并重复的 SSE 流式接口

4. **性能优化**
   - 添加必要的缓存注解
   - 优化数据库查询

## Capabilities

### New Capabilities
- （无）本次为优化重构，不添加新功能

### Modified Capabilities
- `agent-management`: 优化 Agent 管理，改用依赖注入
- `config-security`: 加强配置安全性

## Impact

- 修改 Controller、Service、Agent 相关代码
- 提升代码安全性、可维护性和性能
