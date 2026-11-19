package com.portfolio.devops.tools;

import com.portfolio.devops.model.Incident;
import com.portfolio.devops.repository.IncidentRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class IncidentTools {

    private final IncidentRepository repository;

    public IncidentTools(IncidentRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void seedInitialData() {
        if (repository.count() == 0) {
            repository.save(new Incident("payment-gateway", "CRITICAL",
                    "ConnectionPoolTimeoutException: Unable to acquire JDBC connection from pool", "OPEN"));
            repository.save(new Incident("payment-gateway", "HIGH",
                    "DownstreamTimeoutException: FeignClient call to SwiftSettlementService exceeded 5000ms", "OPEN"));
        }
    }

    @Tool(description = "Checks health metrics and fetches all OPEN database incidents for a microservice.")
    public Map<String, Object> checkServiceHealth(
            @ToolParam(description = "Name of the microservice, e.g., payment-gateway") String serviceName) {

        List<Incident> openIncidents = repository.findByServiceNameIgnoreCaseAndStatus(serviceName, "OPEN");
        boolean isDegraded = !openIncidents.isEmpty();

        return Map.of(
                "service", serviceName,
                "status", isDegraded ? "DEGRADED" : "HEALTHY",
                "cpuUsage", isDegraded ? "94%" : "21%",
                "openIncidentCount", openIncidents.size(),
                "openIncidents", openIncidents,
                "checkedAt", Instant.now().toString()
        );
    }

    @Tool(description = "Logs a new production incident into the SQL database.")
    public Incident reportIncident(
            @ToolParam(description = "Name of the microservice") String serviceName,
            @ToolParam(description = "Severity level: LOW, MEDIUM, HIGH, or CRITICAL") String severity,
            @ToolParam(description = "Detailed error description") String message) {

        return repository.save(new Incident(serviceName, severity.toUpperCase(), message, "OPEN"));
    }

    @Tool(description = "Marks an open incident as RESOLVED in the database by its numeric ID.")
    public String resolveIncident(
            @ToolParam(description = "Numeric ID of the incident to resolve") Long incidentId) {

        return repository.findById(incidentId)
                .map(incident -> {
                    incident.setStatus("RESOLVED");
                    repository.save(incident);
                    return "Incident #" + incidentId + " for " + incident.getServiceName() + " marked as RESOLVED.";
                })
                .orElse("Incident #" + incidentId + " not found.");
    }
}
