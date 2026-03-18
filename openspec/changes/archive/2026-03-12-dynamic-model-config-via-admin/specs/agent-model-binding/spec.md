## ADDED Requirements

### Requirement: Agent Model Binding Database Table
The system SHALL store agent-model bindings in the agent_model_binding database table.

#### Scenario: Binding table structure
- **WHEN** the database is created
- **THEN** agent_model_binding table exists with fields: id, agent_name, llm_config_id, vector_db_config_id, is_default, create_time, update_time

### Requirement: Create Agent Binding
The system SHALL allow creating bindings between agents and LLM/vector DB configs.

#### Scenario: Create new binding
- **WHEN** POST /api/admin/agent-binding with agent_name, llm_config_id, vector_db_config_id
- **THEN** a new binding is saved to database

### Requirement: List Agent Bindings
The system SHALL provide an API to list all agent-model bindings.

#### Scenario: List all bindings
- **WHEN** GET /api/admin/agent-binding/list is called
- **THEN** returns all bindings with related config names

### Requirement: Get Binding by Agent Name
The system SHALL allow querying bindings for a specific agent.

#### Scenario: Get bindings for agent
- **WHEN** GET /api/admin/agent-binding/agent/{agentName} is called
- **THEN** returns bindings for the specified agent

### Requirement: Update Agent Binding
The system SHALL allow updating existing agent-model bindings.

#### Scenario: Update binding
- **WHEN** PUT /api/admin/agent-binding/{id} with updated data
- **THEN** the binding is updated

### Requirement: Delete Agent Binding
The system SHALL allow deleting agent-model bindings.

#### Scenario: Delete binding
- **WHEN** DELETE /api/admin/agent-binding/{id} is called
- **THEN** the binding is removed from database

### Requirement: Set Default Binding
The system SHALL allow setting a binding as the default for an agent.

#### Scenario: Set default binding
- **WHEN** PUT /api/admin/agent-binding/{id}/set-default is called
- **THEN** the binding is marked as is_default=true, previous default is set to false

### Requirement: Get Default Config for Agent
The system SHALL provide a way to get the default LLM config for an agent.

#### Scenario: Get default config
- **WHEN** GET /api/admin/agent-binding/agent/{agentName}/default is called
- **THEN** returns the default LLM config for the agent
