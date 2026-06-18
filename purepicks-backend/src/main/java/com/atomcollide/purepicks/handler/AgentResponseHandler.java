package com.atomcollide.purepicks.handler;

import com.atomcollide.purepicks.model.multi.EventResult;
import com.atomcollide.purepicks.model.req.AgentRequest;
import com.atomcollide.purepicks.model.response.AgentResponse;
import com.atomcollide.purepicks.model.response.GptProcessResult;

import java.util.List;

public interface AgentResponseHandler {
    GptProcessResult handle(AgentRequest request,
                AgentResponse response,
                List<AgentResponse> agentRespList,
                EventResult eventResult);
}
