package com.atomcollide.purepicks.agent.agent;

import com.atomcollide.purepicks.agent.dto.File;
import com.atomcollide.purepicks.agent.printer.Printer;
import com.atomcollide.purepicks.agent.tool.ToolCollection;
import com.atomcollide.purepicks.model.dto.FileInformation;
import com.atomcollide.purepicks.model.req.AgentRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Builder
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class AgentContext {
    String requestId;
    String sessionId;
    String query;
    String task;
    Printer printer;
    ToolCollection toolCollection;
    String dateInfo;
    List<File> productFiles;
    Boolean isStream;
    String streamMessageType;
    String sopPrompt;
    String basePrompt;
    Integer agentType;
    List<File> taskProductFiles;
    String templateType;
}