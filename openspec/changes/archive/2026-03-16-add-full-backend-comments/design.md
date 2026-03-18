## Context

项目代码缺少详细的JavaDoc注释，需要为整个后端项目所有模块添加标准注释。

## Goals / Non-Goals

**Goals:**
- 为所有 public/protected 类添加类级别 JavaDoc 注释
- 为所有 public/protected 方法添加方法级别 JavaDoc 注释
- 包含 @author Jofend
- 有参方法必须包含 @param 说明
- 有返回值的方法包含 @return 说明

**Non-Goals:**
- 不修改业务逻辑
- 不为简单的 getter/setter 添加注释
- 不为 private 方法强制要求注释

## Decisions

**1. 类级别注释模板**

```java
/**
 * 类名描述
 *
 * @author Jofend
 * @date 2024-xx-xx
 */
```

**2. 方法级别注释模板**

```java
/**
 * 方法功能描述
 *
 * @param paramName 参数描述
 * @return 返回值描述
 * @throws ExceptionName 异常说明
 */
```

**3. 包处理顺序（按优先级）**

1. controller - 控制器层
2. service - 业务层
3. agent - 智能体
4. app - 应用层
5. entity/domain - 实体层
6. tool - 工具类
7. mapper - 数据访问层
8. config - 配置类
9. rag - RAG相关
10. constant - 常量
11. advisor - 顾问类
12. 其他

## Risks / Trade-offs

- 注释工作量较大 → 按优先级分批处理
