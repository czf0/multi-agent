## ADDED Requirements

### Requirement: AI Controller API 文档
系统 SHALL 生成 AI Controller 的 API 文档，包含所有聊天接口的说明。

#### Scenario: AI 聊天接口文档
- **WHEN** 生成 AI Controller 文档
- **THEN** 输出包含以下端点的文档：
  - /ai/love_app/chat/* (恋爱助手接口)
  - /ai/manus/chat (超级智能体接口)
  - /ai/quiz/chat (答题助手接口)
  - /ai/health/chat (健康助手接口)

### Requirement: Qwen Controller API 文档
系统 SHALL 生成 Qwen Controller 的 API 文档。

#### Scenario: Qwen 模型接口文档
- **WHEN** 生成 Qwen Controller 文档
- **THEN** 输出包含以下端点的文档：
  - /api/qwen/chat (文本聊天)
  - /api/qwen/stream-chat (流式聊天)
  - /api/qwen/image (图像分析)
  - /api/qwen/video (视频分析)
  - /api/qwen/audio (音频分析)

### Requirement: 文件操作 API 文档
系统 SHALL 生成文件相关 API 文档。

#### Scenario: 文件接口文档
- **WHEN** 生成文件接口文档
- **THEN** 输出包含以下端点的文档：
  - /files/** (文件下载/浏览)

### Requirement: API 文档格式规范
系统 SHALL 按照统一格式输出每个 API 的说明。

#### Scenario: API 格式规范
- **WHEN** 输出每个 API 说明
- **THEN** 包含以下信息：
  - 请求路径 (Endpoint)
  - 请求方法 (Method)
  - 功能描述 (Description)
  - 请求参数 (如有)
  - 响应格式 (如有)
