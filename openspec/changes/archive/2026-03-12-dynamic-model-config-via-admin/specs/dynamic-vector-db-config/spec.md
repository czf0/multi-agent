## ADDED Requirements

### Requirement: Vector DB Config Database Table
The system SHALL store vector database configurations in the vector_db_config database table.

#### Scenario: Vector DB config table structure
- **WHEN** the database is created
- **THEN** vector_db_config table exists with fields: id, name, provider, host, port, database, username, password, enabled, create_time, update_time

### Requirement: Create Vector DB Config
The system SHALL allow creating vector database configurations via API.

#### Scenario: Create new vector DB config
- **WHEN** POST /api/admin/vector-db-config with valid data
- **THEN** a new vector DB config is saved to database

### Requirement: List Vector DB Configs
The system SHALL provide an API to list all vector database configurations.

#### Scenario: List all configs
- **WHEN** GET /api/admin/vector-db-config/list is called
- **THEN** returns all vector DB configs

### Requirement: Update Vector DB Config
The system SHALL allow updating existing vector database configurations.

#### Scenario: Update config
- **WHEN** PUT /api/admin/vector-db-config/{id} with updated data
- **THEN** the config is updated

### Requirement: Delete Vector DB Config
The system SHALL allow deleting vector database configurations.

#### Scenario: Delete config
- **WHEN** DELETE /api/admin/vector-db-config/{id} is called
- **THEN** the config is removed from database

### Requirement: Test Vector DB Config
The system SHALL provide an API to test if a vector database configuration is valid.

#### Scenario: Test valid config
- **WHEN** POST /api/admin/vector-db-config/test with valid connection data
- **THEN** returns success indicating connection successful

#### Scenario: Test invalid config
- **WHEN** POST /api/admin/vector-db-config/test with invalid host/port
- **THEN** returns error indicating connection failed
