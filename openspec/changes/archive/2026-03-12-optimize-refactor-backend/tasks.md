## 1. Security - Remove Hardcoded API Keys

- [x] 1.1 Remove hardcoded API key from QwenConfig.java
- [x] 1.2 Remove hardcoded API key from ToolRegistration.java
- [x] 1.3 Update application.yml to use environment variables

## 2. Architecture - Agent Dependency Injection

- [x] 2.1 Modify Manus.java to accept ModelService instead of ChatModel
- [x] 2.2 Modify QuizAssistant.java to accept ModelService
- [x] 2.3 Modify HealthAssistant.java to accept ModelService
- [x] 2.4 Update AiController to inject agents via @Autowired
- [x] 2.5 Create AgentFactory for managing agent instances (using DynamicModelService instead)

## 3. Code Quality - Implement TestConfig

- [x] 3.1 Implement LlmConfigServiceImpl.testConfig() with actual testing logic
- [x] 3.2 Implement VectorDbConfigServiceImpl.testConfig() with actual testing logic

## 4. Code Cleanup

- [x] 4.1 Remove unused imports in LoveApp.java
- [x] 4.2 Remove commented code blocks in LoveApp.java
- [x] 4.3 Fix variable naming (Manus -> manus)
- [x] 4.4 Consolidate duplicate SSE methods in AiController

## 5. Thread Safety

- [x] 5.1 Review BaseAgent.java for thread safety issues
- [x] 5.2 Make Agent state thread-safe if needed
