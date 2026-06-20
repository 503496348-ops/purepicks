package com.atomcollide.purepicks.agent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 评论情感分析响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SentimentAnalysisResponse {
    private String requestId;
    private Object data;
    private Boolean isFinal;
    private List<CodeInterpreterResponse.FileInfo> fileInfo;
}
