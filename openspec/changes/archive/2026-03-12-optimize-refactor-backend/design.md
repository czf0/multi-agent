## Context

当前后端代码存在以下问题：

1. **安全问题**：硬编码 API Key
2. **架构问题**：Controller 中直接 new Agent 实例，未使用 DI
3. **代码质量**：TODO 方法未实现、线程安全、代码冗余

## Goals / Non-Goals

**Goals:**
- 移除硬编码 API Key，使用环境变量
- Agent 改用依赖注入，添加 AgentFactory
- 实现 testConfig 实际逻辑
- 清理冗余代码，统一命名规范
- 优化线程安全

**Non-Goals:**
- 不改变现有 API 接口
- 不添加新功能

## Decisions

### 1. API Key 安全
**决策：** 使用环境变量 + 配置类封装

```java
@Value("${qwen.api-key:${QWN_API_KEY:}}")
private String apiKey;
```

### 2. Agent 依赖注入
**决策：** 使用 @Autowired 注入 Agent Bean，添加 runWithModel 方法

### 3. Agent 工厂模式
**决策：** 创建 AgentFactory 统一管理 Agent 实例

## Risks / Trade-offs

- **[风险]** 修改可能影响现有功能
  - **缓解：** 保持 API 接口不变，只改内部实现

- **[权衡]** 重构 vs 进度
  - **理由：** 安全问题必须修复

## Migration Plan

1. 先修复安全问题
2. 重构 Agent 注入
3. 清理冗余代码
4. 测试验证
