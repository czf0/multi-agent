## Why

恋爱助手(LoveApp)第一次聊天正常，但再次聊天时报错 "400 Bad Request from POST https://api.minimaxi.com/v1/chat/completions"。这可能是由于聊天记忆(ChatMemory)管理问题、请求参数累积问题或API调用方式不正确导致的。

## What Changes

- 检查并修复 LoveApp 中的 ChatMemory 管理逻辑
- 确保每次请求时聊天历史正确加载和保存
- 检查是否有请求参数累积或格式问题

## Capabilities

### Modified Capabilities
- 恋爱助手聊天功能修复

## Impact

- LoveApp.java
- 相关服务类
