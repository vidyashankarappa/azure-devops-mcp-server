package com.portfolio.devops.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "incidents")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceName;
    private String severity;
    private String message;
    private String status;
    private Instant createdAt;

    public Incident() {}

    public Incident(String serviceName, String severity, String message, String status) {
        this.serviceName = serviceName;
        this.severity = severity;
        this.message = message;
        this.status = status;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getServiceName() { return serviceName; }
    public String getSeverity() { return severity; }
    public String getMessage() { return message; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
}
