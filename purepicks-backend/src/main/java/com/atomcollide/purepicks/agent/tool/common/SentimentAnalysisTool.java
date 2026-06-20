package com.atomcollide.purepicks.agent.tool.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atomcollide.purepicks.agent.agent.AgentContext;
import com.atomcollide.purepicks.agent.dto.CodeInterpreterResponse;
import com.atomcollide.purepicks.agent.dto.File;
import com.atomcollide.purepicks.agent.dto.SentimentAnalysisRequest;
import com.atomcollide.purepicks.agent.dto.SentimentAnalysisResponse;
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
 * 商品评论情感分析工具
 * 对用户评论进行多维度情感分析，提取关键主题和购买建议
 * Brand: AtomCollide-智械工坊
 */
@Slf4j
@Data
public class SentimentAnalysisTool implements BaseTool {
    private AgentContext agentContext;

    @Override
    public String getName() {
        return "sentiment_analysis";
    }

    @Override
    public String getDescription() {
        return "这是一个商品评论情感分析工具，可以对用户评论进行情感分析，提取正面/负面关键词、情感分布和购买建议";
    }

    @Override
    public Map<String, Object> toParams() {
        Map<String, Object> taskParam = new HashMap<>();
        taskParam.put("type", "string");
        taskParam.put("description", "分析任务描述");

        Map<String, Object> reviewsParam = new HashMap<>();
        reviewsParam.put("type", "array");
        reviewsParam.put("items", Map.of("type", "string"));
        reviewsParam.put("description", "评论列表");

        Map<String, Object> productNameParam = new HashMap<>();
        productNameParam.put("type", "string");
        productNameParam.put("description", "商品名称");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("type", "object");
        Map<String, Object> properties = new HashMap<>();
        properties.put("task", taskParam);
        properties.put("reviews", reviewsParam);
        properties.put("productName", productNameParam);
        parameters.put("properties", properties);
        parameters.put("required", Arrays.asList("task", "reviews"));

        return parameters;
    }

    @Override
    public Object execute(Object input) {
        try {
            Map<String, Object> params = (Map<String, Object>) input;
            String task = (String) params.getOrDefault("task", "");
            List<String> reviews = (List<String>) params.getOrDefault("reviews", Collections.emptyList());
            String productName = (String) params.getOrDefault("productName", "");

            SentimentAnalysisRequest request = SentimentAnalysisRequest.builder()
                    .request_id(agentContext.getSessionId())
                    .task(task)
                    .reviews(reviews)
                    .product_name(productName)
                    .stream(true)
                    .build();

            Future<String> future = callSentimentAnalysisStream(request);
            return future.get();
        } catch (Exception e) {
            log.error("{} sentiment_analysis agent error", agentContext.getRequestId(), e);
        }
        agentContext.getPrinter().send("tool_result", AgentResponse.ToolResult.builder()
                .toolName("情感分析智能体")
                .toolParam(new HashMap<>())
                .toolResult("执行失败")
                .build());
        return null;
    }

    /**
     * 调用情感分析API
     */
    public CompletableFuture<String> callSentimentAnalysisStream(SentimentAnalysisRequest analysisRequest) {
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
            String url = config.getDataAnalysisUrl() + "/v1/tool/sentiment_analysis";

            RequestBody body = RequestBody.create(
                    JSONObject.toJSONString(analysisRequest),
                    MediaType.parse("application/json")
            );

            log.info("{} sentiment_analysis request {}", agentContext.getRequestId(), JSONObject.toJSONString(analysisRequest));
            Request.Builder requestBuilder = new Request.Builder().url(url).post(body);
            Request request = requestBuilder.build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    log.error("{} sentiment_analysis on failure", agentContext.getRequestId(), e);
                    future.completeExceptionally(e);
                }

                @Override
                public void onResponse(Call call, Response response) {
                    try (ResponseBody responseBody = response.body()) {
                        if (!response.isSuccessful() || responseBody == null) {
                            log.error("{} sentiment_analysis request error", agentContext.getRequestId());
                            future.completeExceptionally(new IOException("Unexpected response code: " + response));
                            return;
                        }

                        String line;
                        BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody.byteStream()));
                        String digitalEmployee = agentContext.getToolCollection().getDigitalEmployee(getName());
                        String result = "分析结果为空";
                        String messageId = StringUtil.getUUID();
                        StringBuilder fullContentBuilder = new StringBuilder();

                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith("data: ")) {
                                String data = line.substring(6);
                                if (data.equals("[DONE]")) break;
                                if (data.equals("heartbeat")) continue;

                                try {
                                    SentimentAnalysisResponse analysisResponse = JSONObject.parseObject(data, SentimentAnalysisResponse.class);
                                    fullContentBuilder.append(analysisResponse.getData()).append("\n");

                                    if (Boolean.TRUE.equals(analysisResponse.getIsFinal())) {
                                        analysisResponse.setData(fullContentBuilder.toString());
                                        agentContext.getPrinter().send(messageId, "sentiment_analysis",
                                                analysisResponse, digitalEmployee, true);
                                        result = fullContentBuilder.toString();
                                    } else {
                                        agentContext.getPrinter().send(messageId, "sentiment_analysis",
                                                analysisResponse, digitalEmployee, false);
                                    }
                                } catch (Exception parseException) {
                                    log.warn("{} sentiment_analysis parse error: {}", agentContext.getRequestId(), parseException.getMessage());
                                }
                            }
                        }
                        future.complete(result);
                    } catch (Exception e) {
                        log.error("{} sentiment_analysis request error", agentContext.getRequestId(), e);
                        future.completeExceptionally(e);
                    }
                }
            });
        } catch (Exception e) {
            log.error("{} sentiment_analysis request error", agentContext.getRequestId(), e);
            future.completeExceptionally(e);
        }
        return future;
    }
}
