# -*- coding: utf-8 -*-
# =====================
# PurePicks - Review Sentiment Analysis Tool
# Brand: AtomCollide-智械工坊
# Date:   2026/06/20
# =====================
import asyncio
import json
import os
from typing import Dict, List, Optional, AsyncGenerator

from dotenv import load_dotenv
from jinja2 import Template
from loguru import logger

from purepicks_tool.util.llm_util import ask_llm
from purepicks_tool.util.prompt_util import get_prompt
from purepicks_tool.util.file_util import upload_file
from purepicks_tool.util.log_util import timer

load_dotenv()

RESULT_TEMPLATE = """# {{ task }} — 评论情感分析报告

## 概览
- 分析评论总数: {{ total_count }}
- 整体情感倾向: {{ overall_sentiment }}

## 情感分布
| 情感类别 | 数量 | 占比 |
|---------|------|------|
{% for item in sentiment_distribution -%}
| {{ item.label }} | {{ item.count }} | {{ item.percentage }} |
{% endfor %}

## 正面反馈 TOP 关键词
{% for kw in positive_keywords -%}
- {{ kw }}
{% endfor %}

## 负面反馈 TOP 关键词
{% for kw in negative_keywords -%}
- {{ kw }}
{% endfor %}

## 核心发现
{% for finding in key_findings -%}
- {{ finding }}
{% endfor %}

## 购买建议
{{ purchase_recommendation }}
"""


class SentimentAnalysisAgent:
    """商品评论情感分析 Agent

    基于 LLM 对用户评论进行多维度情感分析，提取关键主题和购买建议。
    """

    def __init__(self, queue: asyncio.Queue = None):
        self.queue = queue or asyncio.Queue()
        self.model = os.getenv("SENTIMENT_MODEL", os.getenv("NL2SQL_MODEL_NAME", "gpt-4.1"))
        self.temperature: float = 0.1
        self.top_p: float = 0.9

    @timer(key="sentiment_analysis")
    async def run(
        self,
        task: str,
        reviews: List[str],
        request_id: str,
        product_name: Optional[str] = None,
        stream: bool = True,
        **kwargs,
    ) -> Dict:
        """Execute sentiment analysis on product reviews.

        Args:
            task: Analysis task description.
            reviews: List of review texts.
            request_id: Request tracking ID.
            product_name: Optional product name for context.
            stream: Whether to stream results.

        Returns:
            Dict containing sentiment analysis results.
        """
        try:
            logger.info(f"[Sentiment] request_id={request_id} task={task} review_count={len(reviews)}")

            # Stream progress
            if stream:
                await self.queue.put({
                    "requestId": request_id,
                    "data": f"# 情感分析任务\n{task}\n\n正在分析 {len(reviews)} 条评论...\n",
                    "isFinal": False,
                })

            # Build prompt
            prompt = self._build_prompt(task, reviews, product_name)

            # Call LLM for analysis
            analysis_result = ""
            async for chunk in ask_llm(
                messages=prompt,
                model=self.model,
                stream=False,
                temperature=self.temperature,
                top_p=self.top_p,
                only_content=True,
            ):
                analysis_result = chunk

            logger.info(f"[Sentiment] request_id={request_id} analysis completed, length={len(analysis_result)}")

            # Parse structured result
            parsed = self._parse_result(analysis_result, reviews, task)

            # Upload result file
            report_content = Template(RESULT_TEMPLATE).render(**parsed)
            file_info = await upload_file(
                request_id=request_id,
                content=report_content,
                file_name=f"{task}_情感分析报告",
                file_type="txt",
            )
            if not isinstance(file_info, list):
                file_info = [file_info]

            # Send final result
            if stream:
                await self.queue.put({
                    "requestId": request_id,
                    "data": f"\n# 分析结论\n\n{parsed.get('purchase_recommendation', analysis_result)}\n",
                    "file_info": file_info,
                    "isFinal": True,
                })

            return parsed

        except Exception as e:
            logger.error(f"[Sentiment] request_id={request_id} error: {e}")
            if stream:
                await self.queue.put({
                    "requestId": request_id,
                    "data": {"error": str(e)},
                    "isFinal": True,
                })
            return {"error": str(e)}
        finally:
            await self.queue.put("[DONE]")

    def _build_prompt(self, task: str, reviews: List[str], product_name: Optional[str]) -> str:
        """Build the sentiment analysis prompt."""
        reviews_text = "\n".join(
            [f"[评论{i+1}] {r}" for i, r in enumerate(reviews)]
        )
        product_context = f"商品名称: {product_name}\n" if product_name else ""

        return f"""# 角色
你是一位资深的电商评论情感分析专家，擅长从用户评论中提取情感倾向、关键主题和购买建议。

# 任务
{task}

# {product_context}
# 评论数据（共{len(reviews)}条）
{reviews_text}

# 分析要求
请从以下维度进行分析，并以JSON格式输出：

1. **情感分布**: 统计正面、中性、负面评论的数量和占比
2. **正面关键词**: 提取正面评论中出现频率最高的5个关键词
3. **负面关键词**: 提取负面评论中出现频率最高的5个关键词
4. **核心发现**: 总结3-5条最重要的发现
5. **整体情感倾向**: 综合判断为"正面"/"中性"/"负面"
6. **购买建议**: 基于评论分析给出简明的购买建议（100字以内）

# 输出格式（严格JSON）
```json
{{
    "sentiment_distribution": [
        {{"label": "正面", "count": 0, "percentage": "0%"}},
        {{"label": "中性", "count": 0, "percentage": "0%"}},
        {{"label": "负面", "count": 0, "percentage": "0%"}}
    ],
    "positive_keywords": ["关键词1", "关键词2", "关键词3", "关键词4", "关键词5"],
    "negative_keywords": ["关键词1", "关键词2", "关键词3", "关键词4", "关键词5"],
    "key_findings": ["发现1", "发现2", "发现3"],
    "overall_sentiment": "正面/中性/负面",
    "purchase_recommendation": "购买建议文本"
}}
```

输出："""

    def _parse_result(self, analysis_result: str, reviews: List[str], task: str) -> Dict:
        """Parse LLM analysis result into structured data."""
        default_result = {
            "task": task,
            "total_count": len(reviews),
            "overall_sentiment": "中性",
            "sentiment_distribution": [
                {"label": "正面", "count": 0, "percentage": "0%"},
                {"label": "中性", "count": len(reviews), "percentage": "100%"},
                {"label": "负面", "count": 0, "percentage": "0%"},
            ],
            "positive_keywords": [],
            "negative_keywords": [],
            "key_findings": ["暂无分析结果"],
            "purchase_recommendation": analysis_result[:200] if analysis_result else "暂无建议",
        }

        try:
            # Try to extract JSON from response
            json_str = analysis_result
            # Handle markdown code blocks
            if "```json" in json_str:
                json_str = json_str.split("```json")[1].split("```")[0].strip()
            elif "```" in json_str:
                json_str = json_str.split("```")[1].split("```")[0].strip()

            parsed = json.loads(json_str)
            parsed["task"] = task
            parsed["total_count"] = len(reviews)
            return parsed
        except (json.JSONDecodeError, IndexError) as e:
            logger.warning(f"[Sentiment] Failed to parse JSON result: {e}")
            default_result["purchase_recommendation"] = analysis_result[:500] if analysis_result else "暂无建议"
            return default_result
