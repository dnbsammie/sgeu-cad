package com.sgeu.cad.repository;

import com.sgeu.cad.model.Emergency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmergencyRepository extends JpaRepository<Emergency, UUID> {
}

