## ADDED Requirements

### Requirement: 恋爱助手修复第二次聊天400错误

恋爱助手 SHALL 能够正常进行多次对话，第一次和后续对话都应该正常工作。

#### Scenario: 第一次聊天正常
- **WHEN** 用户进行第一次聊天
- **THEN** 返回正常的AI响应

#### Scenario: 第二次及后续聊天正常
- **WHEN** 用户进行第二次聊天
- **THEN** 返回正常的AI响应，不报400错误
