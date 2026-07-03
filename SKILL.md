---
name: purepicks
version: 1.0.0
description: "端到端多Agent电商推荐系统。NL2SQL智能问数+产品发现+订单管理+评论分析+库存管理。当需要电商数据分析、产品推荐、竞品监控时使用。"
author: AtomCollide-智械工坊团队
license: Apache-2.0
triggers:
  - 电商推荐
  - 智能客服
  - 多Agent协作
  - NL2SQL
  - 自然良品
  - purepicks
---

# PurePicks Skill

> 📖 详细文档见 `references/` 目录

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

## 工作流

使用此技能时，按以下步骤执行：
- [ ] 1. 确认用户需求和使用场景
- [ ] 2. 加载相关代码和配置
- [ ] 3. 执行核心功能
- [ ] 4. 验证输出结果
- [ ] 5. 反馈给用户

## 2026-07-03 产品收敛门禁

- 新增 `scripts/product_convergence_gate.py`：从远端干净 clone 后可运行 `python3 scripts/product_convergence_gate.py --json`，检查 SKILL/README、入口文件、smoke 目标、测试与外部融合引用是否自洽。
- 新增 `tests/test_product_convergence_gate.py`：确保门禁在产品仓库中真实可执行，避免后续增强只停留在孤岛模块。

## 一键开箱交付

本仓库提供标准一键入口：

- `install.sh`：用户的一条命令安装与冒烟入口。
- `scripts/setup.py`：安装声明依赖并串联 doctor。
- `scripts/doctor.py`：检查 README、SKILL、入口脚本、package scripts 与产品收敛门禁。
- `scripts/smoke.py`：运行 doctor、产品收敛门禁与 Python 编译级冒烟。
- `tests/test_one_click_open_box.py`：契约测试，防止 README 写了但脚本缺失。
