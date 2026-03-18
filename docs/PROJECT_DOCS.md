# AI SuperIntelligent Agent - 项目技术文档

## 1. 项目概述

### 1.1 项目简介

AI SuperIntelligent Agent（多智能体 AI 应用）是一个基于 Spring Boot 3.4.5 + Vue 3 构建的多智能体 AI 应用平台。该项目集成了多种大语言模型（通义千问、Ollama），支持多 Agent 协作，可应用于智能对话、文件处理、知识检索等多种场景。

**项目定位**：面向开发者和企业的 AI 智能体开发框架，提供灵活的 Agent 定制能力和丰富的工具集。

### 1.2 技术架构

#### 后端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 21 | 运行环境 |
| Spring Boot | 3.4.5 | Web 框架 |
| Spring AI | 1.0.0-M6 | AI 框架 |
| LangChain4j | 1.0.0-beta2 | AI 编排框架 |
| MyBatis-Plus | 3.5.12 | ORM 框架 |
| PostgreSQL + PGVector | - | 数据库 + 向量存储 |
| Knife4j | 4.4.0 | API 文档 |

#### AI 模型接入

| 模型 | 提供商 | 说明 |
|------|--------|------|
| 通义千问 (Qwen) | 阿里云 DashScope | 默认模型，支持文本/图像/视频/音频 |
| Ollama | 本地部署 | 支持本地大模型 |

#### 前端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.2.13 | 前端框架 |
| Arco Design | 2.57.0 | UI 组件库 |
| Vue Router | 4.5.1 | 路由管理 |
| Axios | 1.9.0 | HTTP 客户端 |

### 1.3 核心功能

1. **多 Agent 系统**：支持 Manus（通用智能体）、LoveApp（恋爱助手）、QuizAssistant（答题助手）、HealthAssistant（健康助手）
2. **SSE 流式响应**：实时流式输出，提升用户体验
3. **RAG 知识增强**：基于向量数据库的知识检索增强
4. **MCP 集成**：支持 MCP 协议扩展工具能力
5. **多模态支持**：文本、图像、视频、音频多模态输入

---

## 2. 项目结构

### 2.1 目录结构

```
ai_superintelligent_agent/
├── pom.xml                                    # Maven 主配置
├── src/
│   └── main/
│       ├── java/com/czf/aiagent/
│       │   ├── controller/                    # REST API 控制器
│       │   │   ├── AiController.java           # AI Agent 接口
│       │   │   ├── QwenController.java         # 通义千问接口
│       │   │   └── FileResourceController.java # 文件操作接口
│       │   ├── agent/                         # AI Agent 实现
│       │   │   ├── Manus.java                  # 通用智能体
│       │   │   ├── QuizAssistant.java          # 答题助手
│       │   │   └── HealthAssistant.java        # 健康助手
│       │   ├── app/
│       │   │   └── LoveApp.java                # 恋爱助手
│       │   ├── tools/                         # 工具实现
│       │   │   ├── FileOperationTool.java      # 文件操作
│       │   │   ├── WebSearchTool.java         # 网页搜索
│       │   │   ├── WebScrapingTool.java       # 网页抓取
│       │   │   ├── ResourceDownloadTool.java  # 资源下载
│       │   │   ├── TerminalOperationTool.java # 终端操作
│       │   │   ├── PDFGenerationTool.java      # PDF生成
│       │   │   ├── HtmlGenerationTool.java    # HTML生成
│       │   │   ├── DateTimeTool.java           # 日期时间
│       │   │   ├── TerminateTool.java          # 终止对话
│       │   │   ├── ImageSearchTool.java        # 图像搜索
│       │   │   ├── DatabaseOperationTool.java  # 数据库操作
│       │   │   └── EmailSendingTool.java        # 邮件发送
│       │   ├── config/                         # 配置类
│       │   ├── service/                        # 业务服务
│       │   ├── rag/                            # RAG 实现
│       │   ├── chatmemory/                     # 聊天记忆
│       │   ├── advisor/                        # AI 顾问
│       │   └── domain/                         # 领域模型
│       └── resources/
│           ├── application.yml                 # 主配置
│           ├── mcp-servers.json                # MCP 配置
│           └── prohibited-words.txt           # 敏感词过滤
├── ai-agent-frontend/                         # Vue 前端
│   ├── src/
│   │   ├── main.js                             # 入口文件
│   │   ├── App.vue                             # 根组件
│   │   ├── router/index.js                     # 路由配置
│   │   ├── services/api.js                    # API 服务
│   │   ├── views/                             # 页面组件
│   │   │   ├── HomeView.vue                   # 首页
│   │   │   ├── LoveAppView.vue                # 恋爱助手页
│   │   │   ├── ManusAppView.vue               # 智能体页
│   │   │   ├── ExamAppView.vue                # 答题助手页
│   │   │   └── HealthAppView.vue              # 健康助手页
│   │   └── components/                        # 公共组件
│   └── package.json
└── image-search-mcp-server/                    # MCP 服务器子模块
```

---

## 3. API 接口文档

### 3.1 Base URL

```
后端：http://localhost:8102/api
前端：http://localhost:8080
```

### 3.2 AI Controller (/api/ai)

#### 恋爱助手接口

| 端点 | 方法 | 说明 |
|------|------|------|
| `/ai/love_app/chat/sync` | GET | 同步调用恋爱助手 |
| `/ai/love_app/chat/sse` | GET | SSE 流式调用恋爱助手 |
| `/ai/love_app/chat/server_sent_event` | GET | SSE ServerSentEvent 格式 |
| `/ai/love_app/chat/sse_emitter` | GET | SseEmitter 流式调用 |

**参数**：
- `message`: 用户消息 (必填)
- `chatId`: 会话 ID (可选)

#### 其他 AI Agent 接口

| 端点 | 方法 | 说明 |
|------|------|------|
| `/ai/manus/chat` | GET | Manus 超级智能体 |
| `/ai/quiz/chat` | GET | 智慧答题助手 |
| `/ai/health/chat` | GET | 云医通健康助手 |

**参数**：
- `message`: 用户消息 (必填)

### 3.3 Qwen Controller (/api/qwen)

| 端点 | 方法 | 说明 |
|------|------|------|
| `/api/qwen/chat` | POST | 文本对话 |
| `/api/qwen/stream-chat` | POST | 流式文本对话 |
| `/api/qwen/image` | POST | 图像分析对话 |
| `/api/qwen/video` | POST | 视频分析对话 |
| `/api/qwen/audio` | POST | 音频分析对话 |

#### 请求格式

**文本对话请求** (`QwenTextRequest`)：
```json
{
  "content": "用户消息内容",
  "model": "qwen-plus",        // 可选，默认 qwen-plus
  "systemPrompt": "系统提示词", // 可选
  "temperature": 0.7            // 可选，温度参数
}
```

**多模态请求** (`QwenMultiModalRequest`)：
```json
{
  "textContent": "问题描述",
  "imageUrls": ["https://..."],  // 图像 URL 列表
  "videoUrls": ["https://..."],  // 视频 URL 列表
  "audioUrl": "https://...",    // 音频 URL
  "model": "qwen-vl-plus"       // 视觉模型
}
```

**响应格式** (`QwenResponse`)：
```json
{
  "success": true,
  "statusCode": 200,
  "message": "success",
  "requestId": "xxx",
  "content": "模型回复内容"
}
```

### 3.4 File Controller (/api/files)

| 端点 | 方法 | 说明 |
|------|------|------|
| `/api/files/pdf/list` | GET | 获取 PDF 文件列表 |
| `/api/files/pdf/{filename}` | GET | 下载 PDF 文件 |
| `/api/files/html/list` | GET | 获取 HTML 文件列表 |
| `/api/files/html/{filename}` | GET | 查看 HTML 文件 |
| `/api/files/file/list` | GET | 获取一般文件列表 |
| `/api/files/file/{filename}` | GET | 下载一般文件 |

---

## 4. 组件参考

### 4.1 AI Agent 组件

#### Manus（通用智能体）
- **类路径**: `agent.com.czf.aiagent.Manus`
- **功能**: 基于 ReAct 模式的通用 AI 助手，支持工具调用
- **特点**: 可自主规划并调用多种工具完成复杂任务

#### LoveApp（恋爱助手）
- **类路径**: `app.com.czf.aiagent.LoveApp`
- **功能**: 情感咨询助手，10年经验恋爱专家
- **特点**: 使用非暴力沟通技巧，支持 RAG 知识库增强

#### QuizAssistant（答题助手）
- **类路径**: `agent.com.czf.aiagent.QuizAssistant`
- **功能**: 专业测评分析助手
- **特点**: 生成带图表的 PDF 测评报告，分析人格/能力/情绪

#### HealthAssistant（健康助手）
- **类路径**: `agent.com.czf.aiagent.HealthAssistant`
- **功能**: 健康信息助手
- **特点**: 基于循证医学知识，强调咨询专业医生

### 4.2 工具组件

| 工具类 | 功能说明 |
|--------|----------|
| `FileOperationTool` | 文件系统操作：读取、写入、删除文件 |
| `WebSearchTool` | 互联网搜索 |
| `WebScrapingTool` | 网页内容抓取 |
| `ResourceDownloadTool` | 远程资源下载 |
| `TerminalOperationTool` | 执行终端命令 |
| `PDFGenerationTool` | 生成 PDF 文档 |
| `HtmlGenerationTool` | 生成 HTML 文档 |
| `DateTimeTool` | 日期时间操作 |
| `TerminateTool` | 终止对话 |
| `ImageSearchTool` | 图像搜索（MCP 集成） |
| `DatabaseOperationTool` | 数据库操作 |
| `EmailSendingTool` | 邮件发送 |

### 4.3 前端组件

#### 路由配置

| 路径 | 组件 | 说明 |
|------|------|------|
| `/` | HomeView.vue | 首页，应用选择 |
| `/love-app` | LoveAppView.vue | AI 恋爱大师 |
| `/manus-app` | ManusAppView.vue | AI 超级智能体 |
| `/exam-app` | ExamAppView.vue | 智慧答题助手 |
| `/health-app` | HealthAppView.vue | 云医通健康助手 |

---

## 5. 组件关系图

```
┌─────────────────────────────────────────────────────────────────┐
│                        Frontend (Vue 3)                         │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌───────────┐  │
│  │ LoveAppView │ │ ManusAppView│ │ExamAppView  │ │HealthApp  │  │
│  └──────┬──────┘ └──────┬──────┘ └──────┬──────┘ └─────┬─────┘  │
│         │               │               │               │        │
│         └───────────────┴───────┬───────┴───────────────┘        │
│                                 │                                │
│                    ┌────────────▼────────────┐                   │
│                    │    services/api.js     │                   │
│                    │  (Axios + EventSource)  │                   │
│                    └────────────┬────────────┘                   │
└─────────────────────────────────┼────────────────────────────────┘
                                  │
                                  │ HTTP/SSE
                                  │
┌─────────────────────────────────┼────────────────────────────────┐
│                        Backend (Spring Boot)                    │
│                    ┌────────────▼────────────┐                   │
│                    │   AiController         │                   │
│                    │   QwenController       │                   │
│                    │   FileResourceController│                 │
│                    └────────────┬────────────┘                   │
│                                 │                                │
│         ┌───────────────────────┼───────────────────────┐        │
│         │                       │                       │        │
│  ┌──────▼──────┐    ┌───────────▼───────────┐   ┌──────▼──────┐  │
│  │  LoveApp    │    │      Manus           │   │   Tools     │  │
│  │  (Component)│    │   (ToolCallAgent)    │   │  (ToolCall) │  │
│  └──────┬──────┘    └───────────┬───────────┘   └─────────────┘  │
│         │                        │                                │
│  ┌──────▼──────────────┐  ┌─────▼──────────┐                    │
│  │  ChatMemory        │  │ ReActAgent     │                    │
│  │  (InMemory/File)   │  │ (Think + Act)  │                    │
│  └─────────────────────┘  └────────────────┘                    │
│                                                                 │
│  ┌─────────────────────────────────────────────────────────────┐ │
│  │                    AI Models                                 │ │
│  │  ┌─────────────┐  ┌──────────────┐  ┌───────────────────┐  │ │
│  │  │ DashScope   │  │   Ollama      │  │   MCP Servers     │  │ │
│  │  │ (Qwen)      │  │  (Local)     │  │ (Image Search)    │  │ │
│  │  └─────────────┘  └──────────────┘  └───────────────────┘  │ │
│  └─────────────────────────────────────────────────────────────┘ │
└──────────────────────────────────────────────────────────────────┘
```

---

## 6. 配置说明

### 6.1 后端配置 (application.yml)

```yaml
spring:
  application:
    name: multi-ai-agent
  profiles:
    active: local
  ai:
    dashscope:
      api-key: ${DASHSCOPE_API_KEY}  # 阿里云 DashScope API Key
  datasource:
    mysql:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/ai
      username: root
      password: 123456

server:
  port: 8102
  servlet:
    context-path: /api
```

### 6.2 环境变量

| 变量名 | 说明 | 必填 |
|--------|------|------|
| `DASHSCOPE_API_KEY` | 阿里云 DashScope API Key | 是 |

---

## 7. 运行指南

### 7.1 启动后端

```bash
# 使用 Maven 启动
mvnw.cmd spring-boot:run

# 指定 Profile
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
```

### 7.2 启动前端

```bash
# 安装依赖
cd ai-agent-frontend
npm install

# 启动开发服务器
npm run serve

# 构建生产版本
npm run build
```

### 7.3 访问地址

| 服务 | 地址 |
|------|------|
| 前端 | http://localhost:8080 |
| Swagger API 文档 | http://localhost:8102/api/swagger-ui.html |
| Knife4j 文档 | http://localhost:8102/api/doc.html |

---

## 8. 扩展开发

### 8.1 添加新的 Agent

1. 在 `agent/` 包下创建新的 Agent 类
2. 实现聊天逻辑，注入 `ChatModel` 和工具
3. 在 `AiController` 添加对应的端点

### 8.2 添加新的工具

1. 在 `tools/` 包下创建新的工具类
2. 使用 `@Tool` 注解标记工具方法
3. 在 `ToolRegistration` 中注册工具

### 8.3 添加新的前端页面

1. 在 `views/` 下创建 Vue 组件
2. 在 `router/index.js` 添加路由
3. 在 `services/api.js` 添加对应的 API 调用

---

## 9. 注意事项

1. **API Key 安全**：生产环境请使用环境变量管理敏感信息
2. **SSE 连接**：SSE 连接有超时限制，默认 3 分钟
3. **文件存储**：生成的文件默认保存在 `{user.home}/ai-agent-files/` 目录
4. **敏感词过滤**：系统内置敏感词过滤，请定期更新 `prohibited-words.txt`

---

*文档版本: 1.0*
*更新时间: 2026-03-12*
