package com.atomcollide.purepicks.agent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 多平台比价响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceCompareResponse {
    private String requestId;
    private Object data;
    private Boolean isFinal;
    private List<CodeInterpreterResponse.FileInfo> fileInfo;
}
