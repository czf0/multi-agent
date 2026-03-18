## ADDED Requirements

### Requirement: 修复 LoveApp 会话历史导致400错误

LoveApp SHALL 能够在有历史记录的情况下正常进行对话，不报400错误。

#### Scenario: 第一次对话正常
- **WHEN** 用户进行第一次对话
- **THEN** 正常返回AI响应

#### Scenario: 有历史记录时对话正常
- **WHEN** 用户进行第二次及后续对话（有聊天历史）
- **THEN** 正常返回AI响应，不报400错误
