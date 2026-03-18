## Context

LoveApp 使用 Spring AI 的 MessageChatMemoryAdvisor 来管理聊天历史。问题出现在有历史记录的会话时，可能原因包括：

1. ChatMemory 是共享实例，可能存在状态污染
2. MessageChatMemoryAdvisor 在处理历史消息时可能格式不正确
3. minimax API 对消息格式要求严格

## Goals / Non-Goals

**Goals:**
- 分析 400 错误的具体原因
- 修复 ChatMemory 导致的问题
- 确保多次对话正常工作

**Non-Goals:**
- 不修改其他智能体功能

## Decisions

**可能原因:**

1. **共享 ChatMemory 问题**: 当前 ChatMemory 是通过构造函数注入的单一实例，多个会话共享同一个实例可能导致状态混乱

2. **无效代码**: 第121行 `this.chatMemory.get(chatId, 3)` 获取了消息但未使用

3. **消息格式问题**: Spring AI 的 MessageChatMemoryAdvisor 在构建请求时可能存在问题

**修复方案:**

1. 移除无效的 chatMemory.get() 代码
2. 为每个会话创建独立的 ChatMemory 实例
3. 或者使用 Spring AI 推荐的方式管理聊天记忆
