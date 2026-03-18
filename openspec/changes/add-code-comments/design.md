## Context

项目代码缺少详细的JavaDoc注释，需要为关键类和方法添加标准注释。

## Goals / Non-Goals

**Goals:**
- 为所有 public 类添加类级别 JavaDoc 注释
- 为所有 public 方法添加方法级别 JavaDoc 注释
- 包含 @author Jofend
- 有参方法必须包含 @param 说明
- 有返回值的方法包含 @return 说明

**Non-Goals:**
- 不修改业务逻辑
- 不添加过于简单的getter/setter注释

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

**3. 优先级**

1. Controller 类（接口层）
2. Service 类（业务层）
3. Entity 类（实体层）
4. Tool 类（工具类）

## Risks / Trade-offs

- 注释工作量较大 → 分批处理，优先处理核心类
