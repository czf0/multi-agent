## ADDED Requirements

### Requirement: ModelProvider Enumeration
The system SHALL define a `ModelProvider` enumeration containing supported AI model vendors.

#### Scenario: Get provider by code
- **WHEN** code "dashscope" is passed to `ModelProvider.getByCode()`
- **THEN** it returns `ModelProvider.DASHSCOPE`

#### Scenario: Get unknown provider
- **WHEN** an unknown code is passed to `ModelProvider.getByCode()`
- **THEN** it returns `ModelProvider.DASHSCOPE` as default

### Requirement: Model Configuration Management
The system SHALL provide a `ModelConfig` class to manage configurations for different model providers.

#### Scenario: Default provider configuration
- **WHEN** application starts without explicit configuration
- **THEN** default provider is "dashscope" and default model is "qwen-plus"

#### Scenario: Get model properties by name
- **WHEN** `getModelProperties("qwen-plus")` is called
- **THEN** it returns `ModelProperties` with provider "dashscope" and modelName "qwen-plus"

### Requirement: Model Service Interface
The system SHALL provide a `ModelService` interface with methods for dynamic ChatModel retrieval.

#### Scenario: Get ChatModel by provider
- **WHEN** `getChatModel("dashscope")` is called
- **THEN** it returns the DashScope ChatModel instance

#### Scenario: Get ChatModel by model name
- **WHEN** `getChatModelByModelName("qwen-plus")` is called
- **THEN** it returns the appropriate ChatModel instance

#### Scenario: Get default ChatModel
- **WHEN** `getDefaultChatModel()` is called
- **THEN** it returns the configured default ChatModel

#### Scenario: Get available models
- **WHEN** `getAvailableModels()` is called
- **THEN** it returns a list of all available model names

### Requirement: Model Routing
The system SHALL provide a `ModelRouter` component that routes requests to appropriate ChatModel.

#### Scenario: Route by model name
- **WHEN** `route(null, "qwen-plus")` is called
- **THEN** it returns the ChatModel for qwen-plus

#### Scenario: Route by provider
- **WHEN** `route("ollama", null)` is called
- **THEN** it returns the ChatModel for Ollama

#### Scenario: Route with both parameters
- **WHEN** `route("dashscope", "qwen-turbo")` is called
- **THEN** model name takes priority and returns qwen-turbo ChatModel

### Requirement: Agent Model Selection
The system SHALL allow agents to use different models at runtime.

#### Scenario: Manus with specific model
- **WHEN** `manus.runWithModel("ollama", "llama3.2")` is called
- **THEN** Manus uses the Ollama llama3.2 model for subsequent requests

#### Scenario: LoveApp with model parameter
- **WHEN** `loveApp.doChat("hello", "chat1", "dashscope", "qwen-max")` is called
- **THEN** LoveApp uses qwen-max model for this request

### Requirement: API Model Parameter Support
The API endpoints SHALL accept optional model selection parameters.

#### Scenario: Love app chat with model
- **WHEN** GET /ai/love_app/chat/sync?message=hello&chatId=chat1&model=qwen-max
- **THEN** response uses qwen-max model

#### Scenario: Manus chat with model
- **WHEN** GET /ai/manus/chat?message=help&model=ollama
- **THEN** response uses Ollama model

#### Scenario: Get available models API
- **WHEN** GET /ai/models is called
- **THEN** it returns list of available model names

### Requirement: Multi-Model Configuration
The system SHALL support configuration of multiple models in application.yml.

#### Scenario: Enable Ollama
- **WHEN** spring.ai.ollama.enabled=true is configured
- **THEN** Ollama ChatModel is available for selection

#### Scenario: Configure multiple models
- **WHEN** ai-models.provider-models maps multiple models to providers
- **THEN** all configured models are available for selection
