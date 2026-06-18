# PurePicks — Multi-Agent E-Commerce Recommendation System

English | [简体中文](README.md)

## The First End-to-End E-Commerce Multi-Agent System Combining Multiple Open-Source Innovations

**Solving the last mile of e-commerce recommendations**

## Introduction

PurePicks is an end-to-end multi-agent e-commerce recommendation system that combines the best of three open-source projects:

PurePicks integrates multi-agent collaboration, NL2SQL smart queries, browser automation, OCR+CV, A2A protocol, and visual workflow capabilities into a complete end-to-end e-commerce intelligent agent system.

For any user query or task, PurePicks can directly provide recommendations, analysis reports, or execute automation tasks. For example, when asked "recommend some cost-effective wireless earbuds", PurePicks can generate a comparative analysis report.

### Core Features

- **Multi-Agent Collaboration**: A2A protocol-based inter-agent communication with 6 specialized agents
- **NL2SQL Smart Query**: Natural language database queries
- **Report Generation**: Auto-generate web/PPT analysis reports
- **Browser Automation**: Integrated Browser-use, Crawl4ai, Playwright
- **OCR + CV**: Screen understanding and actions, fingerprint browser support
- **Visual Workflow**: Flowgram LangGraph visual editor
- **RAG Knowledge Retrieval**: Multi-modal unstructured knowledge management
- **Privacy First**: Local LLM deployment support (Ollama + Qwen3)

## Product Comparison

| Feature | PurePicks | SpringAI-Alibaba | Coze |
|---------|-----------|------------------|------|
| Open Source | ✅ Full | Partial | Partial |
| Complete Product | ✅ End-to-end | No (SDK only) | No (SDK only) |
| Platform Dependency | None | Alibaba Cloud | Volcengine |
| E-commerce Agents | ✅ | No | No |
| Browser Automation | ✅ | No | No |
| Visual Workflow | ✅ | No | No |

## Quick Start

### Prerequisites

- JDK 17+
- Python 3.10+
- Node.js 18+
- Docker (optional)

### Installation

```bash
# 1. Clone the repository
git clone https://github.com/503496348-ops/purepicks.git
cd purepicks

# 2. Configure environment
cp purepicks-backend/src/main/resources/application.yml.example purepicks-backend/src/main/resources/application.yml
# Edit config file, add your LLM API Key

# 3. Start backend
cd purepicks-backend && sh build.sh

# 4. Start frontend
cd ui && sh start.sh

# 5. Start tool service
cd purepicks-tool && python server.py

# 6. Start client
cd purepicks-client && python server.py
```

### Access

Frontend: http://localhost:3000
API: http://localhost:8080

## Agent Catalog

| Agent | Function | Tech Stack |
|-------|----------|------------|
| Report Agent | Generate analysis reports (web/PPT) | LangGraph + LLM |
| Recommendation Agent | E-commerce product recommendations | NL2SQL + RAG |
| Data Agent | Smart query and diagnostic analysis | DGP Protocol + SQL |
| Search Agent | Product search and comparison | Browser-use + Crawl4ai |
| Customer Service Agent | Customer service and Q&A | RAG + A2A |
| File Agent | File processing and OCR | OCR + CV |

## Documentation

- [Deployment Guide](Deploy.md)
- [Multi-modal RAG](README_mrag.md)
- [Data Agent](README_DataAgent.md)
- [API Documentation](docs/)

## Contributing

Contributions welcome! Please read the [Contributor Guide](contributor_EN.pdf).

## License

Apache-2.0

## Copyright

Copyright 2026 AtomCollide-智械工坊

---

**PurePicks** — Making e-commerce recommendations smarter
