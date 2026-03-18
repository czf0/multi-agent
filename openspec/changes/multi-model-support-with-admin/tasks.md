## 1. Model Abstraction Layer

- [ ] 1.1 Create ModelProvider enum (DASHSCOPE, OLLAMA, DEEPSEEK, OPENAI, CLAUDE)
- [ ] 1.2 Create ModelConfig configuration class with @ConfigurationProperties
- [ ] 1.3 Create ModelService interface with getChatModel, getChatModelByModelName, getDefaultChatModel, getAvailableModels methods
- [ ] 1.4 Create DefaultModelServiceImpl implementation with ChatModel Map lookup
- [ ] 1.5 Create ModelRouter for dynamic routing based on provider/model name

## 2. Multi-Model Configuration

- [ ] 2.1 Add spring-ai-openai-starter dependency to pom.xml
- [ ] 2.2 Update application.yml with multi-model configuration (provider-models, models)
- [ ] 2.3 Configure Ollama and OpenAI ChatModel beans (disabled by default)

## 3. Agent Components Modification

- [ ] 3.1 Modify Manus to accept ModelService and add runWithModel method
- [ ] 3.2 Modify LoveApp to add doChat with provider/model parameters
- [ ] 3.3 Modify QuizAssistant to accept ModelService and add runWithModel method
- [ ] 3.4 Modify HealthAssistant to accept ModelService and add runWithModel method

## 4. Controller Updates

- [ ] 4.1 Modify AiController to add provider/model parameters to all endpoints
- [ ] 4.2 Add /ai/models endpoint to list available models
- [ ] 4.3 Inject Agent components as beans instead of creating new instances

## 5. Backend Management - Database

- [ ] 5.1 Create llm_config table SQL script
- [ ] 5.2 Create vector_db_config table SQL script
- [ ] 5.3 Create agent_model_binding table SQL script

## 6. Backend Management - Entities

- [ ] 6.1 Create LlmConfig entity class
- [ ] 6.2 Create VectorDbConfig entity class
- [ ] 6.3 Create AgentModelBinding entity class

## 7. Backend Management - Mapper

- [ ] 7.1 Create LlmConfigMapper with MyBatis-Plus
- [ ] 7.2 Create VectorDbConfigMapper with MyBatis-Plus
- [ ] 7.3 Create AgentModelBindingMapper with MyBatis-Plus

## 8. Backend Management - Service

- [ ] 8.1 Create LlmConfigService interface and implementation
- [ ] 8.2 Create VectorDbConfigService interface and implementation
- [ ] 8.3 Create AgentModelBindingService interface and implementation

## 9. Backend Management - Controller

- [ ] 9.1 Create AdminController with LLM config CRUD endpoints
- [ ] 9.2 Add vector DB config CRUD endpoints to AdminController
- [ ] 9.3 Add agent binding endpoints to AdminController
- [ ] 9.4 Add LLM config test endpoint

## 10. Frontend - API

- [ ] 10.1 Update api.js to add model selection parameter support
- [ ] 10.2 Add admin API methods for LLM config, vector DB config, agent binding

## 11. Frontend - Pages

- [ ] 11.1 Create ModelConfigView.vue for LLM configuration management
- [ ] 11.2 Create VectorDbConfigView.vue for vector DB configuration
- [ ] 11.3 Create AgentBindingView.vue for agent-model binding
- [ ] 11.4 Add routes for admin pages

## 12. Integration & Testing

- [ ] 12.1 Test multi-model switching via API parameters
- [ ] 12.2 Test backend management CRUD operations
- [ ] 12.3 Verify existing Qwen functionality still works
- [ ] 12.4 Verify all Agent components work with different models
