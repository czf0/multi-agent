# 基于Spring AI和LangChain4j构建的智能AI交互系统，支持多种大模型接入、知识库问答和工具调用能力。

## 项目简介

AI Agent是一个以AI开发为核心的智能体项目，旨在提供强大的AI交互能力，包括：

- 多轮对话与上下文记忆
- 知识库问答 (RAG)
- 工具调用与智能规划
- MCP服务集成
- 多模态应用支持

项目集成了主流AI大模型，如阿里云DashScope的通义千问、Ollama本地部署模型等，同时支持多种AI框架，使开发者能够快速构建智能应用。

## 技术栈

- **基础框架**: Java 21 + Spring Boot 3.4.5
- **AI框架**: Spring AI + LangChain4j
- **AI模型接入**:
    - 阿里云DashScope (通义千问)
    - Ollama (本地部署模型)
- **工具与库**:
    - Knife4j (API文档)
    - Hutool (工具类库)
    - Lombok (代码简化)
- **服务配置**: 支持多环境配置 (local, dev, prod)

## 核心功能

### 1. 多种方式调用AI大模型

- Spring AI框架集成
- LangChain4j链式调用
- 原生SDK调用
- 自定义HTTP调用

### 2. 知识库问答 (RAG)

- 支持接入本地知识库
- 支持向量数据库检索
- 提供上下文增强查询

### 3. 智能工具调用

- 文件操作
- 联网搜索
- 网页抓取
- 资源下载
- PDF生成

### 4. MCP服务能力

- 自定义MCP服务开发
- 图片搜索服务
- 支持多种调用方式

### 5. 智能体规划能力

- 基于ReAct模式的自主规划
- 多步骤任务执行
- 任务状态追踪

## 网页展示
![](https://gitee.com/licanfu/ai_-superintelligent_agent-image/blob/master/%E9%A6%96%E9%A1%B5.PNG)
![](https://gitee.com/licanfu/ai_-superintelligent_agent-image/blob/master/%E6%81%8B%E7%88%B1%E5%8A%A9%E6%89%8B%E9%A1%B5%E9%9D%A2.PNG)
## 目录结构（后端）
```
ai-agent/
├─ src/
│  ├─ main/java/com/licanfu/aiagent/...   # 后端代码（controller/agent/service/config/...）
│  ├─ main/resources/
│  │  ├─ application.yml                  # 主配置
│  │  ├─ mcp-servers.json                 # MCP 客户端配置
│  │  ├─ prohibited-words.txt             # 敏感词/限制词
│  │  └─ document/                        # 知识库文档（RAG）
│  └─ test/java/...                       # 测试
├─ sql/ai.sql                             # 数据库初始化脚本（如需）
├─ ai-agent-frontend/                     # 前端工程（Vue 3）
└─ image-search-mcp-server/               # MCP Server 示例模块
```

## 前后端对接要点
- 前端默认把 API 指向 http://localhost:8102/api （见 ai-agent-frontend/src/services/api.js 的 API_BASE_URL）
- 后端需监听 8102 端口并提供 /api/** 路由
- 前端会识别后端返回的 PDF/HTML 链接并渲染“打开文档”按钮；本地开发时 ChatMessage.vue 会把 /api/files/... 拼成 http://localhost:8102/...，生产请保证域名/端口一致或修改该组件逻辑
- 生产环境经 Nginx 反代时，务必按前端 README 的 SSE 配置进行代理（proxy_http_version 1.1、proxy_buffering off、proxy_read_timeout 等）

## 环境准备
- JDK 21+
- Maven 3.8+
- 可选：本地数据库（MySQL 或 PostgreSQL，项目内存在双数据源支持）

## 模型与自动装配（DashScope 推荐）
项目已引入 Spring AI 的 DashScope 自动装配能力。要启用：

1) 在 `application.yml`（或 `application-local.yml`）开启 DashScope 并设置 API Key：
```yaml
server:
  port: 8102

spring:
  ai:
    chat:
      client:
        enabled: true           # 开启 ChatClient 自动装配
    dashscope:
      chat:
        enabled: true           # 开启 DashScope Chat 模型
        options:
          model: qwen-turbo     # 模型名称，可按需调整（如 qwen-plus、qwen-max 等）
      api-key: ${DASHSCOPE_API_KEY:}  # 从环境变量或直接填写
```
> 说明：条件评估日志中出现 “ChatClientAutoConfiguration#chatClientBuilder matched / DashScopeAutoConfiguration#dashscopeChatModel matched - did not find any beans” 并不是报错，表示“若你未自定义同类 Bean，则自动装配会创建它们”。只要 `enabled=true` 且 API Key 正确，`DashScopeChatModel` 和 `ChatClient.Builder` 会被容器提供。

2) 可选：Ollama 本地模型（如需）
```yaml
spring:
  ai:
    ollama:
      chat:
        enabled: false          # 如需本地模型，改为 true
        options:
          model: llama3.1:8b    # 按需替换为你已拉取的模型名
      base-url: http://localhost:11434
```

## MCP 客户端与工具调用
- 已包含 `mcp-servers.json`（resources 根目录），用于声明可连接的 MCP 服务
- 启用同步 MCP 客户端：
```yaml
spring:
  ai:
    mcp:
      client:
        enabled: true
        type: SYNC
```
- 自动装配会把 MCP 工具暴露为 `ToolCallback[]`，供智能体（如 Manus/LoveApp 等）选择调用

## 数据库（可选）
项目包含 MyBatis-Plus 与双数据源（MySQL、PostgreSQL）支持。如果你仅使用 MySQL：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ai?useSSL=false&serverTimezone=UTC
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
```
- 如你的工程内自定义了双数据源（`mysqlDataSource` 与 `postgresDataSource`），请确保至少一个可用；不使用的可在对应 `@Configuration` 中关闭或在配置文件移除相关属性
- 初始化数据（可选）：导入 `sql/ai.sql`

## 运行方式（Windows cmd）
- 本地启动（使用默认 profile）：
```cmd
mvnw.cmd spring-boot:run
```
- 指定 profile（如 local）：
```cmd
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
```
- 打包并运行：
```cmd
mvnw.cmd clean package -DskipTests
java -jar target\ai-agent-*.jar --spring.profiles.active=local
```

启动后访问：
- Swagger/Knife4j 文档（按项目实际路由，一般为）：http://localhost:8102/api/swagger-ui.html

## 模型切换/自定义 ChatClient
- 使用自动装配：推荐按上文启用 `spring.ai.dashscope.*` 或 `spring.ai.ollama.*` 属性，即可获得 `ChatModel` 与 `ChatClient.Builder`
- 自定义：若你需要完全自定义 `ChatClient` 或替换底层模型，可在配置类中声明 `@Bean ChatClient.Builder` 或 `@Bean ChatModel`。当容器已有同类型 Bean 时，自动装配会让位（`@ConditionalOnMissingBean`）

## 和前端联调的小贴士
- 确保所有对话接口都在 `/api/**` 路径下，便于前端统一代理
- SSE 接口要设置响应头 `Content-Type: text/event-stream`、禁用缓存，超时调大；若经 Nginx 反代，参考前端 `nginx.conf` 中的 SSE 片段
- 前端在 `ChatMessage.vue` 中对 `localhost:8102` 有显式处理，若生产域名不同，建议同步调整拼接逻辑（或改为读取环境变量）

## 常见问题排查（Troubleshooting）
1) 条件评估报告出现：`ChatClientAutoConfiguration#chatClientBuilder matched: - @ConditionalOnMissingBean ... did not find any beans`
- 含义：不是错误，表示“若未自定义该 Bean，则自动装配会创建它”。只要相关 `enabled=true`，就会生成

2) DashScope 自动装配全部 matched，但实际没有拿到 `DashScopeChatModel`
- 检查：是否 `spring.ai.dashscope.chat.enabled=true`，`spring.ai.dashscope.api-key` 是否正确；若你自定义了同名 Bean，请确保未被 `@Primary`/同名覆盖

3) 启动失败：`BeanCreationException: Error creating bean with name 'loveApp': Injection of resource dependencies failed`
- 多为依赖注入失败：
  - LoveApp（或 Manus 等）构造器需要 `ChatModel dashscopeChatModel` 与 `ToolCallback[]`；若 `dashscopeChatModel` 因未开启 DashScope 或无 API Key 未被创建，会注入失败
  - 确认 MCP 客户端已启用（见上文 `spring.ai.mcp.client.*`），以便生成工具 `ToolCallback[]`
  - 如果你自定义了 `@Resource` 名称，请确保 Bean 名一致，或改用 `@Autowired`+`@Qualifier`

4) SSE 推流中断/无响应
- 经 Nginx 反代务必：`proxy_http_version 1.1`、`proxy_buffering off`、`proxy_read_timeout 600s`、`chunked_transfer_encoding off`
- 后端接口返回应为 `text/event-stream`，并定期发送 `\n\n` 保持连接

5) 数据库相关错误（多数据源）
- 条件日志中可见同时存在 `postgresDataSource` 与 `mysqlDataSource`。若你仅使用其中一个，请确保未启用另一个的配置，避免“无可用连接/连接失败”导致启动报错

## 开发与测试
- 单元测试：
```cmd
mvnw.cmd -q -Dtest=*Test test
```
- 建议为智能体的关键链路（解析 → 计划 → 工具调用 → 汇总）编写最小可运行的单测或集成测试

## 许可与贡献
- 许可证：MIT（如仓库未包含 LICENSE，请按需补充）
- 欢迎提交 Issue / PR：
  1) Fork 本仓库
  2) 新建分支：`git checkout -b feature/xxx`
  3) 提交并推送：`git commit -m "feat: ..."` → `git push origin feature/xxx`
  4) 发起 PR 并描述变更

---
若你准备上线生产环境，请重点校验：
- DashScope/Ollama 模型额度与稳定性
- Nginx 的 SSE 与缓存配置
- 数据源连接与连接池大小
- 敏感词/合规策略（`prohibited-words.txt`）
- 文件访问（/api/files/**）安全与鉴权
