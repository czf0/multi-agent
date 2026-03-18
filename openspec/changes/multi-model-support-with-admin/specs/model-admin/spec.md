## ADDED Requirements

### Requirement: LLM Configuration Entity
The system SHALL provide an entity class `LlmConfig` for storing LLM provider configurations.

#### Scenario: Create LLM config
- **WHEN** a new LLM config is created with name, provider, model_name, api_key
- **THEN** it is persisted to the database with create_time and update_time

#### Scenario: List all LLM configs
- **WHEN** GET /api/admin/llm-config/list is called
- **THEN** it returns all LLM configurations sorted by priority

### Requirement: Vector Database Configuration Entity
The system SHALL provide an entity class `VectorDbConfig` for storing vector database configurations.

#### Scenario: Create vector DB config
- **WHEN** a new vector DB config is created with name, provider, host, port
- **THEN** it is persisted to the database

#### Scenario: List all vector DB configs
- **WHEN** GET /api/admin/vector-db-config/list is called
- **THEN** it returns all vector database configurations

### Requirement: Agent Model Binding Entity
The system SHALL provide an entity class `AgentModelBinding` for binding agents to LLM and vector DB configs.

#### Scenario: Create agent binding
- **WHEN** a new binding is created for agent "manus" with llm_config_id and vector_db_config_id
- **THEN** it is persisted and marked as default if it's the first binding for that agent

#### Scenario: List agent bindings
- **WHEN** GET /api/admin/agent-binding/list is called
- **THEN** it returns all agent bindings with related config names

### Requirement: LLM Config CRUD API
The system SHALL provide REST APIs for LLM configuration management.

#### Scenario: Create LLM config via API
- **WHEN** POST /api/admin/llm-config with valid LLM config data
- **THEN** a new LLM config is created and returned

#### Scenario: Update LLM config
- **WHEN** PUT /api/admin/llm-config/{id} with updated data
- **THEN** the existing config is updated and returned

#### Scenario: Delete LLM config
- **WHEN** DELETE /api/admin/llm-config/{id} is called
- **THEN** the config is deleted from database

### Requirement: Vector DB Config CRUD API
The system SHALL provide REST APIs for vector database configuration management.

#### Scenario: Create vector DB config via API
- **WHEN** POST /api/admin/vector-db-config with valid data
- **THEN** a new vector DB config is created and returned

#### Scenario: Update vector DB config
- **WHEN** PUT /api/admin/vector-db-config/{id} with updated data
- **THEN** the existing config is updated

#### Scenario: Delete vector DB config
- **WHEN** DELETE /api/admin/vector-db-config/{id} is called
- **THEN** the config is deleted

### Requirement: Agent Binding API
The system SHALL provide REST APIs for agent-model binding management.

#### Scenario: Create agent binding via API
- **WHEN** POST /api/admin/agent-binding with agent_name, llm_config_id, vector_db_config_id
- **THEN** a new binding is created

#### Scenario: Update agent binding
- **WHEN** PUT /api/admin/agent-binding/{id} with updated binding
- **THEN** the binding is updated

### Requirement: LLM Config Test
The system SHALL provide an API to test if an LLM configuration is valid.

#### Scenario: Test valid LLM config
- **WHEN** POST /api/admin/llm-config/test with valid api_key and model_name
- **THEN** it returns success with test response

#### Scenario: Test invalid LLM config
- **WHEN** POST /api/admin/llm-config/test with invalid api_key
- **THEN** it returns error message indicating authentication failure

### Requirement: Backend Management UI
The system SHALL provide web pages for managing model configurations.

#### Scenario: Access LLM config page
- **WHEN** user navigates to /admin/models
- **THEN** a table of all LLM configs is displayed with add/edit/delete buttons

#### Scenario: Access vector DB config page
- **WHEN** user navigates to /admin/vector-db
- **THEN** a table of all vector DB configs is displayed

#### Scenario: Access agent binding page
- **WHEN** user navigates to /admin/agent-binding
- **THEN** a table of all agent bindings is displayed with dropdowns for LLM and vector DB selection
