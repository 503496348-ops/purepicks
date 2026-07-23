---
name: purepicks
description: "自然良品电商推荐 — Table RAG 搜索、商品洞察分析、MCP 服务"
triggers:
  - "电商"
  - "商品推荐"
  - "表格检索"
  - "RAG"
  - "purepicks"
  - "自然良品"
---

# PurePicks — 自然良品电商推荐

基于 Table RAG 的商品搜索 + 洞察分析 + MCP 服务端。

## 核心能力

| 命令 | 说明 |
|------|------|
| `purepicks search <query>` | Table RAG 商品搜索 |
| `purepicks analyze` | 商品洞察分析 |
| `purepicks serve` | 启动 MCP 服务器 |
| `purepicks info` | 产品信息 |

## 快速开始

```bash
# 搜索商品
python3 scripts/cli.py search "有机牛奶" --limit 10

# 启动 MCP 服务
python3 scripts/cli.py serve --port 8188

# 查看产品信息
python3 scripts/cli.py info
```

## 架构

- `purepicks-tool/` — 核心工具包（Table RAG + 分析组件）
- `purepicks-client/` — MCP 客户端服务（FastAPI）
- `scripts/cli.py` — 统一 CLI 入口

## 测试

```bash
python3 -m pytest tests/ -q
```
