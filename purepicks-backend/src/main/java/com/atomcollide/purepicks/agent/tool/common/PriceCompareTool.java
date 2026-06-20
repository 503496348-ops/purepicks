package com.atomcollide.purepicks.agent.tool.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atomcollide.purepicks.agent.agent.AgentContext;
import com.atomcollide.purepicks.agent.dto.CodeInterpreterResponse;
import com.atomcollide.purepicks.agent.dto.File;
import com.atomcollide.purepicks.agent.dto.PriceCompareRequest;
import com.atomcollide.purepicks.agent.dto.PriceCompareResponse;
import com.atomcollide.purepicks.agent.tool.BaseTool;
import com.atomcollide.purepicks.agent.util.SpringContextHolder;
import com.atomcollide.purepicks.agent.util.StringUtil;
import com.atomcollide.purepicks.config.purepicksConfig;
import com.atomcollide.purepicks.model.response.AgentResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.context.ApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 多平台商品比价工具
 * 通过搜索引擎采集多平台价格信息，结合LLM分析生成比价报告
 * Brand: AtomCollide-智械工坊
 */
@Slf4j
@Data
public class PriceCompareTool implements BaseTool {
    private AgentContext agentContext;

    @Override
    public String getName() {
        return "price_compare";
    }

    @Override
    public String getDescription() {
        return "这是一个多平台商品比价工具，可以对比商品在京东、淘宝、拼多多、天猫、抖音商城等平台的价格、优惠活动并给出购买建议";
    }

    @Override
    public Map<String, Object> toParams() {
        Map<String, Object> taskParam = new HashMap<>();
        taskParam.put("type", "string");
        taskParam.put("description", "比价任务描述");

        Map<String, Object> productNameParam = new HashMap<>();
        productNameParam.put("type", "string");
        productNameParam.put("description", "商品名称");

        Map<String, Object> platformsParam = new HashMap<>();
        platformsParam.put("type", "array");
        platformsParam.put("items", Map.of("type", "string"));
        platformsParam.put("description", "目标平台列表，如京东、淘宝、拼多多等");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("type", "object");
        Map<String, Object> properties = new HashMap<>();
        properties.put("task", taskParam);
        properties.put("productName", productNameParam);
        properties.put("platforms", platformsParam);
        parameters.put("properties", properties);
        parameters.put("required", Arrays.asList("task", "productName"));

        return parameters;
    }

    @Override
    public Object execute(Object input) {
        try {
            Map<String, Object> params = (Map<String, Object>) input;
            String task = (String) params.getOrDefault("task", "");
            String productName = (String) params.getOrDefault("productName", "");
            List<String> platforms = (List<String>) params.getOrDefault("platforms", null);

            PriceCompareRequest request = PriceCompareRequest.builder()
                    .request_id(agentContext.getSessionId())
                    .task(task)
                    .product_name(productName)
                    .platforms(platforms)
                    .stream(true)
                    .build();

            Future<String> future = callPriceCompareStream(request);
            return future.get();
        } catch (Exception e) {
            log.error("{} price_compare agent error", agentContext.getRequestId(), e);
        }
        agentContext.getPrinter().send("tool_result", AgentResponse.ToolResult.builder()
                .toolName("比价分析智能体")
                .toolParam(new HashMap<>())
                .toolResult("执行失败")
                .build());
        return null;
    }

    /**
     * 调用比价分析API
     */
    public CompletableFuture<String> callPriceCompareStream(PriceCompareRequest compareRequest) {
        CompletableFuture<String> future = new CompletableFuture<>();
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(300, TimeUnit.SECONDS)
                    .writeTimeout(300, TimeUnit.SECONDS)
                    .callTimeout(300, TimeUnit.SECONDS)
                    .build();

            ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
            purepicksConfig config = applicationContext.getBean(purepicksConfig.class);
            String url = config.getDataAnalysisUrl() + "/v1/tool/price_compare";

            RequestBody body = RequestBody.create(
                    JSONObject.toJSONString(compareRequest),
                    MediaType.parse("application/json")
            );

            log.info("{} price_compare request {}", agentContext.getRequestId(), JSONObject.toJSONString(compareRequest));
            Request.Builder requestBuilder = new Request.Builder().url(url).post(body);
            Request request = requestBuilder.build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    log.error("{} price_compare on failure", agentContext.getRequestId(), e);
                    future.completeExceptionally(e);
                }

                @Override
                public void onResponse(Call call, Response response) {
                    try (ResponseBody responseBody = response.body()) {
                        if (!response.isSuccessful() || responseBody == null) {
                            log.error("{} price_compare request error", agentContext.getRequestId());
                            future.completeExceptionally(new IOException("Unexpected response code: " + response));
                            return;
                        }

                        String line;
                        BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody.byteStream()));
                        String digitalEmployee = agentContext.getToolCollection().getDigitalEmployee(getName());
                        String result = "比价结果为空";
                        String messageId = StringUtil.getUUID();
                        StringBuilder fullContentBuilder = new StringBuilder();

                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith("data: ")) {
                                String data = line.substring(6);
                                if (data.equals("[DONE]")) break;
                                if (data.equals("heartbeat")) continue;

                                try {
                                    PriceCompareResponse compareResponse = JSONObject.parseObject(data, PriceCompareResponse.class);
                                    fullContentBuilder.append(compareResponse.getData()).append("\n");

                                    if (Boolean.TRUE.equals(compareResponse.getIsFinal())) {
                                        compareResponse.setData(fullContentBuilder.toString());
                                        agentContext.getPrinter().send(messageId, "price_compare",
                                                compareResponse, digitalEmployee, true);
                                        result = fullContentBuilder.toString();
                                    } else {
                                        agentContext.getPrinter().send(messageId, "price_compare",
                                                compareResponse, digitalEmployee, false);
                                    }
                                } catch (Exception parseException) {
                                    log.warn("{} price_compare parse error: {}", agentContext.getRequestId(), parseException.getMessage());
                                }
                            }
                        }
                        future.complete(result);
                    } catch (Exception e) {
                        log.error("{} price_compare request error", agentContext.getRequestId(), e);
                        future.completeExceptionally(e);
                    }
                }
            });
        } catch (Exception e) {
            log.error("{} price_compare request error", agentContext.getRequestId(), e);
            future.completeExceptionally(e);
        }
        return future;
    }
}
