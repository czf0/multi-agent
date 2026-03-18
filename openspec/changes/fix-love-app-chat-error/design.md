## Context

恋爱助手(LoveApp)在第一次聊天时正常，但第二次聊天时报错 "400 Bad Request"。这个错误来自 minimaxi API，说明请求格式有问题。

## Goals / Non-Goals

**Goals:**
- 找到导致第二次聊天失败的根本原因
- 修复 ChatMemory 管理问题
- 确保多次聊天都能正常工作

**Non-Goals:**
- 不修改其他智能体的功能

## Decisions

**可能的原因分析:**

1. **ChatMemory 问题**: 聊天历史可能未正确保存或加载，导致请求格式错误
2. **请求参数累积**: 可能在构建请求时累积了错误的参数
3. **API 调用方式**: 可能存在请求构建问题

**修复方案:**
- 检查 LoveApp 中的 ChatMemory 使用方式
- 确保每个会话有独立的 ChatMemory
- 检查 ChatClient 构建方式

## Risks / Trade-offs

- 需要调试找出具体原因
