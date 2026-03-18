## 1. 数据库和实体类

- [x] 1.1 创建 agent 数据库表 SQL 脚本
- [x] 1.2 创建 Agent 实体类
- [x] 1.3 创建 AgentMapper 接口
- [x] 1.4 创建 AgentService 接口和实现类

## 2. 后端接口

- [x] 2.1 在 AdminController 添加智能体 CRUD 接口
- [x] 2.2 修改 AgentModelBindingService，添加根据 agentId 查询的方法
- [x] 2.3 创建 UnifiedChatService 统一对话服务
- [x] 2.4 在 AiController 添加统一对话接口 GET /ai/chat/{agentId}

## 3. 前端

- [x] 3.1 在 api.js 添加智能体管理 API 方法
- [x] 3.2 创建 AgentConfigView.vue 智能体管理页面
- [x] 3.3 在 App.vue 添加智能体管理菜单

## 4. 测试验证

- [ ] 4.1 测试智能体 CRUD 接口
- [ ] 4.2 测试智能体绑定 LLM
- [ ] 4.3 测试统一对话接口
