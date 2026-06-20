package com.atomcollide.purepicks.agent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 多平台比价请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceCompareRequest {
    private String request_id;
    private String task;
    private String product_name;
    private List<String> platforms;
    private Boolean stream;
}
