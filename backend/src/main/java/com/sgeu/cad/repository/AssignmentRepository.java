package com.sgeu.cad.repository;

import com.sgeu.cad.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {
	List<Assignment> findByIncidentId(UUID incidentId);
	List<Assignment> findByUnitIdAndActiveTrue(UUID unitId);
}

