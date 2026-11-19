package com.portfolio.devops;

import com.portfolio.devops.tools.IncidentTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DevopsMcpApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevopsMcpApplication.class, args);
    }

    @Bean
    public ToolCallbackProvider devopsTools(IncidentTools incidentTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(incidentTools)
                .build();
    }
}
