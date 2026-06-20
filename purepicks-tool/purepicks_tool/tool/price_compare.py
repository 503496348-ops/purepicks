# -*- coding: utf-8 -*-
# =====================
# PurePicks - Multi-Platform Price Comparison Tool
# Brand: AtomCollide-智械工坊
# Date:   2026/06/20
# =====================
import asyncio
import json
import os
from typing import Dict, List, Optional, Tuple

from dotenv import load_dotenv
from jinja2 import Template
from loguru import logger

from purepicks_tool.util.llm_util import ask_llm
from purepicks_tool.util.prompt_util import get_prompt
from purepicks_tool.util.file_util import upload_file
from purepicks_tool.util.log_util import timer
from purepicks_tool.tool.deepsearch import DeepSearch

load_dotenv()

RESULT_TEMPLATE = """# {{ task }} — 多平台比价报告

## 商品概览
- 商品名称: {{ product_name }}
- 采集平台数: {{ platform_count }}
- 数据采集时间: {{ timestamp }}

## 价格对比
| 平台 | 价格(元) | 优惠活动 | 商品链接 | 评分 |
|------|---------|---------|---------|------|
{% for item in price_list -%}
| {{ item.platform }} | {{ item.price }} | {{ item.promotion }} | {{ item.link }} | {{ item.rating }} |
{% endfor %}

## 价格分析
- 最低价: {{ min_price.platform }} ¥{{ min_price.price }}
- 最高价: {{ max_price.platform }} ¥{{ max_price.price }}
- 价差: ¥{{ price_diff }}
- 平均价: ¥{{ avg_price }}

## 核心发现
{% for finding in key_findings -%}
- {{ finding }}
{% endfor %}

## 购买建议
{{ purchase_recommendation }}
"""


class PriceCompareAgent:
    """多平台商品比价 Agent

    通过搜索引擎采集多平台价格信息，结合 LLM 分析生成比价报告。
    """

    def __init__(self, queue: asyncio.Queue = None):
        self.queue = queue or asyncio.Queue()
        self.model = os.getenv("PRICE_COMPARE_MODEL", os.getenv("NL2SQL_MODEL_NAME", "gpt-4.1"))
        self.temperature: float = 0.1
        self.top_p: float = 0.9
        self.default_platforms = ["京东", "淘宝", "拼多多", "天猫", "抖音商城"]

    @timer(key="price_compare")
    async def run(
        self,
        task: str,
        product_name: str,
        request_id: str,
        platforms: Optional[List[str]] = None,
        stream: bool = True,
        **kwargs,
    ) -> Dict:
        """Execute multi-platform price comparison.

        Args:
            task: Comparison task description.
            product_name: Product name to compare.
            request_id: Request tracking ID.
            platforms: List of platforms to search.
            stream: Whether to stream results.

        Returns:
            Dict containing price comparison results.
        """
        try:
            target_platforms = platforms or self.default_platforms
            logger.info(f"[PriceCompare] request_id={request_id} product={product_name} platforms={target_platforms}")

            if stream:
                await self.queue.put({
                    "requestId": request_id,
                    "data": f"# 多平台比价任务\n{task}\n\n正在采集 {product_name} 在 {', '.join(target_platforms)} 的价格信息...\n",
                    "isFinal": False,
                })

            # Step 1: Gather price data via search
            search_results = await self._search_prices(product_name, target_platforms, request_id, stream)

            # Step 2: Analyze with LLM
            if stream:
                await self.queue.put({
                    "requestId": request_id,
                    "data": "\n价格数据采集完成，正在分析...\n",
                    "isFinal": False,
                })

            parsed = await self._analyze_prices(task, product_name, search_results, target_platforms)

            # Step 3: Upload result file
            from datetime import datetime
            parsed["timestamp"] = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
            report_content = Template(RESULT_TEMPLATE).render(**parsed)
            file_info = await upload_file(
                request_id=request_id,
                content=report_content,
                file_name=f"{product_name}_多平台比价报告",
                file_type="txt",
            )
            if not isinstance(file_info, list):
                file_info = [file_info]

            if stream:
                await self.queue.put({
                    "requestId": request_id,
                    "data": f"\n# 比价结论\n\n{parsed.get('purchase_recommendation', '暂无建议')}\n",
                    "file_info": file_info,
                    "isFinal": True,
                })

            return parsed

        except Exception as e:
            logger.error(f"[PriceCompare] request_id={request_id} error: {e}")
            if stream:
                await self.queue.put({
                    "requestId": request_id,
                    "data": {"error": str(e)},
                    "isFinal": True,
                })
            return {"error": str(e)}
        finally:
            await self.queue.put("[DONE]")

    async def _search_prices(
        self, product_name: str, platforms: List[str], request_id: str, stream: bool
    ) -> str:
        """Search for price information across platforms."""
        queries = [f"{product_name} {platform} 价格 优惠" for platform in platforms]
        # Use DeepSearch for price gathering
        deep_search = DeepSearch()
        all_content = ""

        for query in queries:
            try:
                async for chunk in deep_search.run(
                    query=query,
                    request_id=f"{request_id}:price",
                    max_loop=1,
                    stream=False,
                ):
                    if isinstance(chunk, str):
                        try:
                            data = json.loads(chunk)
                            if data.get("isFinal") and data.get("answer"):
                                all_content += f"\n---\n搜索: {query}\n{data['answer']}\n"
                        except json.JSONDecodeError:
                            pass
            except Exception as e:
                logger.warning(f"[PriceCompare] search failed for {query}: {e}")

        # If deep search returned nothing, use direct LLM knowledge
        if not all_content.strip():
            logger.info(f"[PriceCompare] DeepSearch returned no results, using LLM knowledge")
            all_content = f"请基于你的知识，提供 {product_name} 在各平台的价格信息。"

        return all_content

    @timer(key="analyze_prices")
    async def _analyze_prices(
        self, task: str, product_name: str, search_results: str, platforms: List[str]
    ) -> Dict:
        """Analyze price data with LLM and generate structured comparison."""
        prompt = f"""# 角色
你是一位专业的电商价格分析师，擅长从多平台价格数据中提取关键信息并给出购买建议。

# 任务
{task}

# 商品名称
{product_name}

# 采集平台
{', '.join(platforms)}

# 搜索采集到的价格数据
{search_results[:8000]}

# 分析要求
请从采集数据中提取各平台的价格信息，并以JSON格式输出：

1. **price_list**: 每个平台的价格信息（platform, price, promotion, link, rating）
2. **key_findings**: 3-5条核心发现
3. **purchase_recommendation**: 购买建议（100字以内）

# 输出格式（严格JSON）
```json
{{
    "product_name": "{product_name}",
    "platform_count": {len(platforms)},
    "price_list": [
        {{"platform": "京东", "price": "999.00", "promotion": "满减/无", "link": "https://...", "rating": "4.8"}},
        {{"platform": "淘宝", "price": "899.00", "promotion": "优惠券/无", "link": "https://...", "rating": "4.7"}}
    ],
    "min_price": {{"platform": "平台名", "price": "899.00"}},
    "max_price": {{"platform": "平台名", "price": "999.00"}},
    "price_diff": "100.00",
    "avg_price": "949.00",
    "key_findings": ["发现1", "发现2", "发现3"],
    "purchase_recommendation": "购买建议文本"
}}
```

输出："""

        result = ""
        async for chunk in ask_llm(
            messages=prompt,
            model=self.model,
            stream=False,
            temperature=self.temperature,
            top_p=self.top_p,
            only_content=True,
        ):
            result = chunk

        return self._parse_result(result, product_name, len(platforms), task)

    def _parse_result(self, result: str, product_name: str, platform_count: int, task: str) -> Dict:
        """Parse LLM result into structured data."""
        default = {
            "task": task,
            "product_name": product_name,
            "platform_count": platform_count,
            "price_list": [],
            "min_price": {"platform": "-", "price": "-"},
            "max_price": {"platform": "-", "price": "-"},
            "price_diff": "-",
            "avg_price": "-",
            "key_findings": ["暂无分析结果"],
            "purchase_recommendation": result[:300] if result else "暂无建议",
            "timestamp": "",
        }

        try:
            json_str = result
            if "```json" in json_str:
                json_str = json_str.split("```json")[1].split("```")[0].strip()
            elif "```" in json_str:
                json_str = json_str.split("```")[1].split("```")[0].strip()

            parsed = json.loads(json_str)
            parsed["task"] = task
            parsed.setdefault("product_name", product_name)
            parsed.setdefault("platform_count", platform_count)
            return parsed
        except (json.JSONDecodeError, IndexError) as e:
            logger.warning(f"[PriceCompare] Failed to parse JSON: {e}")
            default["purchase_recommendation"] = result[:500] if result else "暂无建议"
            return default
