## Why

LoveApp在第二次对话时（有历史记录）仍然报400错误。第一次对话正常，说明问题出在历史消息的处理上。可能原因包括：数据库中存储的消息格式与API要求不兼容，或者MessageChatMemoryAdvisor在构建请求时存在兼容性问题。

## What Changes

- 深入调查400错误的具体原因
- 检查MybatisPlusChatMemory消息存储/检索逻辑
- 检查MessageChatMemoryAdvisor与minimax API的兼容性
- 修复导致400错误的问题

## Capabilities

### New Capabilities
- `fix-loveapp-400-error`: 修复LoveApp会话历史导致的400错误

### Modified Capabilities
- 无

## Impact

- LoveApp.java
- ChatMemoryServiceImpl.java
- MybatisPlusChatMemory.java
