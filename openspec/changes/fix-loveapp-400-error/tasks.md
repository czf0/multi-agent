## 1. 问题定位

- [x] 1.1 使用InMemoryChatMemory替代MybatisPlusChatMemory测试，确认问题是否在数据库持久化
- **结论**: InMemoryChatMemory 也存在同样问题，问题在 MessageChatMemoryAdvisor 本身

## 2. 修复问题

- [x] 2.1 移除 MessageChatMemoryAdvisor，改用手动管理消息历史
- [x] 2.2 实现手动获取历史消息方法 getHistoryMessages()
- [x] 2.3 实现手动保存消息方法 saveMessages()
- [x] 2.4 修改所有对话方法使用手动消息历史管理

## 3. 测试验证

- [ ] 3.1 测试多次对话
- [ ] 3.2 确认会话历史正常工作
