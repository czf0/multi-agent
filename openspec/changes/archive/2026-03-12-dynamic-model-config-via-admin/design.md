## Context

当前项目使用 Spring AI 框架，已支持 DashScope（阿里云百练）和 Ollama，但模型配置硬编码在 application.yml 中，每次修改都需要：
1. 修改 application.yml 配置文件
2. 修改环境变量
3. 重启应用

用户希望将配置存储在数据库中，通过后台管理界面动态配置，无需修改代码或重启。

## Goals / Non-Goals

**Goals:**
- 将大模型配置存储在 MySQL 数据库中
- 通过后台管理界面动态添加、修改、删除模型配置
- 支持配置测试（验证 API Key 是否有效）
- 支持智能体与模型的绑定配置
- 无需重启应用即可生效（或通过刷新按钮）

**Non-Goals:**
- 不实现模型的运行时自动发现
- 不实现复杂的模型负载均衡
- 不实现前端登录认证

## Decisions

### 1. 配置存储方案
**决策：** 使用 MySQL 数据库存储配置

**备选方案考虑：**
- 方案A（采用）：MySQL + MyBatis-Plus
- 方案B：Redis —— 需要额外依赖，且配置持久化更适合用 MySQL
- 方案C：Nacos 配置中心 —— 引入额外依赖

### 2. 动态模型实例创建
**决策：** 根据数据库配置动态创建 ChatModel 实例

Spring AI 的 ChatModel 实例可以通过编程方式创建：
- DashScope: `DashScopeChatModel.builder().apiKey(xxx).model(xxx).build()`
- Ollama: `OllamaChatModel.builder().baseUrl(xxx).model(xxx).build()`
- OpenAI: `OpenAiChatModel.builder().apiKey(xxx).model(xxx).build()`

每次请求时根据配置 ID 或名称获取对应的 ChatModel 实例。

### 3. 配置刷新机制
**决策：** 使用缓存 + 手动刷新

- 首次加载配置到内存缓存
- 提供刷新接口清空缓存并重新加载
- 或者使用定时任务定期刷新

### 4. 安全考虑
**决策：** API Key 加密存储

- 数据库中存储加密后的 API Key
- 使用 AES 对称加密
- 解密密钥存储在环境变量中

## Risks / Trade-offs

- **[风险]** 动态创建的 ChatModel 实例性能
  - **缓解：** 使用单例模式缓存已创建的实例，相同配置复用

- **[风险]** API Key 安全性
  - **缓解：** 加密存储，仅在需要时解密使用

- **[权衡]** 配置复杂度
  - **理由：** 通过后台界面管理灵活度更高，牺牲一定初始配置复杂度

## Migration Plan

1. 创建数据库表（llm_config、vector_db_config、agent_model_binding）
2. 实现实体类、Mapper、Service
3. 实现 Admin Controller
4. 实现动态模型服务
5. 前端页面开发
6. 测试配置 CRUD 和模型切换
7. 部署上线

## Open Questions

- **Q1:** 是否需要支持 API Key 的加密存储？
  - 建议：需要，确保安全性

- **Q2:** 首次启动时是否需要默认配置？
  - 建议：可以提供默认的 DashScope 配置作为内置配置
