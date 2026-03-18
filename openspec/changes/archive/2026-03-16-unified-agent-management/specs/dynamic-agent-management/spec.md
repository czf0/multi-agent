## ADDED Requirements

### Requirement: 智能体 CRUD 管理

系统 SHALL 支持对智能体进行增删改查操作，包括智能体名称、描述、系统提示词等属性。

#### Scenario: 创建智能体
- **WHEN** 管理员提交智能体信息（名称、描述、系统提示词、是否启用）
- **THEN** 系统保存智能体信息到数据库，返回创建成功的智能体ID

#### Scenario: 查询智能体列表
- **WHEN** 管理员请求智能体列表
- **THEN** 系统返回所有智能体信息，支持分页

#### Scenario: 更新智能体
- **WHEN** 管理员修改智能体信息并提交
- **THEN** 系统更新数据库中的智能体信息

#### Scenario: 删除智能体
- **WHEN** 管理员删除指定智能体
- **THEN** 系统从数据库中删除该智能体，同时解除与LLM的绑定关系

### Requirement: 智能体绑定 LLM

系统 SHALL 支持将智能体与 LLM 配置进行绑定。

#### Scenario: 绑定 LLM 到智能体
- **WHEN** 管理员为智能体选择并绑定一个 LLM 配置
- **THEN** 系统保存绑定关系，智能体后续对话使用该 LLM

#### Scenario: 解绑 LLM
- **WHEN** 管理员解除智能体与 LLM 的绑定
- **THEN** 系统移除绑定关系，该智能体使用默认 LLM（如果有）
