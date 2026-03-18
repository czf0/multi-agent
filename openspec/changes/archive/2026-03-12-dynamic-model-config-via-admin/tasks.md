## 1. Database Tables

- [x] 1.1 Create llm_config table SQL script
- [x] 1.2 Create vector_db_config table SQL script
- [x] 1.3 Create agent_model_binding table SQL script

## 2. Entity Classes

- [x] 2.1 Create LlmConfig entity with JPA annotations
- [x] 2.2 Create VectorDbConfig entity with JPA annotations
- [x] 2.3 Create AgentModelBinding entity with JPA annotations

## 3. Mapper Layer

- [x] 3.1 Create LlmConfigMapper interface (extends BaseMapper)
- [x] 3.2 Create VectorDbConfigMapper interface (extends BaseMapper)
- [x] 3.3 Create AgentModelBindingMapper interface (extends BaseMapper)

## 4. Service Layer

- [x] 4.1 Create LlmConfigService interface and LlmConfigServiceImpl
- [x] 4.2 Create VectorDbConfigService interface and VectorDbConfigServiceImpl
- [x] 4.3 Create AgentModelBindingService interface and AgentModelBindingServiceImpl
- [x] 4.4 Add test methods to service implementations

## 5. Admin Controller

- [x] 5.1 Create AdminController with LLM config CRUD endpoints
- [x] 5.2 Add vector DB config CRUD endpoints
- [x] 5.3 Add agent binding CRUD endpoints
- [x] 5.4 Add test/config validation endpoints
- [x] 5.5 Add get default config for agent endpoint

## 6. Dynamic ChatModel Factory

- [x] 6.1 Create DynamicChatModelFactory class
- [x] 6.2 Implement createDashScopeChatModel method
- [x] 6.3 Implement createOllamaChatModel method
- [x] 6.4 Implement createOpenAiChatModel method
- [x] 6.5 Add caching for created ChatModel instances

## 7. Integration with Existing Agents

- [x] 7.1 Modify agents to use dynamic model service
- [x] 7.2 Update AiController to support model selection from database config
- [x] 7.3 Add refresh config endpoint

## 8. Frontend - API Layer

- [x] 8.1 Add admin API methods for LLM config
- [x] 8.2 Add admin API methods for vector DB config
- [x] 8.3 Add admin API methods for agent binding

## 9. Frontend - Admin Pages

- [x] 9.1 Create LLMConfigView.vue page
- [x] 9.2 Create VectorDbConfigView.vue page
- [x] 9.3 Create AgentBindingView.vue page
- [x] 9.4 Add routes for admin pages
- [x] 9.5 Add navigation menu items

## 10. Testing & Verification

- [ ] 10.1 Test LLM config CRUD via API
- [ ] 10.2 Test vector DB config CRUD via API
- [ ] 10.3 Test agent binding CRUD via API
- [ ] 10.4 Test config test endpoint
- [ ] 10.5 Test agent with different model configs
- [ ] 10.6 Verify existing functionality works
