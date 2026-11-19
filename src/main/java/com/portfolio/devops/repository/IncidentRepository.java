package com.portfolio.devops.repository;

import com.portfolio.devops.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByServiceNameIgnoreCaseAndStatus(String serviceName, String status);
}
