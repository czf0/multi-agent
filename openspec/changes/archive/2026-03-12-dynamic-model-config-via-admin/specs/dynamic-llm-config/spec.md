## ADDED Requirements

### Requirement: LLM Config Database Table
The system SHALL store LLM configurations in the llm_config database table.

#### Scenario: LLM config table structure
- **WHEN** the database is created
- **THEN** llm_config table exists with fields: id, name, provider, model_name, api_key, api_base_url, enabled, priority, create_time, update_time

### Requirement: Create LLM Config
The system SHALL allow creating LLM configurations via API.

#### Scenario: Create new LLM config
- **WHEN** POST /api/admin/llm-config with valid data (name, provider, model_name, api_key)
- **THEN** a new LLM config is saved to database with create_time

### Requirement: List LLM Configs
The system SHALL provide an API to list all LLM configurations.

#### Scenario: List all configs
- **WHEN** GET /api/admin/llm-config/list is called
- **THEN** returns all LLM configs ordered by priority

### Requirement: Update LLM Config
The system SHALL allow updating existing LLM configurations.

#### Scenario: Update config
- **WHEN** PUT /api/admin/llm-config/{id} with updated data
- **THEN** the config is updated and update_time is modified

### Requirement: Delete LLM Config
The system SHALL allow deleting LLM configurations.

#### Scenario: Delete config
- **WHEN** DELETE /api/admin/llm-config/{id} is called
- **THEN** the config is removed from database

### Requirement: Enable/Disable LLM Config
The system SHALL allow enabling or disabling LLM configurations without deletion.

#### Scenario: Disable config
- **WHEN** PATCH /api/admin/llm-config/{id}/disable is called
- **THEN** the enabled field is set to false

### Requirement: Test LLM Config
The system SHALL provide an API to test if an LLM configuration is valid.

#### Scenario: Test valid config
- **WHEN** POST /api/admin/llm-config/test with provider, model_name, api_key
- **THEN** returns success with a test message from the model

#### Scenario: Test invalid config
- **WHEN** POST /api/admin/llm-config/test with invalid api_key
- **THEN** returns error indicating authentication failure

### Requirement: Get LLM Config by Agent
The system SHALL allow querying LLM configs bound to a specific agent.

#### Scenario: Get configs for agent
- **WHEN** GET /api/admin/llm-config/agent/{agentName} is called
- **THEN** returns LLM configs bound to the specified agent
