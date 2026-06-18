# PurePicks Skill

- **name**: purepicks
- **version**: 1.0.0
- **description**: 自然良品（PurePicks）— 多Agent电商推荐系统。端到端智能体+NL2SQL+报告生成+浏览器自动化
- **author**: AtomCollide-智械工坊团队
- **license**: Apache-2.0

## 概述

PurePicks（自然良品）是一个开源的端到端多Agent电商推荐系统，融合了三大开源项目的精华：

PurePicks 整合了多Agent协作、NL2SQL智能问数、浏览器自动化、OCR+CV、A2A协议和可视化工作流等核心能力，打造完整的端到端电商智能体系统。

## 核心能力

### 多Agent协作
- 端到端多智能体框架，开箱即用
- A2A协议实现Agent间通信
- 支持自定义子智能体和工具挂载

### 电商推荐
- NL2SQL智能问数
- 产品发现、订单管理、定价策略、评论分析、库存管理、客户服务
- 多模态知识管理（RAG）

### 浏览器自动化
- Browser-use / Crawl4ai / Playwright 集成
- OCR + CV引导的屏幕理解与操作
- 支持指纹浏览器（AdsPower等）

### 报告生成
- 智能报告生成（网页版/PPT版）
- 数据分析与可视化

### 可视化工作流
- Flowgram LangGraph可视化编辑器
- 拖拽式工作流设计
- 支持断点调试和状态检查

## 技术栈

- **后端**: Java (Spring Boot) + Python (FastAPI)
- **前端**: Vue.js + Next.js
- **AI框架**: LangGraph + LangChain + Microsoft Agent Framework
- **协议**: A2A Protocol + MCP
- **数据库**: PostgreSQL + pgvector
- **部署**: Docker Compose

## 快速开始

```bash
# 克隆仓库
git clone https://github.com/503496348-ops/purepicks.git
cd purepicks

# 启动后端
cd purepicks-backend && sh build.sh

# 启动前端
cd ui && sh start.sh

# 启动工具服务
cd purepicks-tool && python server.py

# 启动客户端
cd purepicks-client && python server.py
```

## 项目结构

```
purepicks/
├── purepicks-backend/    # Java后端服务
├── purepicks-client/     # Python客户端
├── purepicks-tool/       # 工具服务（NL2SQL、文件管理等）
├── ui/                   # Vue.js前端
├── docs/                 # 文档
└── PurePicks_start.sh    # 一键启动脚本
```

## 许可证

Apache-2.0

## 版权

Copyright 2026 AtomCollide-智械工坊
