## 一键安装 / One-click Quickstart

```bash
bash install.sh
python3 scripts/doctor.py
python3 scripts/smoke.py
```

- `bash install.sh`：自动执行 setup + smoke，适合第一次使用。
- `python3 scripts/doctor.py`：检查环境、入口文件和产品门禁，失败时给出修复建议。
- `python3 scripts/smoke.py`：执行产品收敛门禁和轻量核心冒烟验证。

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

|| Agent | 功能 | 技术栈 |
||-------|------|--------|
|| 报告Agent | 生成分析报告（网页/PPT） | LangGraph + LLM |
|| 推荐Agent | 电商产品推荐 | NL2SQL + RAG |
|| 数据Agent | 智能问数与诊断分析 | DGP协议 + SQL |
|| 搜索Agent | 产品搜索与对比 | Browser-use + Crawl4ai |
|| 客服Agent | 客户服务与问答 | RAG + A2A |
|| 文件Agent | 文件处理与OCR | OCR + CV |
|| 情感分析Agent | 商品评论情感分析 | LLM + NLP |
|| 比价Agent | 多平台商品比价 | DeepSearch + LLM |

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

---



---

## 🚀 加入AtomCollide-AI智能体实验室

**元素碰撞-AtomCollide-AI 智能体实验室** 是一个专注于AI领域的开源组织，汇聚了众多优秀学习者。

### 核心价值

**找工作：更省力，也更精准**
- 一线大厂内推通道（字节、阿里、腾讯等）
- 全链路求职赋能包（面试题库、简历优化、晋升指导）
- 线下技术沙龙 & 人脉网络

**学AI测试：真正落地，拒绝空谈**
- 从0到1实战落地体系（Skills、MCP、RAG、AI IDE等）
- 独家自研资料与工具矩阵
- 前沿技术同步与提效方案

### 知识库

- [踩坑合集](https://vcnvmnln7wit.feishu.cn/wiki/CjV9wG8IHiIpWikCdFEcxfErnne)
- [商业化案例库](https://vcnvmnln7wit.feishu.cn/wiki/LdIxwlrKGibFEVkWMocc2K9KnBh)
- [科普专栏](https://vcnvmnln7wit.feishu.cn/wiki/K1RPwM8zji9ZchkxlOmcivUgnJe)
- [Open Build](https://vcnvmnln7wit.feishu.cn/wiki/CThswol0PiNJJbkhgT1cZIxanLb)
- [LLM/Agent/研究报告知识库](https://vcnvmnln7wit.feishu.cn/wiki/KwGQwS2TciT2EdkSBBtcYnbsnSd)
- [Skill封装合集](https://vcnvmnln7wit.feishu.cn/wiki/PDfpwqJZUibTyBkUa7TcZZ6Onpd)
- [社区治理运营知识库](https://vcnvmnln7wit.feishu.cn/wiki/MSEGwrdnTiiF9Dk8qCVcNW6InJg)

### 加入社群

| 社群 | 链接 |
|------|------|
| AI探索交流1区 | [加入](https://applink.feishu.cn/client/chat/chatter/add_by_link?link_token=074vd565-6084-455c-ac52-9703e89a0697) |
| AI探索交流2区 | [加入](https://applink.feishu.cn/client/chat/chatter/add_by_link?link_token=60bj94f0-1a67-48a7-abbb-9172b161c2b0) |
| AI探索交流3区 | [加入](https://applink.feishu.cn/client/chat/chatter/add_by_link?link_token=13do1920-db46-4444-b635-005680beaf58) |
| AI探索交流4区 | [加入](https://applink.feishu.cn/client/chat/chatter/add_by_link?link_token=f17o1b86-06f6-4f10-911a-69a299a25fe3) |
| AI探索交流5区 | [加入](https://applink.feishu.cn/client/chat/chatter/add_by_link?link_token=2bbh6ab6-22c2-4753-b973-74bb1a2edcc9) |
| AI探索交流6区 | [加入](https://applink.feishu.cn/client/chat/chatter/add_by_link?link_token=d19r19f7-2f47-42ba-b1ec-cb0342cf2e80) |
| AI探索交流7区 | [加入](https://applink.feishu.cn/client/chat/chatter/add_by_link?link_token=fe9vdacc-7316-4b4d-ae4a-fdbcf56315e6) |
| AI探索交流8区 | [加入](https://applink.feishu.cn/client/chat/chatter/add_by_link?link_token=103kfae8-1fd7-424f-984f-d66c210e42d1) |
| AI探索交流9区 | [加入](https://applink.feishu.cn/client/chat/chatter/add_by_link?link_token=239p3cad-2f83-4baa-a230-f40386067548) |
| AI探索交流10区 | [加入](https://applink.feishu.cn/client/chat/chatter/add_by_link?link_token=880r7cf5-3638-45ff-afb9-7944de991872) |
| AI探索交流-网文作家 | [加入](https://applink.feishu.cn/client/chat/chatter/add_by_link?link_token=6a3v579b-ab43-4e1a-87f9-be63bab88da7) |
| AI探索交流群-音乐达人 | [加入](https://applink.feishu.cn/client/chat/chatter/add_by_link?link_token=76at299e-73da-4eeb-9eba-32161e98f2f8) |
| AI探索交流群-微笑驿站 | [加入](https://applink.feishu.cn/client/chat/chatter/add_by_link?link_token=f2av73d0-6bb4-4a9f-9095-5fbbe83e49ec) |

---

*AtomCollide-智械工坊团队出品*

---

## 组织与社群入口

**元素碰撞 · AtomCollide-AI 智能体实验室**：面向学习者、创作者与自动化实践者，持续沉淀可复用的 AI Agent 产品、工作流与工程经验。使命：**for the learner**。

> 请选择 1 个常用社群加入，内容全域同步，无需重复加入。

### 知识库

| 知识库 | 链接 |
|---|---|
| 踩坑合集 | [进入](https://vcnvmnln7wit.feishu.cn/wiki/CjV9wG8IHiIpWikCdFEcxfErnne) |
| 商业化案例库 | [进入](https://vcnvmnln7wit.feishu.cn/wiki/LdIxwlrKGibFEVkWMocc2K9KnBh) |
| 科普专栏 | [进入](https://vcnvmnln7wit.feishu.cn/wiki/K1RPwM8zji9ZchkxlOmcivUgnJe) |
| Open Build | [进入](https://vcnvmnln7wit.feishu.cn/wiki/CThswol0PiNJJbkhgT1cZIxanLb) |
| LLM / Agent / 研究报告 | [进入](https://vcnvmnln7wit.feishu.cn/wiki/KwGQwS2TciT2EdkSBBtcYnbsnSd) |
| Skill 封装合集 | [进入](https://vcnvmnln7wit.feishu.cn/wiki/PDfpwqJZUibTyBkUa7TcZZ6Onpd) |
| 社区治理运营 | [进入](https://vcnvmnln7wit.feishu.cn/wiki/MSEGwrdnTiiF9Dk8qCVcNW6InJg) |

### 社群邀请

| 社群 | 链接 |
|---|---|
| AI 探索交流 1 区 | [加入](https://applink.feishu.cn/client/chat/chatter/add_by_link?link_token=074vd565-6084-455c-ac52-9703e89a0697) |
| AI 探索交流 2 区 | [加入](https://applink.feishu.cn/client/chat/chatter/add_by_link?link_token=60bj94f0-1a67-48a7-abbb-9172b161c2b0) |
| AI 探索交流 3 区 | [加入](https://applink.feishu.cn/client/chat/chatter/add_by_link?link_token=13do1920-db46-4444-b635-005680beaf58) |
| AI 探索交流 4 区 | [加入](https://applink.feishu.cn/client/chat/chatter/add_by_link?link_token=f17o1b86-06f6-4f10-911a-69a299a25fe3) |
| AI 探索交流 5 区 | [加入](https://applink.feishu.cn/client/chat/chatter/add_by_link?link_token=2bbh6ab6-22c2-4753-b973-74bb1a2edcc9) |
| AI 探索交流 6 区 | [加入](https://applink.feishu.cn/client/chat/chatter/add_by_link?link_token=d19r19f7-2f47-42ba-b1ec-cb0342cf2e80) |
| AI 探索交流 7 区 | [加入](https://applink.feishu.cn/client/chat/chatter/add_by_link?link_token=fe9vdacc-7316-4b4d-ae4a-fdbcf56315e6) |
| AI 探索交流 8 区 | [加入](https://applink.feishu.cn/client/chat/chatter/add_by_link?link_token=103kfae8-1fd7-424f-984f-d66c210e42d1) |
| AI 探索交流 9 区 | [加入](https://applink.feishu.cn/client/chat/chatter/add_by_link?link_token=239p3cad-2f83-4baa-a230-f40386067548) |
| AI 探索交流 10 区 | [加入](https://applink.feishu.cn/client/chat/chatter/add_by_link?link_token=880r7cf5-3638-45ff-afb9-7944de991872) |
| AI 探索交流 — 网文作家 | [加入](https://applink.feishu.cn/client/chat/chatter/add_by_link?link_token=6a3v579b-ab43-4e1a-87f9-be63bab88da7) |
| AI 探索交流群 — 音乐达人 | [加入](https://applink.feishu.cn/client/chat/chatter/add_by_link?link_token=76at299e-73da-4eeb-9eba-32161e98f2f8) |
| AI 探索交流群 — 微笑驿站 | [加入](https://applink.feishu.cn/client/chat/chatter/add_by_link?link_token=f2av73d0-6bb4-4a9f-9095-5fbbe83e49ec) |

---

AtomCollide-智械工坊团队出品。更多产品见：[AtomCollide Product Matrix](https://503496348-ops.github.io/atomcollide-product-matrix/)。


## 示例输出

本仓库的最小可验证使用路径：

1. 阅读 README 的 Quick Start / 使用说明，完成本地安装或配置。
2. 按仓库提供的命令、脚本或入口运行一次最小任务。
3. 对照本产品定位验证输出：**自然良品（PurePicks）** 属于 **电商推荐** 产品，目标是把输入材料转化为可检查、可复用的结果。
4. 若运行环境暂不可用，先通过 README、CHANGELOG、CI 状态和源码结构完成静态验收，再补充真实截图或录屏。

> 维护要求：后续每次发布都应把真实运行截图、CLI 输出、网页截图或 API 响应样例补充到本节，避免仓库首页只描述能力、不展示结果。

## Governance Links

- [LICENSE](LICENSE)
- [CHANGELOG](CHANGELOG.md)
- [SECURITY](SECURITY.md)
- [CONTRIBUTING](CONTRIBUTING.md)

## 2026-07-03 产品收敛门禁

- 新增 `scripts/product_convergence_gate.py`：从远端干净 clone 后可运行 `python3 scripts/product_convergence_gate.py --json`，检查 SKILL/README、入口文件、smoke 目标、测试与外部融合引用是否自洽。
- 新增 `tests/test_product_convergence_gate.py`：确保门禁在产品仓库中真实可执行，避免后续增强只停留在孤岛模块。
