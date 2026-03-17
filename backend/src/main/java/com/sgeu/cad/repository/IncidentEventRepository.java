package com.sgeu.cad.repository;

import com.sgeu.cad.model.IncidentEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IncidentEventRepository extends JpaRepository<IncidentEvent, UUID> {
	List<IncidentEvent> findByIncidentIdOrderByAtAsc(UUID incidentId);
}

