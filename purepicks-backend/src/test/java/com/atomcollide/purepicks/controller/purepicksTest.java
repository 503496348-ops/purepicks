package com.atomcollide.purepicks.controller;

import com.atomcollide.purepicks.agent.tool.mcp.McpTool;
import com.atomcollide.purepicks.agent.util.SpringContextHolder;
import com.atomcollide.purepicks.config.purepicksConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
public class purepicksTest {

    @Test
    public void mcpToolTest() {

        purepicksConfig purepicksConfig = SpringContextHolder.getApplicationContext().getBean(purepicksConfig.class);
        log.info("{} {}", purepicksConfig.getMcpClientUrl(), purepicksConfig.getMcpServerUrlArr());
        if (purepicksConfig.getMcpServerUrlArr().length > 0) {
            String mcpServerUrl = purepicksConfig.getMcpServerUrlArr()[0];

            // time mcp tool
            McpTool tool = new McpTool();
            String listResult = tool.listTool(mcpServerUrl);
            log.info("list tool result {}", listResult);

            Map<String, String> input = new HashMap<>();
            input.put("timezone", "America/New_York");
            String callRsult = tool.callTool(mcpServerUrl, "get_current_time", input);
            log.info("call tool result {}", callRsult);
        }
    }
}