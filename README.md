# PurePicks — 自然良品 多Agent电商推荐系统

简体中文 | [English Version](README_EN.md)

## 业界首个融合多源开源能力的端到端电商多智能体系统

**解决电商推荐的最后一公里问题**

## 简介

PurePicks（自然良品）是一个融合三大开源项目精华的端到端多Agent电商推荐系统。它不仅仅是一个框架或SDK，而是一个可以直接使用的产品级系统。

对于用户输入的查询或任务，PurePicks可以直接给出推荐结果、分析报告或执行自动化操作。例如用户query"推荐几款性价比高的无线耳机"，PurePicks可以直接给出对比分析报告。

### 核心特性

- **多Agent协作**: 基于A2A协议的Agent间通信，6个专业Agent协同工作
- **NL2SQL智能问数**: 自然语言查询数据库
- **报告生成**: 自动生成网页版/PPT版分析报告
- **浏览器自动化**: 集成Browser-use、Crawl4ai、Playwright等工具
- **OCR + CV**: 屏幕理解与操作，支持指纹浏览器
- **可视化工作流**: Flowgram LangGraph可视化编辑器
- **RAG知识检索**: 多模态非结构化知识管理
- **隐私优先**: 支持本地部署LLM（Ollama + Qwen3）

## 产品对比

| 特性 | PurePicks | SpringAI-Alibaba | Coze |
|------|-----------|------------------|------|
| 是否开源 | ✅ 完整开源 | 部分 | 部分 |
| 是否完整产品 | ✅ 端到端 | 否（仅SDK） | 否（仅SDK） |
| 是否依赖平台 | 否 | 是（阿里云百炼） | 是（火山引擎） |
| 电商专用Agent | ✅ | 否 | 否 |
| 浏览器自动化 | ✅ | 否 | 否 |
| 可视化工作流 | ✅ | 否 | 否 |

## 架构

```
┌─────────────────────────────────────────────────────────┐
│                    PurePicks Frontend                     │
│              (Vue.js + Next.js UI)                        │
├─────────────────────────────────────────────────────────┤
│                    PurePicks Backend                      │
│           (Java Spring Boot + Python FastAPI)             │
├──────────┬──────────┬──────────┬──────────┬─────────────┤
│ 报告Agent │ 推荐Agent │ 数据Agent │ 搜索Agent │  客服Agent   │
├──────────┴──────────┴──────────┴──────────┴─────────────┤
│              A2A Protocol + MCP Layer                     │
├─────────────────────────────────────────────────────────┤
│  LangGraph Workflows | Browser Automation | OCR + CV     │
├─────────────────────────────────────────────────────────┤
│           PostgreSQL + pgvector | Ollama                  │
└─────────────────────────────────────────────────────────┘
```

## 快速开始

### 前置条件

- JDK 17+
- Python 3.10+
- Node.js 18+
- Docker (可选)

### 安装

```bash
# 1. 克隆仓库
git clone https://github.com/503496348-ops/purepicks.git
cd purepicks

# 2. 配置环境
cp purepicks-backend/src/main/resources/application.yml.example purepicks-backend/src/main/resources/application.yml
# 编辑配置文件，添加你的LLM API Key

# 3. 启动后端
cd purepicks-backend && sh build.sh

# 4. 启动前端
cd ui && sh start.sh

# 5. 启动工具服务
cd purepicks-tool && python server.py

# 6. 启动客户端
cd purepicks-client && python server.py
```

### 访问

前端: http://localhost:3000
API: http://localhost:8080

## 项目结构

```
purepicks/
├── purepicks-backend/     # Java后端 (Spring Boot)
│   └── src/
│       └── main/java/com/atomcollide/purepicks/
├── purepicks-client/      # Python客户端
│   └── app/
├── purepicks-tool/        # 工具服务 (NL2SQL, 文件管理, LLM)
│   └── purepicks_tool/
├── ui/                    # Vue.js前端
│   └── src/
├── docs/                  # 文档
├── SKILL.md              # 技能定义
└── PurePicks_start.sh    # 一键启动脚本
```

## Agent目录

| Agent | 功能 | 技术栈 |
|-------|------|--------|
| 报告Agent | 生成分析报告（网页/PPT） | LangGraph + LLM |
| 推荐Agent | 电商产品推荐 | NL2SQL + RAG |
| 数据Agent | 智能问数与诊断分析 | DGP协议 + SQL |
| 搜索Agent | 产品搜索与对比 | Browser-use + Crawl4ai |
| 客服Agent | 客户服务与问答 | RAG + A2A |
| 文件Agent | 文件处理与OCR | OCR + CV |

## 文档

- [部署指南](Deploy.md)
- [多模态RAG](README_mrag.md)
- [数据Agent](README_DataAgent.md)
- [API文档](docs/)

## 贡献

欢迎贡献！请阅读 [贡献指南](contributor_ZH.pdf)。

## 许可证

Apache-2.0

## 版权

Copyright 2026 AtomCollide-智械工坊

---

**PurePicks** — 让电商推荐更智能
