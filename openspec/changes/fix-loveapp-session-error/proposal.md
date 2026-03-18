## Why

LoveApp在处理有历史记录的会话时（即第二次及之后的对话）报错 "400 BAD_REQUEST from POST https://api.minimaxi.com/v1/chat/completions"。第一次对话正常，说明问题出在聊天历史的处理上。

## What Changes

- 分析 LoveApp 中 ChatMemory 的使用方式
- 检查 MessageChatMemoryAdvisor 是否正确处理历史消息
- 修复导致 400 错误的问题

## Capabilities

### Modified Capabilities
- 恋爱助手聊天功能修复

## Impact

- LoveApp.java
- ChatMemory 相关配置
