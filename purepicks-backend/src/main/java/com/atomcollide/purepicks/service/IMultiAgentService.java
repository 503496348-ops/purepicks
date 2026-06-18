package com.atomcollide.purepicks.service;

import com.atomcollide.purepicks.model.dto.AutoBotsResult;
import com.atomcollide.purepicks.model.req.GptQueryReq;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface IMultiAgentService {
    /**
     * 请求多 agent发送请求入口函数.
     * @param gptQueryReq
     * @param sseEmitter
     * @return
     */
    AutoBotsResult searchForAgentRequest(GptQueryReq gptQueryReq, SseEmitter sseEmitter);
}
