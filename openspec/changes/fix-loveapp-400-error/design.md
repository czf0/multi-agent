## Context

LoveApp使用Spring AI的MessageChatMemoryAdvisor管理聊天历史，通过MybatisPlusChatMemory实现数据库持久化。第一次对话正常，但第二次对话（有历史记录）时报400错误。

**当前架构：**
1. LoveApp → MessageChatMemoryAdvisor → MybatisPlusChatMemory → ChatMemoryService → 数据库

**可能原因分析：**
1. 数据库存储的消息格式与API要求不兼容
2. 消息顺序问题导致请求格式错误
3. minimax API对消息格式有严格要求（如role字段格式）
4. Spring AI版本与minimax API兼容性问题

## Goals / Non-Goals

**Goals:**
- 修复第二次对话时400错误
- 保持会话历史持久化功能
- 确保多次对话正常工作

**Non-Goals:**
- 不修改其他智能体功能
- 不更换AI供应商

## Decisions

**关键决策：**

1. **临时方案：先禁用数据库持久化测试**
   - 使用InMemoryChatMemory替代MybatisPlusChatMemory
   - 验证是否数据库持久化导致的问题
   - 如果InMemoryChatMemory工作正常，则问题在数据库存储/检索逻辑

2. **检查消息序列化/反序列化**
   - ChatMemoryServiceImpl中convertToMessage方法只保留content
   - 可能的解决方案：使用完整的JSON反序列化恢复消息对象

3. **检查消息顺序**
   - messageOrder字段可能存在排序问题
   - 需要验证检索消息的顺序是否正确

4. **API兼容性检查**
   - 检查minimax API对messages格式的要求
   - 可能需要配置MessageChatMemoryAdvisor的参数

## Risks / Trade-offs

- 如果禁用数据库持久化，会失去会话历史持久化功能（仅作为调试步骤）
- 需要在生产环境重新启用数据库持久化
