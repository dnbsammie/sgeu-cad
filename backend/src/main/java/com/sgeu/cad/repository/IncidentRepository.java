package com.sgeu.cad.repository;

import com.sgeu.cad.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IncidentRepository extends JpaRepository<Incident, UUID> {
	Optional<Incident> findByEmergencyId(UUID emergencyId);
}

