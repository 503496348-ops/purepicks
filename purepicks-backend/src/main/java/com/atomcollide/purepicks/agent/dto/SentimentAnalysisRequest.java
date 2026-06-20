package com.atomcollide.purepicks.agent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 评论情感分析请求
 * 用于对商品评论进行多维度情感分析
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SentimentAnalysisRequest {
    private String request_id;
    private String task;
    private List<String> reviews;
    private String product_name;
    private Boolean stream;
}
