## MODIFIED Requirements

### Requirement: 智能体绑定 LLM
**From**: 系统 SHALL 支持将智能体与 LLM 配置进行绑定。

**To**: 系统 SHALL 支持将智能体与 LLM 配置和向量数据库配置直接绑定，绑定关系存储在智能体表中。

#### Scenario: 绑定 LLM 到智能体
- **WHEN** 管理员为智能体选择并绑定一个 LLM 配置
- **THEN** 系统将 LLM 配置ID直接保存到智能体记录中，智能体后续对话使用该 LLM

#### Scenario: 绑定向量数据库到智能体
- **WHEN** 管理员为智能体选择并绑定一个向量数据库配置
- **THEN** 系统将向量数据库配置ID直接保存到智能体记录中

#### Scenario: 解绑 LLM
- **WHEN** 管理员清除智能体与 LLM 的绑定
- **THEN** 系统将该智能体的 llm_config_id 设为 null

#### Scenario: 绑定关系内嵌到智能体
- **WHEN** 查询智能体详情
- **THEN** 系统返回智能体信息同时包含 llm_config_id 和 vector_db_config_id，无需额外关联查询
