## Why

项目代码中缺少详细的JavaDoc注释，影响代码可读性和可维护性。需要为整个后端项目的所有模块添加标准JavaDoc注释，包含作者信息和参数说明。

## What Changes

- 为所有 public 类添加类级别 JavaDoc 注释，包含 @author Jofend
- 为所有 public/protected 方法添加方法级别 JavaDoc 注释
- 对于有参数的方法，使用 @param 说明参数含义
- 对于有返回值的方法，使用 @return 说明返回值
- 对于可能抛出异常的方法，使用 @throws 说明异常

## Capabilities

### Modified Capabilities
- 代码质量规范：后端全模块注释规范

## Impact

- src/main/java/com/czf/aiagent 下所有 Java 类文件
- controller, service, entity, agent, app, tool, mapper, config, rag, domain, constant, advisor 等所有包
