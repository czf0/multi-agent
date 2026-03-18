## Why

当前项目缺少系统性的技术文档，现有的信息分散在代码注释和有限的README中。新成员加入项目时需要花费大量时间理解系统架构、API接口和组件关系。创建项目文档可以提升团队协作效率，降低知识传递成本，并为后续功能扩展提供清晰的参考。

## What Changes

- 创建完整的项目技术文档，包含项目架构、技术栈、模块说明
- 整理并文档化所有后端 REST API 接口
- 绘制项目结构图和组件关系图
- 记录前端路由和页面组件结构
- 整理 AI Agent 能力说明和工具列表

## Capabilities

### New Capabilities

- `project-overview`: 创建项目整体介绍文档，包含技术架构、核心模块、项目结构
- `api-documentation`: 创建完整的 API 接口文档，包含所有 Controller 的端点说明
- `component-reference`: 创建组件参考文档，记录所有 AI Agent 和 Tool 的功能说明

### Modified Capabilities

- (无现有需求变更)

## Impact

- 后端: 无代码变更，仅文档输出
- 前端: 无代码变更，仅文档输出
- 依赖: 无新增依赖
- 系统: 生成 Markdown 格式文档，输出到项目根目录或 docs 目录
