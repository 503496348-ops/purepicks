package com.atomcollide.purepicks.service;

import com.atomcollide.purepicks.agent.agent.AgentContext;
import com.atomcollide.purepicks.model.req.AgentRequest;

public interface AgentHandlerService {

    /**
     * 处理Agent请求
     */
    String handle(AgentContext context, AgentRequest request);

    /**
     * 进入handler条件
     */
    Boolean support(AgentContext context, AgentRequest request);

}