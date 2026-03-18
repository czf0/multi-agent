## Context

项目早期使用 LangChain4j 作为 AI 框架，当前需要统一替换为 Spring AI 框架。现已完成迁移，需要确认所有 LangChain4j 相关代码已被替换。

## Goals / Non-Goals

**Goals:**
- 确认所有 LangChain4j 依赖已从 pom.xml 中移除
- 确认所有 LangChain4j 导入已替换为 Spring AI
- 确保现有功能（Manus、LoveApp、QuizAssistant、HealthAssistant）正常工作

**Non-Goals:**
- 不修改业务逻辑
- 不添加新功能

## Decisions

**1. 依赖替换**

移除 LangChain4j 依赖，添加 Spring AI 依赖：
- spring-ai-alibaba-starter (DashScope)
- spring-ai-ollama-spring-boot-starter
- spring-ai-openai-spring-boot-starter

**2. 接口替换**

| LangChain4j | Spring AI |
|-------------|-----------|
| ChatLanguageModel | ChatModel |
| ChatClient | ChatClient |
| UserMessage | UserMessage |
| AiMessage | AiMessage |

## Risks / Trade-offs

- 需全面测试确保功能不受影响
